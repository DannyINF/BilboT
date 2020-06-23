package commands;


import core.messageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.STATIC;

public class cmdBotinfo implements Command {
    private String out = "\nThis bot is running on following servers: \n";
    private long members;

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {



            int i = 0;

            // getting all guild + their members
            while (i < event.getJDA().getGuilds().size()) {
                members += event.getJDA().getGuilds().get(i).getMembers().size();
                out += event.getJDA().getGuilds().get(i).getName() + " (" + event.getJDA().getGuilds().get(i).getId() + ")  " +
                        "Nutzeranzahl: " + event.getJDA().getGuilds().get(i).getMembers().size() + "\n";

                i++;
            }

            // sending everything as msg
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("INFORMATION");
            embed.setDescription("\n\nOwner: MoorhuhnHD\nVersion: " + STATIC.VERSION + out + "Insgesamte Nutzeranzahl: " + members);
            event.getChannel().sendMessage(embed.build()).queue();



    }


}
