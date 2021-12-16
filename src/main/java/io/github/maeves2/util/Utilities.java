package io.github.maeves2.util;

import io.github.maeves2.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for this library, destined for internal use. Please note that
 * all methods in this class
 * are static.
 * @since 1.0.0
 * @author MaeveS2
 * */
public class Utilities {
    /**
     * Don't let anyone instantiate this class.
     */
    private Utilities() {
        throw new UnsupportedOperationException("No instances for you.");
    }

    /**
     * Return the current time, formatted like this: {@code [HH:mm:ss]}.
     * @return {@link String} containing the formatted date
     */
    public static String now() {
        var dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "[" + dtf.format(LocalDateTime.now()) + "] ";
    }

     /**
     * Creates a list of {@link OptionData} from one or more
     * {@link io.github.maeves2.commands.SlashCommand.Option} annotations
     * @param options The options (varargs)
     * @return {@link List}
     */
    public static List<OptionData> asOptionData(SlashCommand.Option... options) {
        var ret = new ArrayList<OptionData>();
        for (var option : options)
            ret.add(new OptionData(option.type(), option.name(),
                    option.desc(), option.required()));
        return ret;
    }

    /**
     * Creates a standard failure embed, used to express the occurrence of
     * an internal failure. It has the color {@code #FF212D} (red) and a
     * timestamp, {@link Utilities#now()}.
     * @param content Content of the embed
     * @return {@link MessageEmbed}
     */
    public static MessageEmbed failEmbed(String content) {
        return new EmbedBuilder()
                .setColor(Color.decode("#FF212D"))
                .setAuthor("\u274c" + content)
                .setTimestamp(Instant.now())
                .build();
    }

    /**
     * Find all classes in a package and put them into a {@link Set}.
     * @param targetPackage The package to be searched
     * @return {@link Set} of all classes
     */
    public static Set<Class<?>> getAllClassesFromPackage(String targetPackage) {
        var stweam = ClassLoader.getSystemClassLoader().getResourceAsStream(
                targetPackage.replaceAll("[.]", "/"));
        Objects.requireNonNull(stweam);
        return new BufferedReader(new InputStreamReader(stweam)).lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClassFromPackage(line, targetPackage))
                .collect(Collectors.toSet());
    }

    /**
     * Find one specific class in a package
     * @param className The name of the searched class
     * @param packageName The package to be searched
     * @return {@link Class}
     */
    public static Class<?> getClassFromPackage(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
