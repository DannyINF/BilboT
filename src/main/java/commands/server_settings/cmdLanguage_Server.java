package commands.server_settings;

import commands.Command;
import core.databaseHandler;
import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class cmdLanguage_Server implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws SQLException {
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            String language = null;
            try {
                language = args[1].toLowerCase();
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            if (language == null) {
                event.getTextChannel().sendMessage(messageActions.getLocalizedString("lang_syntax_server", "server", event.getGuild().getId())).queue();
            } else {
                if (language.equals("de_de") || language.equals("de_bay") || language.equals("de_swg") ||
                        language.equals("de_msf") || language.equals("en_gb") || language.equals("de_sac")) {
                    String[] argsLang = {"msgs", "id = '" + event.getGuild().getId() + "'",
                            "language", "'" + language.split("_")[0] + "'", "country", "'" + language.split("_")[1].toUpperCase() + "'"};
                    databaseHandler.database("serversettings", "update", argsLang);
                    embed.setDescription(messageActions.getLocalizedString("lang_set_server", "server", event.getGuild().getId()));
                    event.getTextChannel().sendMessage(embed.build()).queue();
                    Objects.requireNonNull(event.getGuild().getMemberById(event.getJDA().getSelfUser().getId())).modifyNickname(
                            messageActions.getLocalizedString("bilbot_name", "server", event.getGuild().getId())).queue();
                } else {
                    event.getTextChannel().sendMessage(messageActions.getLocalizedString("lang_syntax_server", "server", event.getGuild().getId())).queue();
                }
            }
        } else {
            permissionChecker.noPower(event.getTextChannel(), Objects.requireNonNull(event.getMember()));
        }
    }


}
