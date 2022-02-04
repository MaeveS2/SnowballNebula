package io.github.maeves2.snowballnebula.exception;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionHandler {
    /**
     * The commands that this exception handler can handle.
     * If this is set to "*", all commands will be handled.
     * @return String array
     */
    String[] commands() default "*";

    /**
     * The exception that can be handled by this exception handler.
     * @return Exception class
     */
    Class<? extends Exception> exception() default Exception.class;
}
