package io.github.maeves2.snowballnebula.commands;

import io.github.maeves2.snowballnebula.SnowballNebula;

import java.lang.annotation.*;

/**
 * This annotation is used to mark a class for auto-registry. Its methods
 * annotated with {@link SlashCommand} will automatically be registered
 * and do not need to be explicitly registered using
 * {@link SnowballNebula#register(Class[])}.
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRegister {
}
