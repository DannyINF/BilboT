package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class newQuestionListener extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database("388969412889411585", "select status, id from quizquestions where author_id = '" + event.getAuthor().getId() + "' and status < 14");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() > 0) {
            switch (answer[0]) {
                case "1":
                    try {
                        core.databaseHandler.database("388969412889411585", "update quizquestions set status = 2, question = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where author_id = '" + event.getAuthor().getId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Nun geht es um die Antworten. Achte auch hier auf die korrekte Schreibweise.\n" +
                            "Insgesamt hast du 10 m\u00f6gliche Antworten zur Verf\u00fcgung. Wenn du verschiedene Schreibweisen zulassen m\u00f6chtest, kannst du alle Varianten" +
                            " durch Kommata abgetrennt in EINE Antwort schreiben. (Z.B. \"Gandalf, Olorin, Mithrandir\")\n" +
                            "Gro\u00df- und Kleinschreibung, sowie Akzentzeichen k\u00f6nnen vernachl\u00e4ssigt werden, jedoch vereinfacht ersteres das Pr\u00fcfen der Frage.").queue();
                    event.getChannel().sendMessage(">>> \nGib bitte deine erste Antwort an:").queue();
                    break;
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "10":
                    try {
                        core.databaseHandler.database("388969412889411585", "update quizquestions set status = " + (Integer.parseInt(answer[0]) + 1) + ", answer" + (Integer.parseInt(answer[0]) - 1) + " = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where author_id = '" + event.getAuthor().getId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Gib bitte deine " + answer[0] + ". Antwort an. (Wenn du keine " + answer[0] + ". Antwort hinzuf\u00fcgen m\u00f6chtest, kannst du auf \u2705 unter dieser Nachricht klicken.)").queue(msg -> msg.addReaction("\u2705").queue());
                    break;
                case "11":
                    try {
                        core.databaseHandler.database("388969412889411585", "update quizquestions set status = 12, answer10 = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where author_id = '" + event.getAuthor().getId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Gib bitte an, wie viele Antworten gegeben werden m\u00fcssen. (Beispiel: Wenn du m\u00f6chtest, dass 3 der 9 Ringgef\u00e4hrten genannt werden, dann klicke bitte die \u0033 unter dieser Nachricht an.)").queue(msg -> {
                        msg.addReaction("1\u20E3").queue();
                        msg.addReaction("2\u20E3").queue();
                        msg.addReaction("3\u20E3").queue();
                        msg.addReaction("4\u20E3").queue();
                        msg.addReaction("5\u20E3").queue();
                        msg.addReaction("6\u20E3").queue();
                        msg.addReaction("7\u20E3").queue();
                        msg.addReaction("8\u20E3").queue();
                        msg.addReaction("9\u20E3").queue();
                        msg.addReaction("\uD83D\uDD1F").queue();
                    });
                    break;
            }
        }
    }



    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database("388969412889411585", "select status from quizquestions where author_id = '" + event.getUserId() + "' and status < 14");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() > 0) {
            switch (answer[0]) {
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "10":
                case "11":
                    int answer_count = Integer.parseInt(answer[0]) - 1;
                    try {
                        core.databaseHandler.database("388969412889411585", "update quizquestions set status = 12 where author_id = '" + event.getUserId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Gib bitte an, wie viele Antworten gegeben werden m\u00fcssen. (Wenn du m\u00f6chtest, dass 3 der 9 Ringgef\u00e4hrten genannt werden, dann klicke bitte die \u0033 unter dieser Nachricht an.)").queue(msg -> {
                        for (int i = 1; i < answer_count; i++) {
                            if (i == 10) {
                                msg.addReaction("\uD83D\uDD1F").queue();
                                break;
                            }
                            msg.addReaction(i + "\u20E3").queue();
                        }
                    });
                    break;
                case "12":
                    int threshold = 1;
                    switch (event.getReactionEmote().getEmoji()) {
                        case "1\u20E3":
                            threshold = 1;
                            break;
                        case "2\u20E3":
                            threshold = 2;
                            break;
                        case "3\u20E3":
                            threshold = 3;
                            break;
                        case "4\u20E3":
                            threshold = 4;
                            break;
                        case "5\u20E3":
                            threshold = 5;
                            break;
                        case "6\u20E3":
                            threshold = 6;
                            break;
                        case "7\u20E3":
                            threshold = 7;
                            break;
                        case "8\u20E3":
                            threshold = 8;
                            break;
                        case "9\u20E3":
                            threshold = 9;
                            break;
                        case "\uD83D\uDD1F":
                            threshold = 10;
                            break;
                    }
                    try {
                        core.databaseHandler.database("388969412889411585", "update quizquestions set status = 13, threshhold = " + threshold + " where author_id = '" + event.getUserId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String[] question = null;
                    try {
                        question = core.databaseHandler.database("388969412889411585", "select * from quizquestions where author_id = '" + event.getUserId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    assert question != null;
                    EmbedBuilder expert = new EmbedBuilder();
                    expert.setTitle("\u00dcbersicht");
                    expert.setDescription("Das ist deine Frage bisher. Es m\u00fcssen **" + threshold + " Antworten** angegeben werden.");
                    expert.addField("Frage", question[1], false);
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(question).subList(2, question.length - 8));
                    for (int i = 0; i < 10; i++) {
                        if (list.get(i).length() != 0)
                            expert.addField((i + 1) + ". Antwort", list.get(i), false);
                    }
                    event.getChannel().sendMessage(expert.build()).queue();
                    event.getChannel().sendMessage(">>> Um die Frage einzusenden, musst du nur noch auf \u2705 unter dieser Nachricht klicken!").queue(msg -> msg.addReaction("\u2705").queue());
                    break;
                case "13":
                    //einsenden
                    question = null;
                    try {
                        question = core.databaseHandler.database("388969412889411585", "select * from quizquestions where author_id = '" + event.getUserId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    assert question != null;
                    expert = new EmbedBuilder();
                    expert.setTitle("Frage #" + answer[0] + " von " + Objects.requireNonNull(event.getJDA().getUserById(question[16])).getAsTag() + " kontrollieren!");
                    expert.setDescription("Bitte kontrolliert folgende Frage und ihre Antworten und reagiert mit den entsprechenden Emotes.\n" +
                            "Insgesamt m\u00fcssen bei dieser Frage **" + question[12] + " Antworten** gegeben werden.");
                    expert.addField("Frage", question[1], false);
                    list = new ArrayList<>(Arrays.asList(question).subList(2, question.length - 8));
                    for (int i = 0; i < 10; i++) {
                        if (list.get(i).length() != 0)
                            expert.addField((i + 1) + ". Antwort", list.get(i), false);
                    }
                    Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(388969412889411585L)).getTextChannelById(588823110250266641L)).sendMessage(expert.build()).queue(msg -> {
                        msg.addReaction("\u2705").queue();
                        msg.addReaction("\u26D4").queue();
                        msg.addReaction("\u270F").queue();
                        msg.addReaction("\u21A9").queue();
                        msg.addReaction("\uD83C\uDFAD").queue();
                        try {
                            core.databaseHandler.database("388969412889411585", "update quizquestions set status = " + msg.getId() + " where author_id = '" + event.getUserId() + "' and status < 14");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    event.getChannel().sendMessage(">>> Deine Frage wurde eingesendet. Vielen Dank!").queue();
                    break;

            }
        }
    }
}
