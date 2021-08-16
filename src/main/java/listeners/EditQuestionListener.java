package listeners;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.STATIC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EditQuestionListener extends ListenerAdapter {
    //TODO: comment everything
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String[] answer = null;
        try {
            answer = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where status < 28 and edit_id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[15].length() > 0 && !event.getAuthor().isBot()) {
            switch (answer[15]) {
                case "15":
                    try {
                        DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set question = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where id = " + answer[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Die Frage lautet nun: `" + event.getMessage().getContentRaw().replace("'", "\"").replace("'", "\"") + "`").queue();
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
                        DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set answer" + (Integer.parseInt(answer[15]) - 15) + " = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where id = " + answer[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Die " + (Integer.parseInt(answer[15]) - 15) + ". Antwort lautet nun: `" + event.getMessage().getContentRaw().replace("'", "\"") + "`").queue();
                    break;
            }
        }
    }

    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        String[] answer = null;
        try {
            answer = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where status < 28 and edit_id = '" + event.getUserId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[15].length() > 0) {
            switch (answer[15]) {
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
                                DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = 15 where id = " + answer[0]);
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
                                DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = " + answer_number + " where id = " + answer[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Die " + (answer_number - 15) + ". Antwort lautet bisher `" + answer[answer_number-14] + "`. Gib die neue " + (answer_number - 15) + ". Antwort an oder l\u00f6sche diese mit \u26D4!").queue(msg -> msg.addReaction("\u26D4").queue());
                            break;
                        case "\uD83D\uDD1F":
                            try {
                                DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = 25 where id = " + answer[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Die 10. Antwort lautet bisher `" + answer[11] + "`. Gib die neue 10. Antwort an oder l\u00f6sche diese mit \u26D4!").queue(msg -> msg.addReaction("\u26D4").queue());
                            break;
                        case "\u26D4":
                            int answer_count = 11;
                            ArrayList<String> list = new ArrayList<>(Arrays.asList(answer).subList(2, answer.length - 8));
                            for (String str : list)
                                if (str.length() == 0)
                                    answer_count--;
                            if (answer_count - 1 > 1) {
                                try {
                                    DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set answer" + (Integer.parseInt(answer[15]) - 15) + " = '' where id = " + answer[0]);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    answer = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where id = " + answer[0]);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                assert answer != null;
                                if (answer_count - 1 > Integer.parseInt(answer[15]) - 15) {
                                    for (int i = 1; i < answer_count; i++) {
                                        System.out.println(i);
                                        assert answer != null;
                                        if (answer[i + 1].length() == 0) {
                                            for (int j = i + 1; j < 11; j++) {
                                                System.out.println(j);
                                                if (answer[j + 1].length() != 0) {
                                                    System.out.println(answer[j+1]);
                                                    try {
                                                        DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set answer" + i + " = answer" + j + ", answer" + j + " = '' where id = " + answer[0]);
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        answer = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where id = " + answer[0]);
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                event.getChannel().sendMessage(">>> Die Antwort wurde gel\u00f6scht.").queue();
                            } else {
                                event.getChannel().sendMessage(">>> Die Antwort kann nicht gel\u00f6scht werden. Es kann nicht weniger als eine Antwort geben.").queue();
                            }
                            break;
                        case "\uD83D\uDD22":
                            answer_count = 11;
                            list = new ArrayList<>(Arrays.asList(answer).subList(2, answer.length - 8));
                            for (String str : list)
                                if (str.length() == 0)
                                    answer_count--;
                            int finalAnswer_count = answer_count;
                            event.getChannel().sendMessage(">>> Gib bitte an, wie viele Antworten gegeben werden m\u00fcssen. Bisher waren es **" + answer[12] + "**. (Beispiel: Wenn du m\u00f6chtest, dass 3 der 9 Ringgef\u00e4hrten genannt werden, dann klicke bitte die \u0033 unter dieser Nachricht an.)").queue(msg -> {
                                for (int i = 1; i < finalAnswer_count; i++) {
                                    if (i == 10) {
                                        msg.addReaction("\uD83D\uDD1F").queue();
                                        break;
                                    }
                                    msg.addReaction(i + "\u20E3").queue();
                                }
                            });
                            try {
                                DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = 26 where id = " + answer[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "\uD83C\uDDE6":
                            answer_count = 11;
                            list = new ArrayList<>(Arrays.asList(answer).subList(2, answer.length - 8));
                            for (String str : list)
                                if (str.length() == 0)
                                    answer_count--;
                            event.getChannel().sendMessage(">>> Gib die " + (answer_count) + ". Antwort an.").queue();
                            try {
                                DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = " + (answer_count + 15) + " where id = " + answer[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "\u2705":
                            try {
                                DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = 27 where id = " + answer[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Klicke auf \u2705, um die editierte Frage einzusenden!").queue(msg -> msg.addReaction("\u2705").queue());
                            break;
                    }
                    break;
                case "26":
                    int threshold = 0;
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
                        case "\u2705":
                            try {
                                DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = 27 where id = " + answer[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(">>> Klicke auf \u2705, um die editierte Frage einzusenden!").queue(msg -> msg.addReaction("\u2705").queue());
                            break;
                    }
                    if (threshold == 0)
                        break;
                    event.getChannel().sendMessage(">>> Jetzt m\u00fcssen **" + threshold + "** Antworten angegeben werden.").queue();
                    try {
                        DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = 14, threshhold = " + threshold + " where id = " + answer[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "27":
                    //einsenden
                    String[] question = null;
                    try {
                        question = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where id = " + answer[0]);
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
                    String[] finalAnswer = answer;
                    Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(STATIC.GUILD_ID)).getTextChannelById(STATIC.QUESTION_CHANNEL)).sendMessage(expert.build()).queue(msg -> {
                        msg.addReaction("\u2705").queue();
                        msg.addReaction("\u26D4").queue();
                        msg.addReaction("\u270F").queue();
                        try {
                            DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = " + msg.getId() + " where id = " + finalAnswer[0]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    expert.setTitle("Deine Frage wurde editiert!");
                    expert.setDescription("Deine Frage wurde editiert und erneut eingesendet.");
                    Objects.requireNonNull(event.getJDA().getUserById(answer[16])).openPrivateChannel().queue(channel -> channel.sendMessage(expert.build()).queue());

                    event.getChannel().sendMessage(">>> Die editierte Frage wurde eingesendet. Vielen Dank!").queue();
                    break;
            }
        }
    }
}
