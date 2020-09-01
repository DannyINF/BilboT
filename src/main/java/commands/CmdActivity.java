package commands;

import core.DatabaseHandler;
import core.PermissionChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.GetUser;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class CmdActivity implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            Member member = GetUser.getMemberFromInput(args, event.getAuthor(), event.getGuild(), event.getChannel());
            int activity;
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

            assert member != null;
            String[] data = DatabaseHandler.database(event.getGuild().getId(), "select activity from users where id = '" + member.getId() + "'");
            activity = Integer.parseInt(Objects.requireNonNull(data)[0]);

            event.getChannel().sendMessage("**" + member.getUser().getAsTag() + "** besitzt eine Aktivit\u00e4t von **" + numberFormat.format(activity) + "**.").queue();
        } else {
            PermissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }
    }
}
