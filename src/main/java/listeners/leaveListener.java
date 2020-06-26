package listeners;

import core.messageActions;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.sql.SQLException;
import java.util.Objects;


public class leaveListener extends ListenerAdapter {


    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

            SET_CHANNEL set_channel = CHANNEL.getSetChannel("log", event.getGuild().getId());
            if (set_channel.getMsg()) {
                messageActions.neededChannel(event, "log");
            } else {
                TextChannel log = event.getGuild().getTextChannelById(set_channel.getChannel());

                TextChannel welcome = event.getGuild().getDefaultChannel();

                assert log != null;
                log.sendMessage(messageActions.getLocalizedString("log_user_leave", "server", event.getGuild().getId())
                        .replace("[USER]", event.getUser().getName() + "#" + event.getUser().getDiscriminator())).queue();
                assert welcome != null;
                welcome.sendMessage("Nam\u00E1ri\u00EB " + Objects.requireNonNull(event.getMember()).getAsMention() + " (" + event.getMember().getEffectiveName() + ")! M\u00F6ge Il\u00FAvatar mit dir sein!").queue();
                try {
                    core.databaseHandler.database(event.getGuild().getId(), "update users set ticket = 1 where id = '" + event.getMember().getId() + "'");
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

    }
}
