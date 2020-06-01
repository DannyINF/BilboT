package commands.user_settings;

import commands.Command;
import core.databaseHandler;
import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;

public class cmdLanguage implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        String status;
        status = modulesChecker.moduleStatus("language", event.getGuild().getId());
        if (status.equals("activated")) {
            String language = null;
            try {
                language = args[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                event.getChannel().sendMessage(messageActions.getLocalizedString("lang_syntax_user", "user", event.getAuthor().getId())).queue();
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            if (language.isEmpty()) {
                event.getChannel().sendMessage(messageActions.getLocalizedString("lang_syntax_user", "user", event.getAuthor().getId())).queue();
            } else {
                if (language.equals("de_de") || language.equals("de_bay") || language.equals("de_swg") ||
                        language.equals("de_msf") || language.equals("en_gb") || language.equals("de_sac")) {
                    String[] argsLang = {"users", "id = '" + event.getAuthor().getId() + "'",
                            "language", "'" + language.split("_")[0] + "'", "country", "'" + language.split("_")[1].toUpperCase() + "'"};
                    databaseHandler.database("usersettings", "update", argsLang);
                    embed.setDescription(messageActions.getLocalizedString("lang_set_user", "user", event.getAuthor().getId())
                            .replace("[USER]", event.getAuthor().getAsMention()));
                    event.getChannel().sendMessage(embed.build()).queue();
                } else {
                    event.getChannel().sendMessage(messageActions.getLocalizedString("lang_syntax_user", "user", event.getAuthor().getId())).queue();
                }
            }

        } else {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Modules");
            embed.setColor(Color.RED);
            embed.setDescription("Das Modul \u00BBlanguage\u00AB ist deaktiviert!");
            event.getChannel().sendMessage(embed.build()).queue();
        }

    }


}
