package commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.DriverManager;

public class CmdShutdown {

    public static void shutdown(SlashCommandEvent event) {
        event.reply("Shutting down...").setEphemeral(true).queue(); // Let the user know we received the command before doing anything else

        if (event.getUser().getId().equals("277746420281507841")) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (Exception e) {
                event.getJDA().shutdown();
            }
        }
    }
}
