package listeners;

import core.MessageActions;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;


public class ChannelListener extends ListenerAdapter {

    public void onTextChannelCreate(@NotNull TextChannelCreateEvent event) {
        //TODO: log channel creation
    }

    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        //TODO: log channel deletion
    }

    public void onVoiceChannelCreate(@NotNull VoiceChannelCreateEvent event) {
        //TODO: log voicechannel creation
    }

    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {
        //TODO: log voice channel deletion
    }

    @Override
    public void onVoiceChannelUpdateName(@NotNull VoiceChannelUpdateNameEvent event) {
        //TODO: rename corresponding text channel when voice channel is renamed
    }
}
