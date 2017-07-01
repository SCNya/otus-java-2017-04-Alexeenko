package com.otus.alexeenko.database.services;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vsevolod on 18/06/2017.
 */
public abstract class ReflectionHelper {

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                return type.getConstructor(toClasses(args)).newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?>[] toClasses(Object[] args) {
        List<Class<?>> classes = Arrays.stream(args).map(Object::getClass).collect(Collectors.toList());

        return classes.toArray(new Class<?>[classes.size()]);
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        boolean access = true;

        try {
            access = field.isAccessible();

            if (!access)
                field.setAccessible(true);

            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !access)
                field.setAccessible(false);
        }
    }

    public static Field[] getFields(Class<?> clazz) {
        Field[] parentFields = clazz.getSuperclass().getDeclaredFields();
        Field[] childFields = clazz.getDeclaredFields();
        int parentLength = parentFields.length;
        int childLength = childFields.length;

        Field[] fields = new Field[parentLength + childLength];
        System.arraycopy(parentFields, 0, fields, 0, parentLength);
        System.arraycopy(childFields, 0, fields, parentLength, childLength);
        return fields;
    }

    public static Object getValue(Object dataSet, Field field) {
        Object value = null;
        boolean access = true;

        try {
            access = field.isAccessible();

            if (!access)
                field.setAccessible(true);
            value = field.get(dataSet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !access)
                field.setAccessible(false);
        }

        return value;
    }

    public static String getJoinColumnName(Field field, String name) {
        if (isJoinTable(field)) {
            String joinColumnName = getJoinColumnNameFromAnnotation(field);

            if (!joinColumnName.equals(""))
                return joinColumnName;
            else return field.getName();
        } else
            return name + "_id";
    }

    public static String getInverseJoinColumnName(Field field) {
        if (isJoinTable(field)) {
            String inverseJoinColumnName = getInverseJoinColumnNameFromAnnotation(field);

            if (!inverseJoinColumnName.equals(""))
                return inverseJoinColumnName;
            else return field.getName();
        } else
            return "id";
    }

    public static String getTableName(Class<?> clazz) {
        String tableName = clazz.getAnnotation(Table.class).name();

        if (!tableName.equals(""))
            return tableName;
        else
            return clazz.getSimpleName();
    }

    public static String getColumnName(Field field) {
        String columnName = field.getAnnotation(Column.class).name();

        if (!columnName.equals(""))
            return columnName;
        else return field.getName();
    }

    public static boolean isEntity(Class<?> clazz) {
        return clazz.getAnnotation(Entity.class) != null;
    }

    public static boolean isTable(Class<?> clazz) {
        return clazz.getAnnotation(Table.class) != null;
    }

    public static boolean isMappedSuperclass(Class<?> clazz) {
        return clazz.getSuperclass().getAnnotation(MappedSuperclass.class) != null;
    }

    public static boolean isColumn(Field field) {
        return field.getAnnotation(Column.class) != null;
    }

    public static boolean isTransient(Field field) {
        return field.getAnnotation(Transient.class) != null;
    }

    public static boolean isOneToOne(Field field) {
        return field.getAnnotation(OneToOne.class) != null;
    }

    public static boolean isOneToMany(Field field) {
        return field.getAnnotation(OneToMany.class) != null;
    }

    private static boolean isJoinTable(Field field) {
        return field.getAnnotation(JoinTable.class) != null;
    }

    public static boolean isID(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

    private static String getJoinColumnNameFromAnnotation(Field field) {
        return field.getAnnotation(JoinTable.class).joinColumns()[0].name();
    }

    private static String getInverseJoinColumnNameFromAnnotation(Field field) {
        return field.getAnnotation(JoinTable.class).inverseJoinColumns()[0].name();
    }

    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isNumber(Object value) {
        return value instanceof Number;
    }

    public static Class<?> getGenericClass(Field field) {
        return (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    public static Object[] buildCollection(Object value) {
        return ((Collection) value).toArray();
    }
}
