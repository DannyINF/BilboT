package commands;

import core.permissionChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class cmdActivity implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            Member member = util.getUser.getMemberFromInput(args, event.getAuthor(), event.getGuild(), event.getChannel());
            int activity = 0;
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

            String[] data = core.databaseHandler.database(event.getGuild().getId(), "select activity from users where id = '" + member.getId() + "'");
            activity = Integer.parseInt(data[0]);

            event.getChannel().sendMessage("**" + member.getUser().getAsTag() + "** besitzt eine Aktivit\u00e4t von **" + numberFormat.format(activity) + "**.").queue();
        } else {
            core.permissionChecker.noPower(event.getChannel(), event.getMember());
        }
    }
}
