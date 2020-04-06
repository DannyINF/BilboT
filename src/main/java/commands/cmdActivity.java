package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class cmdActivity implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {
        Member member = util.getUser.getMemberFromInput(args, event.getAuthor(), event.getGuild(), event.getTextChannel());
        int activity = 0;
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        String[] arguments = {"users", "id = '" + member.getUser().getId() + "'", "1", "activity"};
        String[] data = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
        activity = Integer.parseInt(data[0]);

        event.getTextChannel().sendMessage("**" + member.getUser().getAsTag() + "** besitzt eine Aktivität von **" + numberFormat.format(activity) + "**.").queue();
    }
}
