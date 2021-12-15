# SnowballNebula
Discord slash commands framework, works with JDA (https://github.com/DV8FromTheWorld/JDA).

## Roadmap
 * Command aliases (SoonTM)
 * Automatically registering commands in guilds, global commands (SoonTM)
 * Databases (NeverTM)
 
## Documentation
### Example Usage
```java
var snowball = new SnowballNebula(jda)
                   .enableLogging()
                   .enableAutoRegistering(Testing.class.getPackageName());
var commands = snowball.getCommands();
jda.getGuilds().forEach(e -> e.updateCommands().addCommands(commands).queue());$
 ```
(`jda` being an instance of JDA)
 
### Creating a command
To create a command, you have to annotate a method with `SlashCommand` and define all the values.
Example (from src/test):
 ```java
 @SlashCommand(name = "say", desc = "Make the bot say something", perm = Permission.UNKNOWN, options = {
            @SlashCommand.Option(type = OptionType.STRING, name = "text", desc = "Text to say", required = true),
            @SlashCommand.Option(type = OptionType.BOOLEAN, name = "embed", desc = "Make it an embed?")
    })
    public static void say(SlashCommandEvent event) {
        var text = event.getOption("text").getAsString();
        event.reply(text).queue();
    }
 ```
The `Option` annotation is used to add an option to the command.

This code produces the following command: 
![](https://cdn.discordapp.com/attachments/902497478270672916/920778608568598548/unknown.png)

Then, you have to register the command by using the `SnowballNebula#register` method or by enabling auto-register
for that package. The method takes the `Class<?>` which contains the methodas argument. It can be obtained using
`<class name>.class`. (auto-registering is explained in the next paragraph)

### Auto-registering
Auto-registering is enabled using `SnowballNebula#enableAutoRegistering`, with the package name as argument. This
can be obtained using `<class name>.class.getPackageName()`.

![Snowball Nebula](https://cdn.discordapp.com/attachments/653632542100160547/920772806982131772/unknown.png)
