package commands;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.derby.impl.sql.execute.CurrentDatetime;
import util.STATIC;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CmdQuiz {

    public static void quiz(SlashCommandEvent event) throws Exception {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else

        /*if (!(event.getUser().getId().equals("277746420281507841") || event.getUser().getId().equals("209272591532163073") || event.getUser().getId().equals("305256757398339585") || event.getUser().getId().equals("354354147614654465"))) {
            event.getChannel().sendMessage("Still beta, dude \uD83D\uDE0F").queue();
            return;
        }*/
        try {
            switch (event.getSubcommandName()) {
                case "casual":
                    DatabaseHandler.database(event.getGuild().getId(), "delete from reports where victim_id = '" + event.getUser().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4' or report_id = '5')");
                    DatabaseHandler.database(event.getGuild().getId(), "delete from quizquestions where author_id = '" + event.getUser().getId() + "' and status < 14");
                    DatabaseHandler.database(event.getGuild().getId(), "insert into quizcasual (id, streak, question_id, revived, past_questions) values " +
                            "('" + event.getUser().getId() + "', 0, 0, FALSE, '')");
                    String[] test = Objects.requireNonNull(DatabaseHandler.database(event.getGuild().getId(), "select season from quizusers where id = '" + event.getUser().getId() + "' and season = " + STATIC.SEASON));
                    if (test.length == 0)
                        DatabaseHandler.database(event.getGuild().getId(), "insert into quizusers (id, season, casual_correct, casual_false, ranked_correct, " +
                                "ranked_false, casual_streak, ranked_streak, ranked_building_streak, elo, peak, casual_games, ranked_games) values " +
                                "('" + event.getUser().getId() + "', " + STATIC.SEASON + ", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)");

                    event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(">>> Wenn du bereit bist zu starten, schreibe `START`!").queue());
                    break;
                case "ranked":
                    //start ranked match
                    break;
                case "stats":
                    //send stats
                    EmbedBuilder stats = new EmbedBuilder();
                    stats.setColor(Color.WHITE);
                    int season = -1;
                    try {
                        season = (int) event.getOption("quiz_stats_season").getAsLong();
                    } catch (Exception ignored) {}
                    Member member = event.getMember();
                    try {
                        member = event.getOption("quiz_stats_user").getAsMember();
                    } catch (Exception ignored) {}
                    assert member != null;
                    stats.setTitle("Quiz-Statistiken f\u00fcr " + member.getUser().getAsTag());
                    String seasonal = "";
                    if (season == -1)
                        stats.setFooter("Insgesamte Statistik", null);
                    else {
                        stats.setFooter("Season " + season, null);
                        seasonal = " and season = " + season;
                    }
                    stats.setTimestamp(new CurrentDatetime().getCurrentTimestamp().toLocalDateTime().atZone(ZoneId.of("Europe/Berlin")));
                    NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                    String[] stats_answer = DatabaseHandler.database(event.getGuild().getId(),
                            "select sum(casual_correct), sum(casual_false), sum(ranked_correct), sum(ranked_false), max(casual_streak), max(ranked_streak), " +
                                    "max(peak), sum(casual_games), sum(ranked_games) from quizusers where id = '" + member.getId() + "'" + seasonal);
                    assert stats_answer != null;
                    if (stats_answer.length == 0) {
                        event.getChannel().sendMessage(">>> " + event.getUser().getAsMention() + ", du musst mindestens ein Spiel insgesamt bzw. ein Spiel in dieser Season gespielt haben, damit deine Statistiken einsehbar sind.").queue();
                        return;
                    }
                    String statsall = "";
                    if (season == -1)
                        statsall = "Spiele (insgesamt): " + numberFormat.format(Integer.parseInt(stats_answer[7]) + Integer.parseInt(stats_answer[8])) +
                                "\nKorrekte Antworten (insgesamt): " + numberFormat.format(Integer.parseInt(stats_answer[0]) + Integer.parseInt(stats_answer[2])) +
                                "\nFalsche Antworten (insgesamt): " + numberFormat.format(Integer.parseInt(stats_answer[1]) + Integer.parseInt(stats_answer[3])) +
                                "\nFehlerquote (insgesamt): " + numberFormat.format(((Integer.parseInt(stats_answer[1]) + Integer.parseInt(stats_answer[3])) * 1000) / (
                                Integer.parseInt(stats_answer[0]) + Integer.parseInt(stats_answer[2]) + Integer.parseInt(stats_answer[1]) + Integer.parseInt(stats_answer[3])) / 10) + "%\n\n";
                    String statscasual = "**CASUAL**" +
                            "\nSpiele: " + numberFormat.format(Integer.parseInt(stats_answer[7])) +
                            "\nKorrekte Antworten: " + numberFormat.format(Integer.parseInt(stats_answer[0])) +
                            "\nFalsche Antworten: " + numberFormat.format(Integer.parseInt(stats_answer[1])) +
                            "\nFehlerquote: " + numberFormat.format((Integer.parseInt(stats_answer[1])) * 1000 / (Integer.parseInt(stats_answer[0]) + Integer.parseInt(stats_answer[1])) / 10) + "%\n\n";
                    /*String statsranked = "**RANKED**" +
                            "\nSpiele: " + numberFormat.format(Integer.parseInt(stats_answer[8])) +
                            "\nKorrekte Antworten: " + numberFormat.format(Integer.parseInt(stats_answer[2])) +
                            "\nFalsche Antworten: " + numberFormat.format(Integer.parseInt(stats_answer[3])) +
                            "\nFehlerquote: " + numberFormat.format((Integer.parseInt(stats_answer[3]) * 1000) / (Integer.parseInt(stats_answer[2]) + Integer.parseInt(stats_answer[3])) / 10) + "%";*/
                    stats.setDescription(
                           statsall + statscasual //+ statsranked
                    );
                    event.getChannel().sendMessage(stats.build()).queue();
                    break;
                case "ranking":
                case "leaderboard":
                    //send leaderboard
                    season = -1;
                    try {
                        season = (int) event.getOption("quiz_ranking_season").getAsLong();
                    } catch (Exception ignored) {}
                    seasonal = "";
                    if (season != -1)
                        seasonal = " where season = " + season;
                    int start;
                    try {
                        start = (int) event.getOption("quiz_ranking_rank").getAsLong();
                    } catch (Exception e) {
                        start = Integer.parseInt(Objects.requireNonNull(DatabaseHandler.database(event.getGuild().getId(), "select * from (select row_number() over (), id from (select id, max(elo), sum(ranked_games), avg(ranked_false / (ranked_correct + ranked_false)) from quizusers" + seasonal + " group by id order by max(elo) desc) as tmp) as temp where id = '" + event.getUser().getId() + "'"))[0]) - 5;
                    }
                    if (start <= 0)
                        start = 1;
                    int k;
                    String name;
                    String elo;
                    String games;
                    String quote;

                    String[] answer = DatabaseHandler.database(event.getGuild().getId(), "select id, max(elo), sum(ranked_games), avg(cast(ranked_false as double) * 1000 / (cast(ranked_correct as double) + cast(ranked_false as double)) / 10) from quizusers" + seasonal + " group by id order by max(elo) desc offset " + (start - 1) + " rows fetch next 10 rows only");

                    StringBuilder sb = new StringBuilder();

                    for (int j = 0; j < 10; j++) {
                        try {
                            name = Objects.requireNonNull(event.getJDA().getUserById(answer[j * 4])).getAsTag();
                        } catch (Exception e) {
                            name = Objects.requireNonNull(answer)[j * 4];
                        }
                        elo = answer[j*4+1];
                        games = answer[j*4+2];
                        quote = String.valueOf(Math.round(Float.parseFloat(answer[j*4+3])));

                        if (name.equals(event.getUser().getAsTag()) && j == 0) {
                            sb.append("```css\n");
                        } else if (!name.equals(event.getUser().getAsTag()) && j == 0) {
                            sb.append("```");
                        } else if (name.equals(event.getUser().getAsTag())) {
                            sb.append("\n``````css\n");
                        }

                        sb.append(start + j);
                        sb.append(". ");
                        sb.append(name);
                        k = name.length();
                        while (k < 35) {
                            sb.append(" ");
                            k++;
                        }
                        numberFormat = new DecimalFormat("###,###,###,###,###");
                        sb.append("ELO:");
                        sb.append(numberFormat.format(Long.parseLong(elo)));
                        k = elo.length();
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append("Ranglistenspiele:");
                        sb.append(numberFormat.format(Long.parseLong(games)));
                        k = games.length();
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append("Fehlerquote:");
                        sb.append(numberFormat.format(Long.parseLong(quote))).append("%");
                        k = quote.length() + 1;
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append("\n");
                        if (name.equals(event.getUser().getAsTag()) && !(j == 9)) {
                            sb.append("\n``````");
                        }

                    }
                    sb.append("```");

                    event.getChannel().sendMessage(sb.toString()).queue();
                    break;
                case "report":
                    //TODO: Errorhandling
                    String id = event.getOption("report_question").getAsString().replace("#", "");
                    String[] question = null;
                    try {
                        question = DatabaseHandler.database(STATIC.GUILD_ID, "select * from quizquestions where id = " + id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    assert question != null;
                    EmbedBuilder expert = new EmbedBuilder();
                    expert.setColor(Color.RED);
                    expert.setTitle("Eine Frage von " + Objects.requireNonNull(event.getJDA().getUserById(question[16])).getAsTag() + " wurde reportet!");
                    expert.setDescription("Die Frage **#" + question[0] + "** von **" + Objects.requireNonNull(event.getJDA().getUserById(question[16])).getAsTag() + "**" +
                                    " wurde von **" + event.getUser().getAsTag() + "** mit dem Grund `" + event.getOption("quiz_report_reason").getAsString() + "` reportet!\n" +
                            "Bitte kontrolliert folgende Frage und ihre Antworten und reagiert mit den entsprechenden Emotes.\n" +
                            "Insgesamt m\u00fcssen bei dieser Frage **" + question[12] + " Antworten** gegeben werden.");
                    expert.addField("Frage", question[1], false);
                    ArrayList<String> answer_list = new ArrayList<>(Arrays.asList(question).subList(2, question.length - 4));
                    for (int i = 0; i < 10; i++) {
                        if (answer_list.get(i).length() != 0)
                            expert.addField((i + 1) + ". Antwort", answer_list.get(i), false);
                    }
                    String[] finalQuestion = question;
                    Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(STATIC.GUILD_ID)).getTextChannelById(STATIC.QUESTION_CHANNEL)).sendMessage(expert.build()).queue(msg -> {
                        msg.addReaction("\u2705").queue();
                        msg.addReaction("\u26D4").queue();
                        msg.addReaction("\u270F").queue();
                        msg.addReaction("\u21A9").queue();
                        msg.addReaction("\uD83C\uDFAD").queue();
                        try {
                            DatabaseHandler.database(STATIC.GUILD_ID, "update quizquestions set status = " + msg.getId() + ", report_id = '" + event.getUser().getId() + "', reason = '" + event.getOption("quiz_report_reason").getAsString() + "' where id = " + finalQuestion[0]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    
                    break;
                case "add":
                    //add question
                    DatabaseHandler.database(event.getGuild().getId(), "delete from reports where victim_id = '" + event.getUser().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4' or report_id = '5')");
                    DatabaseHandler.database(event.getGuild().getId(), "delete from quizquestions where author_id = '" + event.getUser().getId() + "' and status < 14");
                    DatabaseHandler.database(event.getGuild().getId(), "insert into quizquestions (question, answer1, answer2, answer3, answer4, answer5, " +
                            "answer6, answer7, answer8, answer9, answer10, threshhold, correct, incorrect, status, author_id, report_id, reason, edit_id) " +
                            "values ('', '', '', '', '', '', '', '', '', '', '', 0, 0, 0, 1, '" + event.getUser().getId() + "', '', '', '')");
                    event.getUser().openPrivateChannel().queue(channel -> {
                        channel.sendMessage(">>> Hey **" + event.getUser().getAsTag() + "**,\n" +
                                "um eine Frage f\u00fcr das Quiz zu erstellen, musst du folgende Dinge angeben:").queue();
                        channel.sendMessage(">>> **Welche Frage m\u00f6chtest du hinzuf\u00fcgen?** Achte hier bitte darauf, die korrekte Rechtschreibung und Grammatik zu benutzen.\n" +
                                "Du kannst jederzeit neu anfangen, in dem du auf dem Server wieder den Befehl `/quiz add` ausf\u00fchrst.").queue();
                    });
                    break;
                default:
                    //send help
                    break;
            }
        } catch (Exception ignored){
            //send help
        }
    }
}