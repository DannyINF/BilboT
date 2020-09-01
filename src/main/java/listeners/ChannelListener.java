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
        SET_CHANNEL set_channel = CHANNEL.getSetChannel("log", event.getGuild().getId());
        if (set_channel.getMsg()) {
            MessageActions.neededChannel(event, "log");
        } else {
            TextChannel log = event.getGuild().getTextChannelById(set_channel.getChannel());
            assert log != null;
            log.sendMessage(MessageActions.getLocalizedString("log_channel_created", "server", event.getGuild().getId())
                    .replace("[CHANNEL]", event.getChannel().getAsMention())).queue();
        }
    }

    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        SET_CHANNEL set_channel = CHANNEL.getSetChannel("log", event.getGuild().getId());
        if (set_channel.getMsg()) {
            MessageActions.neededChannel(event, "log");
        } else {
            TextChannel log = event.getGuild().getTextChannelById(set_channel.getChannel());
            assert log != null;
            log.sendMessage(MessageActions.getLocalizedString("log_channel_deleted", "server", event.getGuild().getId())
                    .replace("[CHANNEL]", event.getChannel().getName())).queue();
        }
    }

    public void onVoiceChannelCreate(@NotNull VoiceChannelCreateEvent event) {
        SET_CHANNEL set_channel = CHANNEL.getSetChannel("log", event.getGuild().getId());
        if (set_channel.getMsg()) {
            MessageActions.neededChannel(event, "log");
        } else {
            TextChannel log = event.getGuild().getTextChannelById(set_channel.getChannel());
            assert log != null;
            log.sendMessage(MessageActions.getLocalizedString("log_voice_created", "server", event.getGuild().getId())
                    .replace("[CHANNEL]", event.getChannel().getName())).queue();
        }
    }

    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {
        SET_CHANNEL set_channel = CHANNEL.getSetChannel("log", event.getGuild().getId());
        if (set_channel.getMsg()) {
            MessageActions.neededChannel(event, "log");
        } else {
            TextChannel log = event.getGuild().getTextChannelById(set_channel.getChannel());
            assert log != null;
            log.sendMessage(MessageActions.getLocalizedString("log_voice_deleted", "server", event.getGuild().getId())
                    .replace("[CHANNEL]", event.getChannel().getName())).queue();
        }
    }

    @Override
    public void onVoiceChannelUpdateName(@NotNull VoiceChannelUpdateNameEvent event) {
        event.getGuild().getTextChannelsByName(event.getOldName().replace(" ", "-"), true).get(0)
                .getManager().setName(event.getNewName()).setTopic(MessageActions.getLocalizedString("channel_chat_for", "server", event.getGuild().getId())
                .replace("[CHANNELNAME]", event.getChannel().getName())).queue();
    }
}
