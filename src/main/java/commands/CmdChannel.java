package commands;

import core.DatabaseHandler;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class CmdChannel {

    public static void channel(SlashCommandEvent event) throws SQLException {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else

        if ("set".equalsIgnoreCase(event.getSubcommandName())) {
            String channeltype = event.getOption("channel_type").getAsString();
            String channel = event.getOption("channel_channel").getAsMentionable().getAsMention().replace(">", "").replace("<", "")
                    .replace("#", "");
            if (channeltype.equals("log") || channeltype.equals("modlog") || channeltype.equals("spam") ||
                    channeltype.equals("voicelog") || channeltype.equals("cmdlog")) {
                DatabaseHandler.database("serversettings", "update channels set " + channeltype + " = '" + channel + "' where id = '" + event.getGuild().getId() + "'");
            } else {
                event.getChannel().sendMessage("Wrong channeltype!").queue();
            }
        }
    }


}
