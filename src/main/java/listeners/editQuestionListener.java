package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class editQuestionListener extends ListenerAdapter {
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database("388969412889411585", "select status, id from quizquestions where status < 28 and edit_id = '" + event.getAuthor() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() > 0) {
            switch (answer[0]) {
                case "15":
                    try {
                        core.databaseHandler.database("388969412889411585", "update quizquestions set question = '" + event.getMessage().getContentRaw() + "' where edit_id = '" + event.getAuthor().getId() + "' and status < 28");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Die Frage lautet nun: `" + event.getMessage().getContentRaw() + "`").queue();
                    break;
                case "16":
                case "17":
                case "18":
                case "19":
                case "20":
                case "21":
                case "22":
                case "23":
                case "24":
                case "25":
                    try {
                        core.databaseHandler.database("388969412889411585", "update quizquestions set answer" + (Integer.parseInt(answer[0]) - 15) + " = '" + event.getMessage().getContentRaw() + "' where edit_id = '" + event.getAuthor().getId() + "' and status < 28");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Die " + (Integer.parseInt(answer[0]) - 15) + ". Antwort lautet nun: `" + event.getMessage().getContentRaw() + "`").queue();
                    break;
            }
        }
    }

    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database("388969412889411585", "select status, id from quizquestions where status < 28 and edit_id = '" + event.getUserId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() > 0) {
            switch (answer[0]) {
                case "14":
                case "15":
                case "16":
                case "17":
                case "18":
                case "19":
                case "20":
                case "21":
                case "22":
                case "23":
                case "24":
                case "25":
                    switch (event.getReactionEmote().getEmoji()) {
                        case "\uD83C\uDDF6":
                            try {
                                core.databaseHandler.database("388969412889411585", "update quizquestions set status = 15 where edit_id = '" + event.getUserId() + "' and status < 28");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Die Frage lautet bisher `" + answer[1] + "`. Gib die neue Frage an!").queue();
                            break;
                        case "1\u20E3":
                        case "2\u20E3":
                        case "3\u20E3":
                        case "4\u20E3":
                        case "5\u20E3":
                        case "6\u20E3":
                        case "7\u20E3":
                        case "8\u20E3":
                        case "9\u20E3":
                            int answer_number = Integer.parseInt(event.getReactionEmote().getEmoji().replace("\u20E3", "")) + 15;
                            try {
                                core.databaseHandler.database("388969412889411585", "update quizquestions set status = " + answer_number + " where edit_id = '" + event.getUserId() + "' and status < 28");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Die " + (answer_number - 15) + ". Antwort lautet bisher `" + answer[answer_number-14] + "`. Gib die neue " + (answer_number - 15) + ". Antwort an!").queue();
                            break;
                        case "\uD83D\uDD1F":
                            try {
                                core.databaseHandler.database("388969412889411585", "update quizquestions set status = 25 where edit_id = '" + event.getUserId() + "' and status < 28");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Die 10. Antwort lautet bisher `" + answer[11] + "`. Gib die neue 10. Antwort an!").queue();
                            break;
                        case "\uD83D\uDD22":
                            int answer_count = 11;
                            ArrayList<String> list = new ArrayList<>(Arrays.asList(answer).subList(2, answer.length - 8));
                            for (String str : list)
                                if (str.length() == 0)
                                    answer_count--;
                            int finalAnswer_count = answer_count;
                            event.getChannel().sendMessage(">>> Gib bitte an, wie viele Antworten gegeben werden m\u00fcssen. Bisher waren es **" + answer[12] + "**. (Wenn du m\u00f6chtest, dass 3 der 9 Ringgef\u00e4hrten genannt werden, dann klicke bitte die \u0033 unter dieser Nachricht an.)").queue(msg -> {
                                for (int i = 1; i < finalAnswer_count; i++) {
                                    if (i == 10) {
                                        msg.addReaction("\uD83D\uDD1F").queue();
                                        break;
                                    }
                                    msg.addReaction(i + "\u20E3").queue();
                                }
                            });
                            try {
                                core.databaseHandler.database("388969412889411585", "update quizquestions set status = 26 where edit_id = '" + event.getUserId() + "' and status < 28");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "\u2705":
                            try {
                                core.databaseHandler.database("388969412889411585", "update quizquestions set status = 27 where edit_id = '" + event.getUserId() + "' and status < 28");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Klicke auf \u2705, um die editierte Frage einzusenden!").queue(msg -> msg.addReaction("\u2705").queue());
                            break;
                    }
                    break;
                case "26":
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
                        core.databaseHandler.database("388969412889411585", "update quizquestions set threshhold = " + threshold + " where edit_id = '" + event.getUserId() + "' and status < 28");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "27":
                    //einsenden
                    String[] question = null;
                    try {
                        question = core.databaseHandler.database("388969412889411585", "select * from quizquestions where edit_id = '" + event.getUserId() + "' and status < 28");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    assert question != null;
                    EmbedBuilder expert = new EmbedBuilder();
                    expert.setTitle("Frage von " + Objects.requireNonNull(event.getJDA().getUserById(question[16])).getAsTag() + " (editiert von " + Objects.requireNonNull(event.getJDA().getUserById(question[19])).getAsTag() + ") kontrollieren!");
                    expert.setDescription("Bitte kontrolliert folgende Frage und ihre Antworten und reagiert mit den entsprechenden Emotes.\n" +
                            "Insgesamt m\u00fcssen bei dieser Frage " + question[12] + " Antworten gegeben werden.");
                    expert.addField("Frage", question[1], false);
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(question).subList(2, question.length - 8));
                    for (int i = 0; i < 10; i++) {
                        if (list.get(i).length() != 0)
                            expert.addField((i + 1) + ". Antwort", list.get(i), false);
                    }
                    Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(388969412889411585L)).getTextChannelById(588823110250266641L)).sendMessage(expert.build()).queue(msg -> {
                        msg.addReaction("\u2705").queue();
                        msg.addReaction("\u26D4").queue();
                        msg.addReaction("\u270F").queue();
                        try {
                            core.databaseHandler.database("388969412889411585", "update quizquestions set status = " + msg.getId() + " where edit_id = '" + event.getUserId() + "' and status < 28");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    event.getChannel().sendMessage(">>> Die editierte Frage wurde eingesendet. Vielen Dank!").queue();
                    break;

            }
        }
    }
}
