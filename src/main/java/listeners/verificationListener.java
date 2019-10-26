package listeners;

import core.databaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

public class verificationListener extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().toLowerCase().equals("ich bin kein bot!")) {
            String[] arguments2 = {"users", "id = '" + event.getAuthor().getId() + "'", "verified", "TRUE"};
            try {
                databaseHandler.database("usersettings", "update", arguments2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Guild guild : event.getAuthor().getMutualGuilds()) {
                String[] arguments3 = {"users", "id = '" + event.getAuthor().getId() + "'", "verifystatus", "TRUE"};
                try {
                    databaseHandler.database(guild.getId(), "update", arguments3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (!Objects.requireNonNull(guild.getMember(event.getAuthor())).getRoles().contains(guild.getRolesByName("exil", true).get(0))) {
                    try {
                        guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(event.getAuthor().getId())), guild.getRolesByName("verified", true).get(0)).queue();
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }
}
