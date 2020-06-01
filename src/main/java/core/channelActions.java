package core;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

public class channelActions {
    public static TextChannel getChannel(GuildMessageReceivedEvent event, String name) {
        try {
            String[] selectArgs = {"channels", "id = '" + event.getGuild().getId() + "'", "1", name};
            String id = Objects.requireNonNull(databaseHandler.database("serversettings", "select", selectArgs))[0];
            return event.getGuild().getTextChannelById(id);
        } catch (Exception e) {
            try {
                for (TextChannel tx : event.getGuild().getTextChannels()) {
                    if (tx.getName().contains("spam")) {
                        return tx;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return event.getGuild().getDefaultChannel();
    }

    public static TextChannel getChannel(GuildMemberJoinEvent event, String name) {
        try {
            String[] selectArgs = {"channels", "id = '" + event.getGuild().getId() + "'", "1", name};
            String id = Objects.requireNonNull(databaseHandler.database("serversettings", "select", selectArgs))[0];
            return event.getGuild().getTextChannelById(id);
        } catch (Exception e) {
            try {
                for (TextChannel tx : event.getGuild().getTextChannels()) {
                    if (tx.getName().contains("spam")) {
                        return tx;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return event.getGuild().getDefaultChannel();
    }
}
