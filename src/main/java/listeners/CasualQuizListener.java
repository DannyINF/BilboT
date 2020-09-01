package listeners;

import core.DatabaseHandler;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;
import util.QuizActions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CasualQuizListener extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String[] answer = null;
        try {
            answer = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizcasual where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() > 0 && !event.getAuthor().isBot()) {
            if ("0".equals(answer[2])) {
                if (QuizActions.simplifyString(event.getMessage().getContentRaw()).equals("start")) {
                    String[] question = null;
                    try {
                        question = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where status = 111 order by random() OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    assert question != null;
                    try {
                        DatabaseHandler.database(STATIC.GUILD_ID, "update quizcasual set question_id = " + question[0] + ", past_questions = '" + question[0] + "' where id = '" + event.getAuthor().getId() + "'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //insg. Zeichen * 3 Sekunde * antworten / threshhold + Zeichen der Frage / 20
                    long timeInMillies;
                    long chars = 0L;
                    int threshold = Integer.parseInt(question[12]);
                    int answers = 0;
                    ArrayList<ArrayList<String>> answerlists = new ArrayList<>();
                    for (int i = 1; i < 11; i++) {
                        if (question[i+1].length() > 0) {
                            answerlists.add(new ArrayList<>(Arrays.asList(question[i + 1].split(", "))));
                            answers++;
                        }
                    }
                    for (ArrayList<String> answerlist : answerlists) {
                        long answerchars = 0L;
                        int answeroptions = 0;
                        for (String answeroption : answerlist) {
                            answerchars += answeroption.length();
                            answeroptions++;
                        }
                        chars += answerchars / answeroptions;
                    }
                    timeInMillies = chars / answers * threshold * 3000 + question[1].length()*1000 / 20;
                    event.getChannel().sendMessage(">>> **#" + question[0] + "** " + question[1]).queue(
                            (success) -> STATIC.restartQuizStopwatch(event, timeInMillies)
                    );
                }
            } else {
                long timeInMillis = STATIC.getQuizStopwatch(event);
                String id = answer[2];
                String[] question = null;
                try {
                    question = DatabaseHandler.database(STATIC.GUILD_ID, "select threshhold, answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9, answer10 from quizquestions where id = " + id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                assert question != null;
                int threshold = Integer.parseInt(question[0]);
                int correct = 0;
                //split answer in list by ,
                //if list.lenght != threshold -> failure
                //loop through all answers, eliminate nulls
                //for String answer : useranswerlist -> compare to every answer split by ,
                //if correct -> correct++; remove answer
                //if correct == threshold -> win; else -> failure
                String[] useranswers = QuizActions.simplifyString(event.getMessage().getContentRaw()).split(", ");
                if (useranswers.length != threshold) {
                    failure(event);
                    return;
                }
                ArrayList<ArrayList<String>> answerlists = new ArrayList<>();
                for (int i = 1; i < 11; i++) {
                    if (question[i].length() > 0)
                        answerlists.add(new ArrayList<>(Arrays.asList(question[i].split(", "))));
                }
                boolean isAnswered;
                for (String useranswer : useranswers) {
                    isAnswered = false;
                    for (ArrayList<String> answerlist : answerlists) {
                        for (String answeroption : answerlist) {
                            if (QuizActions.simplifyString(answeroption).equals(useranswer)) {
                                correct++;
                                answerlists.remove(answerlist);
                                isAnswered = true;
                                break;
                            }
                        }
                        if (isAnswered)
                            break;
                    }
                }
                if (correct == threshold) {
                    if ((event.getMessage().getContentRaw().length()/9*1000+1000) > timeInMillis) {
                        event.getChannel().sendMessage(">>> Es wurde ein Betrugsversuch festgestellt. Dieser Lauf wird abgebrochen.").queue();
                        Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(STATIC.GUILD_ID)).getTextChannelById(STATIC.QUESTION_CHANNEL)).sendMessage(">>> Ein Betrugsversuch wurde beim Nutzer **" +
                        event.getAuthor().getAsTag() + "** festgestellt!").queue();
                        failure(event);
                    } else {
                        event.getChannel().sendMessage(">>> Korrekt! Du hast `" + timeInMillis + "` Millisekunden ben\u00f6tigt.").queue();
                        win(event);
                    }
                } else
                    failure(event);
            }
        }
    }

    public static void failure(PrivateMessageReceivedEvent event) {
        String[] casual = null;
        try {
            casual = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizcasual where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException ignored) { }
        assert casual != null;
        try {
            DatabaseHandler.database(STATIC.GUILD_ID, "delete from quizcasual where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException ignored) { }
        String[] question = null;
        try {
            question = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where id = " + casual[2]);
        } catch (SQLException ignored) { }
        assert question != null;
        try {
            DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set incorrect = incorrect + 1 where id = " + question[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DatabaseHandler.database(STATIC.GUILD_ID, "update quizusers set casual_false = casual_false + 1, casual_games = casual_games + 1 where id = '" + event.getAuthor().getId() + "' and season = " + STATIC.SEASON);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] streak = null;
        try {
            streak = DatabaseHandler.database(STATIC.GUILD_ID, "select casual_streak from quizusers where id = '" + event.getAuthor().getId() + "' and season = " + STATIC.SEASON);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert streak != null;
        if (Long.parseLong(casual[1]) > Long.parseLong(streak[0])) {
            try {
                DatabaseHandler.database(STATIC.GUILD_ID, "update quizusers set casual_streak = " + casual[1] + " where id = '" + event.getAuthor().getId() + "' and season = " + STATIC.SEASON);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (Long.parseLong(casual[1]) > 10)
                Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(STATIC.GUILD_ID)).getTextChannelById("461158065899569174")).sendMessage(">>> **" + event.getAuthor().getAsTag() + "** " +
                    "hat im Casual-Quiz einen neuen pers\u00f6nlichen Rekord von `" + casual[1] + "` Fragen aufgestellt. Gratulation!").queue();
        }

        StringBuilder answer = new StringBuilder();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(question).subList(2, question.length - 8));
        for (int i = 0; i < 10; i++) {
            if (list.get(i).length() != 0)
                answer.append("**").append(i + 1).append(". Antwort:** ").append(list.get(i)).append("\n");
        }
        event.getChannel().sendMessage(">>> Falsche Antwort! Richtig w\u00e4re gewesen:\n" + answer.toString()).queue(msg -> msg.delete().queueAfter(30, TimeUnit.SECONDS));
        event.getChannel().sendMessage(">>> Du hast verloren! Streak: `" + casual[1] + "`").queue(msg -> msg.addReaction("\uD83C\uDDE8").queue());
        //TODO: save user stats
    }

    private static void win(PrivateMessageReceivedEvent event) {
        String[] casual = null;
        try {
            casual = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizcasual where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert casual != null;
        StringBuilder sb = new StringBuilder();
        for (String id : casual[4].split("#")) {
            sb.append(" and not id = ").append(id);
        }
        String[] question = null;
        try {
            question = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where status = 111" + sb.toString() + " order by random() OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert question != null;
        try {
            DatabaseHandler.database(STATIC.GUILD_ID, "update quizcasual set streak = streak + 1, question_id = " + question[0] + ", past_questions = '" + casual[4] + "#" + question[0] + "' where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set correct = correct + 1 where id = " + question[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DatabaseHandler.database(STATIC.GUILD_ID, "update quizusers set casual_correct = casual_correct + 1 where id = '" + event.getAuthor().getId() + "' and season = " + STATIC.SEASON);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long timeInMillies;
        long chars = 0L;
        int answers = 0;
        int threshold = Integer.parseInt(question[12]);
        ArrayList<ArrayList<String>> answerlists = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            if (question[i+1].length() > 0) {
                answerlists.add(new ArrayList<>(Arrays.asList(question[i + 1].split(", "))));
                answers++;
            }
        }
        for (ArrayList<String> answerlist : answerlists) {
            long answerchars = 0L;
            int answeroptions = 0;
            for (String answeroption : answerlist) {
                answerchars += answeroption.length();
                answeroptions++;
            }
            chars += answerchars / answeroptions;
        }
        timeInMillies = chars / answers * threshold * 3000 + question[1].length()*1000 / 20;
        event.getChannel().sendMessage(">>> **#" + question[0] + "** " + question[1]).queue(
                (success) -> STATIC.restartQuizStopwatch(event, timeInMillies)
        );
    }

    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(msg -> msg.retrieveReactionUsers(event.getReactionEmote().getEmoji()).queue(users -> {
            if (users.size() > 1) {
                if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDE8") && !event.getUser().isBot()) {
                    try {
                        DatabaseHandler.database(STATIC.GUILD_ID, "delete from reports where victim_id = '" + event.getUserId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4' or report_id = '5')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        DatabaseHandler.database(STATIC.GUILD_ID, "delete from quizquestions where author_id = '" + event.getUserId() + "' and status < 14");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        DatabaseHandler.database(STATIC.GUILD_ID, "insert into quizcasual (id, streak, question_id, revived, past_questions) values " +
                                "('" + event.getUserId() + "', 0, 0, FALSE, '')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendMessage(">>> Wenn du bereit bist zu starten, schreibe `START`!").queue();
                }
            }
        }));
    }
}
