package commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import util.STATIC;

public class Cmd2x {

    public static void TwoX(SlashCommandEvent event) {
        if (event.getMember().getId().equals("277746420281507841")) {
            event.reply(">>> Das **Double-XP-Event** wurde " + (STATIC.toggle2x() ? "gestartet." : "beendet.")).queue();
        }
    }
}
