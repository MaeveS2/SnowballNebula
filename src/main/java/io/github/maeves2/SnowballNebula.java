package io.github.maeves2;

import io.github.maeves2.commands.AutoRegister;
import io.github.maeves2.commands.RegisteredCommand;
import io.github.maeves2.commands.SlashCommand;
import io.github.maeves2.util.Utilities;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main class of the SnowballNebula framework. Used to register commands,
 * enable auto registering and get registered commands as {@link RegisteredCommand}.
 * <h2>Roadmap</h2>
 * <li>Command aliases (SoonTM)</li>
 * <li>Automatically registering commands in guilds, global commands (SoonTM)</li>
 * <li>Databases (NeverTM)</li>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * var snowball = new SnowballNebula(jda)
 *                 .enableLogging()
 *                 .enableAutoRegistering(Testing.class.getPackageName());
 * var commands = snowball.getCommands();
 * jda.getGuilds().forEach(e -> e.updateCommands().addCommands(commands).queue());
 * }</pre>
 *
 * @since 1.0.0
 * @author MaeveS2
 */
public class oh SnowballNebula extends ListenerAdapter {
    /**
     * Your JDA instance
     */
    private JDA jda;
    /**
     * Map containing all commands that were registred using {@link SnowballNebula#register(Class[])}
     * or auto-registering. Can be retrieved using {@link SnowballNebula#getCommand(String)}
     * or {@link SnowballNebula#getCommands()}
     */
    private Map<String, RegisteredCommand> registry;
    /**
     * List of all guilds the connected bot is in
     */
    private List<Guild> guilds;
    /**
     * Should certain events events be logged? Connection to a bot is aways logged.
     */
    private boolean log = false;
    /**
     * Logger for this class
     */
    private final Logger logger = LoggerFactory.getLogger(SnowballNebula.class);
    /**
     * COMING SOON<strong>TM</strong>
     */
    private boolean globalCommands = false;

    /**
     * Constructor of this class :bruh: <br>
     * <strong>Please make sure to use {@link JDA#awaitReady()} before
     * creating an instance of this class!!</strong>
     * @param jda Your JDA instance
     */
    public SnowballNebula(JDA jda) {
        jda.addEventListener(this);
        this.jda = jda;
        logger.info("Successfully connected to bot " + jda.getSelfUser().getName());
        this.registry = new HashMap<>();
        this.guilds = jda.getGuilds();
    }

    /**
     * Enable logging for the framework. If logging is enabled, the following
     * will be logged:
     * <li>Registering of commands</li>
     * <li>Execution of commands</li>
     * <li>Enabling of auto-registering</li>
     *
     * @return {@link SnowballNebula} for chaining convenience
     */
    public SnowballNebula enableLogging() {
        this.log = true;
        return this;
    }

    /**
     * Log a message at the INFO level using slf4j
     *
     * @param message message to log
     */
    private void log(String message) {
        if(log) logger.info(message);
    }

    /**
     * Returns the specified JDA instance, destined for internal use.
     *
     * @return {@link JDA}
     */
    public JDA getJda() {
        return jda;
    }

    /**
     * Scans one or more classes for methods annotated with {@link SlashCommand}
     * and registers them.
     * @param classes The class or classes that should be checked for commands
     * @return {@link SnowballNebula} for chaining convenience
     */
    public SnowballNebula register(Class<?>... classes) {
        Arrays.stream(classes).forEach(clazz -> {
            for (var method : clazz.getMethods()) {
                var command = method.getAnnotation(SlashCommand.class);
                if (command != null) {
                    var options = Utilities.asOptionData(command.options());
                    var data = new CommandData(command.name(), command.desc()).addOptions(options);
                    registry.putIfAbsent(command.name(), new RegisteredCommand(
                            clazz, command.name(), command.perm(), command.permissionMessage(),
                            method, data, options));
                    log("Registered new command (/" + command.name() + ") from " + clazz.getName());
                } else
                    continue;
            }
        });
        return this;
    }

    /**
     * Enables auto-registering for a specified package. Classes that should
     * automatically be registered are marked with the marker annotation
     * {@link AutoRegister}.
     * @param targetPackage The package in which auto-registering will be
     *        enabled. Can be obtained using {@link Class#getPackageName()}.
     * @return {@link SnowballNebula} for chaining convenience
     */
    public SnowballNebula enableAutoRegistering(String targetPackage) {
        Utilities.getAllClassesFromPackage(targetPackage).stream()
                .filter(e -> e.isAnnotationPresent(AutoRegister.class))
                .collect(Collectors.toSet())
                .forEach(e -> register(e));
        log("Enabled automatic registering");
        return this;
    }

    /**
     * Code to execute when a slash command is used. Overridden from the
     * {@link ListenerAdapter} class.
     * <h2>Do not use this method!!</h2>
     */
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getUser().equals(jda.getSelfUser())) return;
        var command = getRegisteredCommand(event.getName());
        Objects.requireNonNull(command);

        var perm = command.getPerm();
        if (!event.getMember().hasPermission(perm)) {
            var message = command.getPermissionMessage().replace("$PERMISSION$",
                    "`" + perm.getName() + "`");
            event.replyEmbeds(Utilities.failEmbed(message));
            return;
        }
        try {
            command.getMethod().invoke(command.getInstance(), event);
            log(event.getUser().getAsTag() + " executed command /" +
                    event.getName() + " in " + event.getGuild());
        } catch (Exception e) {
            event.replyEmbeds(Utilities.failEmbed("Sorry, something went wrong..."));
            e.printStackTrace();
        }
    }

    public SnowballNebula addCommandsToGuilds() {
        jda.getGuilds().forEach(e -> e.updateCommands().addCommands(this.getCommands()).queue());
        var guildNames = jda.getGuilds().stream().parallel().map(e -> e.getName()).collect(Collectors.toSet());
        log("Registered commands in guilds: " + guildNames);
        return this;
    }

    public SnowballNebula upsertCommands() {
        this.getCommands().forEach(e -> jda.upsertCommand(e));
        log("Upserted all commands, this might take up to one hour...");
        return this;
    }

    /**
     * Returns a registered command, destined for internal use.
     * @param name Name of the command
     * @return {@link RegisteredCommand}
     */
    public RegisteredCommand getRegisteredCommand(String name) {
        return registry.get(name);
    }

    /**
     * Returns the command data of a command by name.
     * @param name The name of the command
     * @return {@link CommandData}
     */
    public CommandData getCommand(String name) {
        return registry.get(name).getData();
    }

    /**
     * Returns a list of all command (command data)
     * @return {@link List}
     */
    public List<CommandData> getCommands() {
        return registry.values().stream().map(e -> e.getData()).toList();
    }
}
