package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdDeleteRoles implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        // deletes every role with the term "Level" in it
        int i = 0;
        while (i < event.getGuild().getRoles().size()) {
            if (event.getGuild().getRoles().get(i).getName().contains("Level")) {
                event.getGuild().getRoles().get(i).delete().queue();
            }
            i++;
        }
    }


}
