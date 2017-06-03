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
    public abstract void write(JsonObjectBuilder out, String name, T value);
    public abstract void write(JsonArrayBuilder out, T value);

    public static final TypeAdapter<Integer> INTEGER = new TypeAdapter<Integer>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Integer value) {
                out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, Integer value) {
               out.add(value);
        }
    };

    public static final TypeAdapter<Long> LONG = new TypeAdapter<Long>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Long value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, Long value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<Byte> BYTE = new TypeAdapter<Byte>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Byte value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, Byte value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<Short> SHORT = new TypeAdapter<Short>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Short value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, Short value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<Double> DOUBLE = new TypeAdapter<Double>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Double value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, Double value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<Float> FLOAT = new TypeAdapter<Float>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Float value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, Float value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() {
        @Override
        public void write(JsonObjectBuilder out, String name, BigInteger value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, BigInteger value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
        @Override
        public void write(JsonObjectBuilder out, String name, BigDecimal value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, BigDecimal value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<JsonValue> JSON_VALUE = new TypeAdapter<JsonValue>() {
        @Override
        public void write(JsonObjectBuilder out, String name, JsonValue value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, JsonValue value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Boolean value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, Boolean value) {
            out.add(value);
        }
    };

    public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() {
        @Override
        public void write(JsonObjectBuilder out, String name, Character value) {
            out.add(name, String.valueOf(value));
        }

        @Override
        public void write(JsonArrayBuilder out, Character value) {
            out.add(String.valueOf(value));
        }
    };

    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        @Override
        public void write(JsonObjectBuilder out, String name, String value) {
            out.add(name, value);
        }

        @Override
        public void write(JsonArrayBuilder out, String value) {
            out.add(value);
        }
    };
}
