package commands;

import core.DatabaseHandler;
import core.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class CmdEdit {

    public static void edit(SlashCommandEvent event, String id, String query) throws SQLException {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else

        if (Objects.requireNonNull(event.getMember()).getUser().getId().equals("277746420281507841")) {
            System.out.println(Arrays.toString(DatabaseHandler.database(id, query)));
        }
    }
}
