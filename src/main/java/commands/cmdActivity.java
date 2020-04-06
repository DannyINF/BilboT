package commands;

import core.permissionChecker;
import net.dv8tion.jda.api.Permission;
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
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            Member member = util.getUser.getMemberFromInput(args, event.getAuthor(), event.getGuild(), event.getTextChannel());
            int activity = 0;
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

            String[] arguments = {"users", "id = '" + member.getUser().getId() + "'", "1", "activity"};
            String[] data = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
            activity = Integer.parseInt(data[0]);

            event.getTextChannel().sendMessage("**" + member.getUser().getAsTag() + "** besitzt eine Aktivit\u00e4t von **" + numberFormat.format(activity) + "**.").queue();
        } else {
            core.permissionChecker.noPower(event.getTextChannel(), event.getMember());
        }
    }
}
