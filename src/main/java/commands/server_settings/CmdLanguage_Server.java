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

public class CmdLanguage_Server implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            String language = null;
            try {
                language = args[1].toLowerCase();
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            if (language == null) {
                event.getChannel().sendMessage(MessageActions.getLocalizedString("lang_syntax_server", "server", event.getGuild().getId())).queue();
            } else {
                if (language.equals("de_de") || language.equals("de_bay") || language.equals("de_swg") ||
                        language.equals("de_msf") || language.equals("en_gb") || language.equals("de_sac")) {
                    DatabaseHandler.database("serversettings", "update msgs set language = '" + language.split("_")[0] + "', " +
                            "country = '" + language.split("_")[1].toUpperCase() + "' where id = '" + event.getGuild().getId() + "'");
                    embed.setDescription(MessageActions.getLocalizedString("lang_set_server", "server", event.getGuild().getId()));
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    Objects.requireNonNull(event.getGuild().getMemberById(event.getJDA().getSelfUser().getId())).modifyNickname(
                            MessageActions.getLocalizedString("bilbot_name", "server", event.getGuild().getId())).queue();
                } else {
                    event.getChannel().sendMessage(MessageActions.getLocalizedString("lang_syntax_server", "server", event.getGuild().getId())).queue();
                }
            }
        } else {
            PermissionChecker.noPower(event);
        }
    }


}
