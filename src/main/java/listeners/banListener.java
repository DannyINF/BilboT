package listeners;

import core.databaseHandler;
import core.messageActions;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.sql.SQLException;


public class banListener extends ListenerAdapter {


    public void onGuildBan(@NotNull GuildBanEvent event) {
        String[] arguments3 = {"users", "id = '" + event.getUser().getId() + "'", "verifystatus", "FALSE"};
        try {
            databaseHandler.database(event.getGuild().getId(), "update", arguments3);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextChannel welcome = event.getGuild().getDefaultChannel();
        SET_CHANNEL set_channel2 = CHANNEL.getSetChannel("log", event.getGuild().getId());
        if (set_channel2.getMsg()) {
            messageActions.neededChannel(event, "log");
        } else {
            TextChannel log = event.getGuild().getTextChannelById(set_channel2.getChannel());
            assert log != null;
            log.sendMessage(messageActions.getLocalizedString("log_ban", "server", event.getUser().getId())
                    .replace("[USER]", event.getUser().getAsTag())).queue();
            assert welcome != null;
            welcome.sendMessage(event.getUser().getAsMention() + " klebt jetzt am Banhammer!").queue();
        }
    }

    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        event.getGuild().getTextChannelsByName("logchannel", true).get(0).sendMessage(
                messageActions.getLocalizedString("log_unban", "server", event.getGuild().getId())
                        .replace("[USER]", event.getUser().getName() + "#" + event.getUser().getDiscriminator())).queue();
    }
}
