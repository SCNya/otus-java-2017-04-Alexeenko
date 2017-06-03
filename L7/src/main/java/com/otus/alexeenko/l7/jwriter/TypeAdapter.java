package com.otus.alexeenko.l7.jwriter;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Vsevolod on 01/06/2017.
 */
public abstract class TypeAdapter<T> {
    public abstract <V> void write(V out, String name, T value);

    public static final TypeAdapter<Integer> INTEGER = new TypeAdapter<Integer>() {
        @Override
        public <V> void write(V out, String name, Integer value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<Long> LONG = new TypeAdapter<Long>() {
        @Override
        public <V> void write(V out, String name, Long value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<Byte> BYTE = new TypeAdapter<Byte>() {
        @Override
        public <V> void write(V out, String name, Byte value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<Short> SHORT = new TypeAdapter<Short>() {
        @Override
        public <V> void write(V out, String name, Short value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<Double> DOUBLE = new TypeAdapter<Double>() {
        @Override
        public <V> void write(V out, String name, Double value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<Float> FLOAT = new TypeAdapter<Float>() {
        @Override
        public <V> void write(V out, String name, Float value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() {
        @Override
        public <V> void write(V out, String name, BigInteger value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
        @Override
        public <V> void write(V out, String name, BigDecimal value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<JsonValue> JSON_VALUE = new TypeAdapter<JsonValue>() {
        @Override
        public <V> void write(V out, String name, JsonValue value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
        @Override
        public <V> void write(V out, String name, Boolean value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };

    public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() {
        @Override
        public <V> void write(V out, String name, Character value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, String.valueOf(value));
            else
                ((JsonArrayBuilder) out).add(String.valueOf(value));
        }
    };

    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        @Override
        public <V> void write(V out, String name, String value) {
            if (out instanceof JsonObjectBuilder)
                ((JsonObjectBuilder) out).add(name, value);
            else
                ((JsonArrayBuilder) out).add(value);
        }
    };
}
