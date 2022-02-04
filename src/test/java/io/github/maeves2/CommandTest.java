package io.github.maeves2;

import io.github.maeves2.snowballnebula.commands.AutoRegister;
import io.github.maeves2.snowballnebula.commands.SlashCommand;
import io.github.maeves2.snowballnebula.exception.ExceptionHandler;
import io.github.maeves2.snowballnebula.util.Utilities;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@AutoRegister
public class CommandTest {

    @SlashCommand(name = "say", desc = "Make the bot say something", perm = Permission.UNKNOWN, options = {
            @SlashCommand.Option(type = OptionType.STRING, name = "text", desc = "Text to say", required = true),
            @SlashCommand.Option(type = OptionType.BOOLEAN, name = "embed", desc = "Make it an embed?")
    })
    public static void say(SlashCommandEvent event) {
        var text = event.getOption("idk").getAsString();
        throw new RuntimeException();
    }

    @ExceptionHandler(commands = "*", exception = RuntimeException.class)
    public static void handle(RuntimeException e, SlashCommandEvent event) {
        event.replyEmbeds(Utilities.failEmbed(e.toString())).queue();
    }
}
