package com.otus.alexeenko.l7;

import com.google.gson.Gson;
import com.otus.alexeenko.l7.jwriter.JWriter;

/**
 * Created by Vsevolod on 30/05/2017.
 */
public class L7 {
    public static void main(String[] args) {
        Gson gson = new Gson();
        String json = gson.toJson(new PrimitiveClass());
        System.out.println(json);

        JWriter jWriter = new JWriter();
        json = jWriter.toJson(new PrimitiveClass());
        System.out.println(json);
    }
}
