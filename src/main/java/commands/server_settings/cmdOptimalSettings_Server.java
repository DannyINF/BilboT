package commands.server_settings;

import commands.Command;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;

public class cmdOptimalSettings_Server implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws SQLException {
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            //TODO: talk, quote, help
            String[] activation = {"search", "clear", "language", "xp", "report", "welcome", "rules", "botinfo", "chatfilter", "joining", "leaving", "voice", "maps", "intro", "addons"};
            String[] deactivation = {"verification", "event"};
            for (String module : activation) {
                String[] arguments = {"modules", "id = '" + event.getGuild().getId() + "'", module, "'activated'"};
                core.databaseHandler.database("serversettings", "update", arguments);
            }
            for (String module : deactivation) {
                String[] arguments = {"modules", "id = '" + event.getGuild().getId() + "'", module, "'deactivated'"};
                core.databaseHandler.database("serversettings", "update", arguments);
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            embed.setTitle("Optimal settings");
            embed.setDescription("Activated search, clear, language, xp, report, welcome, rules, botinfo, chatfilter, joining, leaving, voice, maps, intro, addons");
            event.getTextChannel().sendMessage(embed.build()).queue();
            EmbedBuilder embed1 = new EmbedBuilder();
            embed1.setColor(Color.RED);
            embed1.setTitle("Optimal settings");
            embed1.setDescription("Deactivated verification, event");
            event.getTextChannel().sendMessage(embed1.build()).queue();
        } else {
            permissionChecker.noPower(event.getTextChannel());
        }

    }

    //TODO: Überarbeitung


}
