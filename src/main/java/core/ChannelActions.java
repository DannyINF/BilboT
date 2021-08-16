package core;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

public class ChannelActions {
    public static TextChannel getChannel(GuildMessageReceivedEvent event, String name) {
        return getTextChannelInGuild(name, event.getGuild());
    }

    public static TextChannel getChannel(GuildMemberJoinEvent event, String name) {
        return getTextChannelInGuild(name, event.getGuild());
    }

    private static TextChannel getTextChannelInGuild(String name, Guild guild) {
        try {
            String id = Objects.requireNonNull(DatabaseHandler.database("serversettings", "select " + name + " from channels where id = '" + guild.getId() + "'"))[0];
            return guild.getTextChannelById(id);
        } catch (Exception e) {
            try {
                for (TextChannel tx : guild.getTextChannels()) {
                    if (tx.getName().contains("spam")) {
                        return tx;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return guild.getDefaultChannel();
    }
}
