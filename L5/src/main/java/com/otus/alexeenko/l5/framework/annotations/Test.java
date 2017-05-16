package com.otus.alexeenko.l5.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Vsevolod on 13/05/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {

    /**
     * Default empty exception
     */
    class None extends Throwable {
        private static final long serialVersionUID = 1L;

        private None() {
        }
    }

    Class<? extends Throwable> expected() default None.class;
}
