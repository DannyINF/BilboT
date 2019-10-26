package listeners;

import core.databaseHandler;
import core.messageActions;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.sql.SQLException;
import java.util.Objects;

public class introListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        SET_CHANNEL set_channel = CHANNEL.getSetChannel("spam", event.getGuild().getId());
        if (set_channel.getMsg()) {
            messageActions.neededChannel(event, "spam");
        } else {
            TextChannel spam = event.getGuild().getTextChannelsByName("bilbot-communication-channel", true).get(0);

            if (!event.getMember().getUser().isBot()) {
                boolean print;
                print = !event.getChannelJoined().getName().equals("\uD83D\uDCDA-Lyrikecke");
                boolean isOnServer;
                try {
                    isOnServer = Objects.requireNonNull(event.getGuild().getMemberById("454613079804608522")).getOnlineStatus().equals(OnlineStatus.ONLINE);
                } catch (Exception e) {
                    isOnServer = false;
                }
                if (print && isOnServer) {
                    String intro;
                    String[] arguments = {"users", "id = '" + event.getMember().getUser().getId() + "'", "1", "intro"};
                    try {
                        intro = Objects.requireNonNull(databaseHandler.database(event.getGuild().getId(), "select", arguments))[0].split("#")[0];
                    } catch (SQLException e) {
                        intro = "NO_INTRO";
                    }
                    spam.sendMessage(
                            Objects.requireNonNull(event.getGuild().getMemberById("454613079804608522")).getAsMention() + " music " + event.getMember().getUser().getId() + " play " + intro
                    ).queue();
                }
            }
        }
    }
}
