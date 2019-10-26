package commands;


import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

public class cmdBotinfo implements Command {
    private String out = "\nThis bot is running on following servers: \n";
    private long members;

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String status = null;

        // checking for activation
        try {
            status = modulesChecker.moduleStatus("botinfo", event.getGuild().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert status != null;
        if (status.equals("activated_" + event.getGuild().getId())) {
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
            event.getTextChannel().sendMessage(embed.build()).queue();

        } else {
            messageActions.moduleIsDeactivated(event, "botinfo");
        }


    }


}
