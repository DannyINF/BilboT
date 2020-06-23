package commands;

import core.databaseHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class cmdChannel implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        switch (args[0].toLowerCase()) {
            case "set":
                String channeltype = args[1];
                String channel = args[2].replace(">", "").replace("<", "")
                        .replace("#", "");
                if (channeltype.equals("log") || channeltype.equals("modlog") || channeltype.equals("spam") ||
                        channeltype.equals("voicelog") || channeltype.equals("cmdlog")) {
                    databaseHandler.database("serversettings", "update channels set " + channeltype + " = '" + channel + "' where id = '" + event.getGuild().getId() + "'");
                } else {
                    event.getChannel().sendMessage("Wrong channeltype!").queue();
                }

                break;
        }
    }


}
