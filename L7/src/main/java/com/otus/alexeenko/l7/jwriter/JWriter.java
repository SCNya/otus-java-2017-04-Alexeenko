package com.otus.alexeenko.l7.jwriter;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by Vsevolod on 30/05/2017.
 */
public class JWriter {
    private static final Pattern pattern = Pattern.compile("int|[Ff]loat|[Dd]ouble|[Ll]ong|[Ss]hort|" +
            "[Bb]yte|[Bb]oolean|Integer|Number|Decimal");
    private final StringBuilder result;

    public JWriter() {
        result = new StringBuilder();
    }

    public String toJson(Object obj) {
        return "{" + findValues(obj) + "}";
    }

    private String findValues(Object obj) {
        Field[] fields = null;
        boolean[] accesses = null;

        try {
            fields = obj.getClass().getDeclaredFields();
            accesses = new boolean[fields.length];

            for (int i = 0; i < fields.length; ++i) {
                Field field = fields[i];
                accesses[i] = field.isAccessible();

                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    if (!accesses[i])
                        field.setAccessible(true);

                    result.append('\"')
                            .append(fields[i].getName())
                            .append("\":");

                    if (isNeedQuotes(field.getType().getName()))
                        writeWithOutQuotes(field.get(obj));
                    else
                        writeWithQuotes(field.get(obj));

                    if (i != fields.length - 1)
                        result.append(',');
                }
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
        return result.toString();
    }

    private boolean isNeedQuotes(String name) {

        return pattern.matcher(name).find();
    }

    private void writeWithQuotes(Object value) {
        result.append('\"')
                .append(value.toString())
                .append('\"');
    }

    private void writeWithOutQuotes(Object value) {
        result.append(value.toString());
    }
}
