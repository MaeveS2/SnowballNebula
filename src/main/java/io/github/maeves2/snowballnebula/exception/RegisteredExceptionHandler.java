package io.github.maeves2.snowballnebula.exception;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RegisteredExceptionHandler {
    /**
     * List of all commands that this exception handler can handle.
     * If this list contains "*", all commands can be handled.
     */
    private List<String> commands;

    /**
     * Instance of mapped object.
     */
    private Object instance;

    /**
     * The method which handles the exception, invoked when an exception is encountered.
     */
    private Method method;

    /**
     * The exception that can be handled by this handler.
     */
    private Class<? extends Exception> handledException;

    /**
     * The constructor of this class.
     */
    public RegisteredExceptionHandler(String[] commands, Object instance, Method method, Class<? extends Exception> handledException) {
        this.commands = Arrays.asList(commands);
        this.instance = instance;
        this.method = method;
        this.handledException = handledException;
    }

    /**
     * @return The instance
     */
    public Object getInstance() {
        return instance;
    }

    /**
     * @return The list of handled commands
     */
    public List<String> getCommands() {
        return commands;
    }
     /**
     * @param commandName The name of the command to be checked
     * @return Whether a command can be handled or not
     */
    public boolean canHandle(String commandName) {
        return this.commands.contains(commandName) || this.commands.contains("*");
    }

    /**
     * @return The handling method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @return The handled exception
     */
    public Class<? extends Exception> getHandledException() {
        return handledException;
    }
}
