package commands.server_settings;

import commands.Command;
import core.DatabaseHandler;
import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class CmdModules_Server implements Command {

    private static String[] modules(GuildMessageReceivedEvent event, String module, String status) throws SQLException {
        String modul = null;
        switch (module) {
            case "event":
                modul = "event";
                break;
            case "maps":
                modul = "maps";
                break;
            case "search":
                modul = "search";
                break;
            case "clear":
                modul = "clear";
                break;
            case "language":
                modul = "language";
                break;
            case "xp":
                modul = "xp";
                break;
            case "profile":
                modul = "profile";
                break;
            case "quotes":
                modul = "quotes";
                break;
            case "report":
                modul = "report";
                break;
            case "welcome":
                modul = "welcome";
                break;
            case "rules":
                modul = "rules";
                break;
            case "help":
                modul = "help";
                break;
            case "botinfo":
                modul = "botinfo";
                break;
            case "chatfilter":
                modul = "chatfilter";
                break;
            case "joining":
                modul = "joining";
                break;
            case "leaving":
                modul = "leaving";
                break;
            case "voice":
                modul = "voice";
                break;
            case "verification":
                modul = "verification";
                break;
        }
        DatabaseHandler.database("serversettings", "update modules set " + modul + " = " + status + " where id = '" + event.getGuild().getId() + "'");
        return new String[]{modul, status};
    }

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            String condition = null;
            String module = null;
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(MessageActions.getLocalizedString("modules_title", "server", event.getGuild().getId()));
            try {
                condition = args[1];
                module = args[2];
            } catch (Exception ignored) {

            }
            String status = null;
            boolean help = false;
            switch (condition) {
                case "activate":
                    embed.setColor(Color.GREEN);
                    status = "'activated'";
                    break;
                case "deactivate":
                    embed.setColor(Color.RED);
                    status = "'deactivated'";
                    break;
                default:
                    help = true;
                    embed.setColor(Color.YELLOW);
                    embed.setDescription(MessageActions.getLocalizedString("modules_help", "server", event.getGuild().getId()) +
                            "/help /server modules"
                    );
                    break;
            }
            if (!help) {
                String[] answer = modules(event, module, status);
                String status_str;
                if (answer[1].equals("activated")) {
                    status_str = MessageActions.getLocalizedString("modules_status_activated", "server", event.getGuild().getId());
                } else {
                    status_str = MessageActions.getLocalizedString("modules_status_deactivated", "server", event.getGuild().getId());
                }
                embed.setDescription(MessageActions.getLocalizedString("modules_status", "server", event.getGuild().getId()).replace("[MODUL]", answer[0])
                        .replace("[STATUS]", status_str));
            }
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } else {
            PermissionChecker.noPower(event);
        }

    }
}
