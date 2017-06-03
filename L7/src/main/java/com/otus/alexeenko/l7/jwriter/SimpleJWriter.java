package com.otus.alexeenko.l7.jwriter;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.otus.alexeenko.l7.jwriter.TypeAdapter.*;

/**
 * Created by Vsevolod on 30/05/2017.
 */
public class SimpleJWriter implements JWriter {
    private static final Map<Class<?>, TypeAdapter<?>> adapters;

    static {
        Map<Class<?>, TypeAdapter<?>> typeAdapterMap = new HashMap<>();
        typeAdapterMap.put(Integer.class, INTEGER);
        typeAdapterMap.put(BigInteger.class, BIG_INTEGER);
        typeAdapterMap.put(BigDecimal.class, BIG_DECIMAL);
        typeAdapterMap.put(JsonValue.class, JSON_VALUE);
        typeAdapterMap.put(Long.class, LONG);
        typeAdapterMap.put(Double.class, DOUBLE);
        typeAdapterMap.put(Float.class, FLOAT);
        typeAdapterMap.put(Boolean.class, BOOLEAN);
        typeAdapterMap.put(Byte.class, BYTE);
        typeAdapterMap.put(Short.class, SHORT);
        typeAdapterMap.put(Character.class, CHARACTER);
        typeAdapterMap.put(String.class, STRING);

        adapters = Collections.unmodifiableMap(typeAdapterMap);
    }

    public SimpleJWriter() {
    }

    public String toJson(Object obj) {
        return findValues(Json.createObjectBuilder(), obj).build().toString();
    }

    private <T> T findValues(T builder, Object obj) {
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
        return builder;
    }

    @SuppressWarnings("ConstantConditions")
    private <T> void write(T builder, String name, Object value) {
        TypeAdapter adapter = adapters.get(value.getClass());

        if (isCollection(value)) {
            value = ((Collection) value).toArray();
        }

        if (adapter != null) {
            write(adapter, builder, name, value);

        } else if (isArray(value)) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (int j = 0; j < Array.getLength(value); ++j) {
                write(arrayBuilder, null, Array.get(value, j));
            }
            writeArray(builder, name, arrayBuilder);

        } else {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            if (isMap(value)) {
                Object[] keys = ((Map) value).keySet().toArray();
                Object[] values = ((Map) value).values().toArray();

                for (int j = 0; j < Array.getLength(keys); ++j)
                    write(objectBuilder, Array.get(keys, j).toString(), Array.get(values, j));
            } else {
                findValues(objectBuilder, value);
            }
            writeObject(builder, name, objectBuilder);
        }
    }

    private <T> void writeObject(T builder, String name, JsonObjectBuilder objectBuilder) {
        if (name != null)
            ((JsonObjectBuilder) builder).add(name, objectBuilder);
        else
            ((JsonArrayBuilder) builder).add(objectBuilder);
    }

    private <T> void writeArray(T builder, String name, JsonArrayBuilder arrayBuilder) {
        if (name != null)
            ((JsonObjectBuilder) builder).add(name, arrayBuilder);
        else
            ((JsonArrayBuilder) builder).add(arrayBuilder);
    }

    @SuppressWarnings("unchecked")
    private <T> void write(TypeAdapter adapter, T builder, String name, Object value) {
        if (name != null)
            adapter.write(((JsonObjectBuilder) builder), name, value);
        else
            adapter.write(((JsonArrayBuilder) builder), value);
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