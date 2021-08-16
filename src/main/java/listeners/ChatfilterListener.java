package listeners;

import core.MessageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;


public class ChatfilterListener extends ListenerAdapter {
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        //TODO: textfilter which is channel- and user-dynamic

    }
}

