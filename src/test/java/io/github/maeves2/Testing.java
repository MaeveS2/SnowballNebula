package io.github.maeves2;

import io.github.maeves2.snowballnebula.SnowballNebula;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Testing {
    public static void main(String... args) throws LoginException, InterruptedException {
        var token = args[0];
        var jda = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.IDLE)
                .setActivity(Activity.competing("your mother"))
                .build();
        jda.awaitReady();

        var snowball = new SnowballNebula(jda)
                .enableLogging()
                .enableAutoRegistering(Testing.class.getPackageName());
    }
}
