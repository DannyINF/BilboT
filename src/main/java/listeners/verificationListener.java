package listeners;

import core.databaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

public class verificationListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getGuild().getDefaultChannel().equals(event.getChannel())) {
            Message msg;
            msg = event.getGuild().getDefaultChannel().retrieveMessageById(event.getMessageId()).complete();
            if (msg.getContentDisplay().contains(event.getUser().getAsMention()) && event.getReactionEmote().equals(MessageReaction.ReactionEmote.fromUnicode("✅", event.getJDA()))) {
                String[] arguments2 = {"users", "id = '" + event.getUserId() + "'", "verified", "TRUE"};
                try {
                    databaseHandler.database("usersettings", "update", arguments2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (Guild guild : event.getUser().getMutualGuilds()) {
                    String[] arguments3 = {"users", "id = '" + event.getUserId() + "'", "verifystatus", "TRUE"};
                    try {
                        databaseHandler.database(guild.getId(), "update", arguments3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (!Objects.requireNonNull(guild.getMember(event.getUser())).getRoles().contains(guild.getRolesByName("exil", true).get(0))) {
                        try {
                            guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(event.getUserId())), guild.getRolesByName("verified", true).get(0)).queue();
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }

    }
}
