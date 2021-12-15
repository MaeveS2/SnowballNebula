package io.github.maeves2.commands;

import java.lang.annotation.*;

/**
 * This annotation is used to mark a class for auto-registry. Its methods
 * annotated with {@link SlashCommand} will automatically be registered
 * and do not need to be explicitly registered using
 * {@link io.github.maeves2.SnowballNebula#register(Class[])}.
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRegister {
}
