# SnowballNebula
Discord slash commands framework, works with JDA (https://github.com/DV8FromTheWorld/JDA).

## Roadmap
 * Command aliases (SoonTM)
 * Automatically registering commands in guilds, global commands (SoonTM)
 * Databases (NeverTM)
 * 
## Example usage
```java
var snowball = new SnowballNebula(jda)
                   .enableLogging()
                   .enableAutoRegistering(Testing.class.getPackageName());
var commands = snowball.getCommands();
jda.getGuilds().forEach(e -> e.updateCommands().addCommands(commands).queue());$
 ```
 (`jda` being an instance of JDA)
