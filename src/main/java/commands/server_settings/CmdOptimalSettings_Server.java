package commands.server_settings;

import commands.Command;
import core.DatabaseHandler;
import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class CmdOptimalSettings_Server implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            //TODO: talk, quote, help
            String[] activation = {"search", "clear", "language", "xp", "report", "welcome", "rules", "botinfo", "chatfilter", "joining", "leaving", "voice", "maps", "intro", "addons"};
            String[] deactivation = {"verification", "event"};
            for (String module : activation) {
                DatabaseHandler.database("serversettings", "update modules set " + module + " = 'activated' where id = '" + event.getGuild().getId() + "'");
            }
            for (String module : deactivation) {
                DatabaseHandler.database("serversettings", "update modules set " + module + " = 'deactivated' where id = '" + event.getGuild().getId() + "'");
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            embed.setTitle("Optimal settings");
            embed.setDescription("Activated search, clear, language, xp, report, welcome, rules, botinfo, chatfilter, joining, leaving, voice, maps, intro, addons");
            event.getChannel().sendMessage(embed.build()).queue();
            EmbedBuilder embed1 = new EmbedBuilder();
            embed1.setColor(Color.RED);
            embed1.setTitle("Optimal settings");
            embed1.setDescription("Deactivated verification, event");
            event.getChannel().sendMessage(embed1.build()).queue();
        } else {
            PermissionChecker.noPower(event);
        }
    }
    //TODO: Überarbeitung
}