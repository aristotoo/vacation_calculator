package com.bogdan.calculator.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking methods that should be logged.
 * This annotation is used in conjunction with LoggingAspect to provide
 * method execution logging functionality.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    /**
     * Custom name for the method in logs.
     * If empty, the actual method name will be used.
     *
     * @return The custom method name
     */
    String value() default "";

    /**
     * Whether to log method parameters.
     *
     * @return true if parameters should be logged, false otherwise
     */
    boolean logParams() default true;

    /**
     * Whether to log method result.
     *
     * @return true if result should be logged, false otherwise
     */
    boolean logResult() default true;

    /**
     * Whether to log method execution time.
     *
     * @return true if execution time should be logged, false otherwise
     */
    boolean logExecutionTime() default true;
} 