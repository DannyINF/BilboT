package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdTalk implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals("277746420281507841")) {
            StringBuilder sb = new StringBuilder();
            for (String str : args) {
                sb.append(str);
                sb.append(" ");
            }
            event.getTextChannel().sendMessage(sb.toString()).queue();
        }
    }


}
