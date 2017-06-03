package com.otus.alexeenko.l7.jwriter;

import com.google.gson.Gson;
import com.otus.alexeenko.l7.PrimitiveClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Vsevolod on 31/05/2017.
 */
public class JWriterTest {
    private final Gson gson = new Gson();
    private final JWriter jWriter = new SimpleJWriter();

    @Test
    public void toJson() {
        String json1 = gson.toJson(new PrimitiveClass());
        String json2 = jWriter.toJson(new PrimitiveClass());

        assertEquals(json1, json2);
    }

    @Test
    public void fromJson() {
        String json1 = gson.toJson(new PrimitiveClass());
        String json2 = jWriter.toJson(new PrimitiveClass());

        PrimitiveClass obj1 = gson.fromJson(json1, PrimitiveClass.class);
        PrimitiveClass obj2 = gson.fromJson(json2, PrimitiveClass.class);

        assertEquals(obj1, obj2);
    }

    @Test
    public void equals() {
        PrimitiveClass primitive1 = new PrimitiveClass();
        PrimitiveClass primitive2 = new PrimitiveClass();

        primitive1.setInteger(127);
        primitive2.setInteger(127);

        String json1 = gson.toJson(primitive1);
        String json2 = jWriter.toJson(primitive2);

        assertEquals(json1, json2);

        primitive1 = gson.fromJson(json1, PrimitiveClass.class);
        primitive2 = gson.fromJson(json2, PrimitiveClass.class);

        assertEquals(primitive1, primitive2);
    }

    @Test
    public void notEquals() {
        PrimitiveClass primitive1 = new PrimitiveClass();
        PrimitiveClass primitive2 = new PrimitiveClass();

        primitive1.setIs(true);
        primitive2.setIs(false);

        String json1 = gson.toJson(primitive1);
        String json2 = jWriter.toJson(primitive2);

        assertNotEquals(json1, json2);

        primitive1 = gson.fromJson(json1, PrimitiveClass.class);
        primitive2 = gson.fromJson(json2, PrimitiveClass.class);

        assertNotEquals(primitive1, primitive2);
    }
}