package commands;

import core.DatabaseHandler;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class CmdReport {

    public static void action(SlashCommandEvent event) throws SQLException {
        event.reply("Ich habe dir eine private Nachricht gesendet!").setEphemeral(true).queue(); // Let the user know we received the command before doing anything else

        //TODO: close reports that are open
        DatabaseHandler.database(event.getGuild().getId(), "delete from reports where victim_id = '" + event.getUser().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4' or report_id = '5')");
        DatabaseHandler.database(event.getGuild().getId(), "delete from quizquestions where author_id = '" + event.getUser().getId() + "' and status < 14");
        DatabaseHandler.database(event.getGuild().getId(), "insert into reports (report_id, victim_id, offender_id, channel, cause, info) " +
                "values ('1', '" + event.getUser().getId() + "', '', '', '', '')");
        event.getUser().openPrivateChannel().queue(channel -> {
            channel.sendMessage(">>> Hey **" + event.getUser().getAsTag() + "**,\n" +
                            "um einen Report zu erstellen, musst du mir noch einige Fragen beantworten:").queue();
            channel.sendMessage(">>> Wen m\u00f6chtest du reporten? Gib hier am besten alles so genau wie m\u00f6glich an, wie z.B. die ID oder den Namen mit Tag (Nutzer#1234).").queue();
        });
    }
}