package io.github.maeves2.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to create a new slash command. The slash command
 * has to be registered using {@link io.github.maeves2.SnowballNebula#register(Class[])}
 * or the class in which it is used has to be annotated with {@link AutoRegister}
 * and auto-registering has to be enabled.
 * <h2>Example</h2>
 * <pre>{@code
 * @SlashCommand(name = "say", desc = "Make the bot say something", perm = Permission.UNKNOWN, options = {
 *             @SlashCommand.Option(type = OptionType.STRING, name = "text", desc = "Text to say", required = true),
 *             @SlashCommand.Option(type = OptionType.BOOLEAN, name = "embed", desc = "Make it an embed?")
 *     })
 * }</pre>
 * @since 1.0.0
 * @author MaeveS2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlashCommand {
    /**
     * The name of the slash command, the annotated method is called when a
     * slash command with this name is used.
     * @return The name of the slash command
     */
    String name();

    /**
     * The description of the slash command, shown in the discord client. Defaults
     * to a blank description.
     * @return The description of the slash command
     */
    String desc() default "";

    /**
     * The permission needed to execute the slash command, specified using the
     * {@link Permission} enum. Defaults to {@code Permission.UNKNOWN}.
     * @return The permission of the slash command
     */
    Permission perm() default Permission.UNKNOWN;

    /**
     * The message sent when a user who doesn't have the needed permission
     * executes the command, default message available. The string {@code $PERMISSION$}
     * will be replaced with the name of the permission.
     * @return The permission message of the slash command
     */
    String permissionMessage() default "\u274c You do not have the needed permissions to execute this command ($PERMISSION$).";

    /**
     * The options of the slash command, shown in the discord client. Specified
     * using an array of the {@link Option} annotation.
     * @return An array of the options
     */
    Option[] options();

    /**
     * Nested annotation used to create an option for a slash command.
     */
    @interface Option {
        /**
         * Type of the option, specified using the {@link OptionType} enum.
         * @return Type of the option, for example {@code OptionType.STRING}
         */
        OptionType type();

        /**
         * The name of the option
         * @return The name of the option
         */
        String name();

        /**
         * The description of the option, defaults to a blank string.
         * @return The description of the option
         */
        String desc() default "";

        /**
         * Whether the option is required or not, defaults to {@code false}.
         * @return Whether the option is required
         */
        boolean required() default false;
    }
}
