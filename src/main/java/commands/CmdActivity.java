package commands;

import core.DatabaseHandler;
import core.PermissionChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class CmdActivity {

    public static void activity(SlashCommandEvent event, Member member) throws Exception {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {

            int activity;
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

            assert member != null;
            String[] data = DatabaseHandler.database(event.getGuild().getId(), "select activity from users where id = '" + member.getId() + "'");
            activity = Integer.parseInt(Objects.requireNonNull(data)[0]);

            event.reply("**" + member.getUser().getAsTag() + "** besitzt eine Aktivit\u00e4t von **" + numberFormat.format(activity) + "**.").queue();
        } else {
            PermissionChecker.noPower(event);
        }
    }
}
