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
    private final Map<Class<?>, TypeAdapter<?>> adapters;

    public SimpleJWriter() {
        Map<Class<?>, TypeAdapter<?>> adapters = new HashMap<>();
        adapters.put(Integer.class, INTEGER);
        adapters.put(BigInteger.class, BIG_INTEGER);
        adapters.put(BigDecimal.class, BIG_DECIMAL);
        adapters.put(JsonValue.class, JSON_VALUE);
        adapters.put(Long.class, LONG);
        adapters.put(Double.class, DOUBLE);
        adapters.put(Float.class, FLOAT);
        adapters.put(Boolean.class, BOOLEAN);
        adapters.put(Byte.class, BYTE);
        adapters.put(Short.class, SHORT);
        adapters.put(Character.class, CHARACTER);
        adapters.put(String.class, STRING);

        this.adapters = Collections.unmodifiableMap(adapters);
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
                write(arrayBuilder, name, Array.get(value, j));
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
        if (builder instanceof JsonObjectBuilder)
            ((JsonObjectBuilder) builder).add(name, objectBuilder);
        else
            ((JsonArrayBuilder) builder).add(objectBuilder);
    }

    private <T> void writeArray(T builder, String name, JsonArrayBuilder arrayBuilder) {
        if (builder instanceof JsonObjectBuilder)
            ((JsonObjectBuilder) builder).add(name, arrayBuilder);
        else
            ((JsonArrayBuilder) builder).add(arrayBuilder);
    }

    @SuppressWarnings("unchecked")
    private <T> void write(TypeAdapter adapter, T builder, String name, Object value) {
        adapter.write(builder, name, value);
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

    private static boolean isStatic(Field field) {
        return java.lang.reflect.Modifier.isStatic(field.getModifiers());
    }

    private static boolean isTransient(Field field) {
        return java.lang.reflect.Modifier.isTransient(field.getModifiers());
    }
}