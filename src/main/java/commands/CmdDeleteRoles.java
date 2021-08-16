package commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CmdDeleteRoles {

    public static void deleteroles(SlashCommandEvent event) {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else


        System.out.println(event.getGuild().getRolesByName("Lyrikabend - Verwalter", true).get(0).getPosition());

        String[] loreraenge = {
                "Maia", "Calaquende", "Moriquende", "Peredhel", "Dúnadan", "Adan", "Hobbit", "Drúadan"
        };
        for (String str : loreraenge) {
            try {
                event.getGuild().getRolesByName(str, true).get(0).getPosition();
                event.getGuild().getRolesByName(str, true).get(0).delete().queue();
            } catch (Exception ignored) {}

        }

        // deletes every role with the term "Level" in it
        int i = 0;
        while (i<event.getGuild().getRoles().size()) {
            if (event.getGuild().getRoles().get(i).getName().contains("Level")) {
                System.out.println(event.getGuild().getRoles().get(i).getPosition());
                event.getGuild().getRoles().get(i).delete().queue();
            }
            i++;
        }
    }
}
