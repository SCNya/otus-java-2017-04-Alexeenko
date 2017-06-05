package com.otus.alexeenko.l7.jwriter;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.otus.alexeenko.l7.jwriter.TypeAdapter.*;

/**
 * Created by Vsevolod on 30/05/2017.
 */
public class SimpleJWriter implements JWriter {
    private static final Map<Class<?>, TypeAdapter<?>> adapters;
    private final StringWriter result;

    static {
        Map<Class<?>, TypeAdapter<?>> typeAdaptersMap = new HashMap<>();
        typeAdaptersMap.put(Integer.class, INTEGER);
        typeAdaptersMap.put(BigInteger.class, BIG_INTEGER);
        typeAdaptersMap.put(BigDecimal.class, BIG_DECIMAL);
        typeAdaptersMap.put(JsonValue.class, JSON_VALUE);
        typeAdaptersMap.put(Long.class, LONG);
        typeAdaptersMap.put(Double.class, DOUBLE);
        typeAdaptersMap.put(Float.class, FLOAT);
        typeAdaptersMap.put(Boolean.class, BOOLEAN);
        typeAdaptersMap.put(Byte.class, BYTE);
        typeAdaptersMap.put(Short.class, SHORT);
        typeAdaptersMap.put(Character.class, CHARACTER);
        typeAdaptersMap.put(String.class, STRING);

        adapters = Collections.unmodifiableMap(typeAdaptersMap);
    }

    public SimpleJWriter() {
        result = new StringWriter();
    }

    public String toJson(Object obj) {
        TypeAdapter adapter = adapters.get(obj.getClass());
        JsonGenerator jGenerator = Json.createGenerator(result);

        if (adapter != null) {
            write(adapter, jGenerator, obj);
            jGenerator.close();
            return result.toString();
        } else {
            if (isCollection(obj))
                obj = buildCollection(obj);

            if (isArray(obj)) {
                writeArray(jGenerator, obj);
                jGenerator.close();
                return result.toString();
            } else {
                writeObject(jGenerator, obj);
                jGenerator.close();
                return result.toString();
            }
        }
    }

    private void findValues(JsonObjectBuilder builder, Object obj) {
        Field[] fields = null;
        boolean[] accesses = null;

        try {
            fields = obj.getClass().getDeclaredFields();
            accesses = new boolean[fields.length];

            for (int i = 0; i < fields.length; ++i) {
                Object value;
                Field field = fields[i];
                accesses[i] = field.isAccessible();

                if (!accesses[i])
                    field.setAccessible(true);

                value = field.get(obj);

                if (value != null && !isStatic(field)
                        && !isTransient(field))
                    write(builder, field.getName(), field.get(obj));
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (fields != null && accesses != null) {
                for (int i = 0; i < fields.length; ++i)
                    if (fields[i] != null && !accesses[i])
                        fields[i].setAccessible(false);
            }
        }
    }

    private void write(JsonObjectBuilder builder, String name, Object value) {
        TypeAdapter adapter = adapters.get(value.getClass());

        if (adapter != null) {
            write(adapter, builder, name, value);

        } else {
            if (isCollection(value))
                value = buildCollection(value);

            if (isArray(value))
                writeArray(builder, name, value);
            else
                writeObject(builder, name, value);
        }
    }

    private void write(JsonArrayBuilder builder, Object value) {
        TypeAdapter adapter = adapters.get(value.getClass());

        if (adapter != null) {
            write(adapter, builder, value);

        } else {
            if (isCollection(value))
                value = buildCollection(value);

            if (isArray(value))
                writeArray(builder, value);
            else
                writeObject(builder, value);
        }
    }

    private void writeObject(JsonObjectBuilder builder, String name, Object value) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (isMap(value))
            buildMap(value, objectBuilder);
        else
            findValues(objectBuilder, value);

        builder.add(name, objectBuilder); //{"" : ...}
    }

    private void writeObject(JsonArrayBuilder builder, Object value) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (isMap(value))
            buildMap(value, objectBuilder);
        else
            findValues(objectBuilder, value);

        builder.add(objectBuilder); //[{},{}]
    }

    private void writeObject(JsonGenerator builder, Object value) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (isMap(value))
            buildMap(value, objectBuilder);
        else
            findValues(objectBuilder, value);

        builder.write(objectBuilder.build()); //[{},{}]
    }

    private void buildMap(Object value, JsonObjectBuilder objectBuilder) {
        Map map = (Map) value;
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();

        for (int j = 0; j < Array.getLength(keys); ++j)
            write(objectBuilder, Array.get(keys, j).toString(), Array.get(values, j));
    }

    private void writeArray(JsonObjectBuilder builder, String name, Object value) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (int j = 0; j < Array.getLength(value); ++j) {
            write(arrayBuilder, Array.get(value, j));
        }
        builder.add(name, arrayBuilder);  //{"":[]}
    }

    private void writeArray(JsonArrayBuilder builder, Object value) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (int j = 0; j < Array.getLength(value); ++j) {
            write(arrayBuilder, Array.get(value, j));
        }
        builder.add(arrayBuilder);  //[[],[]]
    }

    private void writeArray(JsonGenerator builder, Object value) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (int j = 0; j < Array.getLength(value); ++j) {
            write(arrayBuilder, Array.get(value, j));
        }
        builder.write(arrayBuilder.build());  //[[],[]]
    }

    @SuppressWarnings("unchecked")
    private void write(TypeAdapter adapter, JsonObjectBuilder builder, String name, Object value) {
        adapter.write(builder, name, value);
    }

    @SuppressWarnings("unchecked")
    private void write(TypeAdapter adapter, JsonArrayBuilder builder, Object value) {
        adapter.write(builder, value);
    }

    @SuppressWarnings("unchecked")
    private void write(TypeAdapter adapter, JsonGenerator builder, Object value) {
        adapter.write(builder, value);
    }

    private Object[] buildCollection(Object value) {
        return ((Collection) value).toArray();
    }

    private boolean isArray(Object value) {
        return value.getClass().isArray();
    }

    private boolean isCollection(Object value) {
        return value instanceof Collection;
    }

    private boolean isMap(Object value) {
        return value instanceof Map;
    }

    private boolean isStatic(Field field) {
        return java.lang.reflect.Modifier.isStatic(field.getModifiers());
    }

    private boolean isTransient(Field field) {
        return java.lang.reflect.Modifier.isTransient(field.getModifiers());
    }
}