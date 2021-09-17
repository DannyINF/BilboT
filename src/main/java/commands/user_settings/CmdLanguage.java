package commands.user_settings;

import commands.Command;
import core.DatabaseHandler;
import core.MessageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;

public class CmdLanguage implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {

            String language = null;
            try {
                language = args[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                event.getChannel().sendMessage(MessageActions.getLocalizedString("lang_syntax_user", "user", event.getAuthor().getId())).queue();
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            if (language.isEmpty()) {
                event.getChannel().sendMessage(MessageActions.getLocalizedString("lang_syntax_user", "user", event.getAuthor().getId())).queue();
            } else {
                if (language.equals("de_de") || language.equals("de_bay") || language.equals("de_swg") ||
                        language.equals("de_msf") || language.equals("en_gb") || language.equals("de_sac")) {
                    DatabaseHandler.database("usersettings", "update users set language = '" + language.split("_")[0] + "', " +
                            "country = '" + language.split("_")[1].toUpperCase() + "' where id = '" + event.getAuthor().getId() + "'");
                    embed.setDescription(MessageActions.getLocalizedString("lang_set_user", "user", event.getAuthor().getId())
                            .replace("[USER]", event.getAuthor().getAsMention()));
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                } else {
                    event.getChannel().sendMessage(MessageActions.getLocalizedString("lang_syntax_user", "user", event.getAuthor().getId())).queue();
                }
            }


    }


}
