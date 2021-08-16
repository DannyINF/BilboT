package commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CmdTalk {

    public static void talk(SlashCommandEvent event, String query) {
        event.reply("Sending message: " + query).setEphemeral(true).queue(); // Let the user know we received the command before doing anything else
        if (event.getUser().getId().equals("277746420281507841")) {
            event.getChannel().sendMessage(query).queue();
        }
    }
}
