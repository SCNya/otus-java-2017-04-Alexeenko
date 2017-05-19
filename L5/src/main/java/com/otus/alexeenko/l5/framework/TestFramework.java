package com.otus.alexeenko.l5.framework;

import com.otus.alexeenko.l5.framework.annotations.After;
import com.otus.alexeenko.l5.framework.annotations.Before;
import com.otus.alexeenko.l5.framework.annotations.Test;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static com.otus.alexeenko.l5.framework.ReflectionHelper.callMethod;
import static com.otus.alexeenko.l5.framework.ReflectionHelper.instantiate;

/**
 * Created by Vsevolod on 13/05/2017.
 */
public class TestFramework {
    private final static String TEST_ANNOTATION_NAME = Test.class.getCanonicalName();
    private final static String BEFORE_ANNOTATION_NAME = Before.class.getCanonicalName();
    private final static String AFTER_ANNOTATION_NAME = After.class.getCanonicalName();

    public static Result runTests(String packageName) {
        final Set<Class<?>> classes = new HashSet<>();
        new FastClasspathScanner(packageName)
                .matchAllClasses(classes::add)
                .scan();

        return runTests(classes.toArray(new Class<?>[classes.size()]));
    }

    public static Result runTests(Class<?>... classes) {
        Result result = new Result();

        for (Class<?> clasz : classes) {
            TargetClass targetClass = new TargetClass(clasz);

            for (Method method : clasz.getMethods())
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation.annotationType().getCanonicalName()
                            .equals(BEFORE_ANNOTATION_NAME))
                        targetClass.addBefore(method);

                    if (annotation.annotationType().getCanonicalName()
                            .equals(TEST_ANNOTATION_NAME))
                        targetClass.addTest(method);

                    if (annotation.annotationType().getCanonicalName()
                            .equals(AFTER_ANNOTATION_NAME))
                        targetClass.addAfter(method);
                }
            check(targetClass, result);
        }

        return result;
    }

    private static void check(TargetClass targetClass, Result result) {

        for (Method testMethod : targetClass.getTestList()) {
            Object testObj = instantiate(targetClass.getClasz());
            Info info = null;
            boolean expectedException = false;
            Test testAnnotation;

            try {
                for (Method beforeMethod : targetClass.getBeforeList())
                    callMethod(testObj, beforeMethod);

                testAnnotation = testMethod.getAnnotation(Test.class);
                if ((testAnnotation.expected()).equals(Test.None.class))
                    expectedException = true;

                callMethod(testObj, testMethod);

                if (expectedException)
                    info = new Info(testMethod.getName(), true);
                else
                    info = new Info(testMethod.getName(), false, instantiate(testAnnotation.expected()));

                for (Method afterMethod : targetClass.getAfterList())
                    callMethod(testObj, afterMethod);

            } catch (Exception e) {
                testAnnotation = testMethod.getAnnotation(Test.class);

                if ((testAnnotation.expected().getName()).equals(e.getCause().toString()))
                    info = new Info(testMethod.getName(), true);

                if (info == null)
                    info = new Info(testMethod.getName(), false, e);
            } finally {
                result.add(info);
            }
        }
    }
}