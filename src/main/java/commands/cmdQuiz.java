package commands;

import core.databaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.derby.impl.sql.execute.CurrentDatetime;
import util.getUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class cmdQuiz implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        /*if (!(event.getAuthor().getId().equals("277746420281507841") || event.getAuthor().getId().equals("209272591532163073") || event.getAuthor().getId().equals("305256757398339585") || event.getAuthor().getId().equals("354354147614654465"))) {
            event.getChannel().sendMessage("Still beta, dude \uD83D\uDE0F").queue();
            return;
        }*/
        if (args.length > 0) {
            switch (args[0]) {
                case "casual":
                    //start casual match
                    break;
                case "ranked":
                    //start ranked match
                    break;
                case "stats":
                    //send stats
                    EmbedBuilder stats = new EmbedBuilder();
                    stats.setColor(Color.WHITE);
                    int season = -1;
                    if (args.length > 1) {
                        season = Integer.parseInt(args[1]);
                    }
                    Member member;
                    if (args.length > 2) {
                        //getUser
                        ArrayList<String> list = new ArrayList<>(Arrays.asList(args).subList(2, args.length));
                        member = getUser.getMemberFromInput(list.toArray(new String[0]), event.getAuthor(), event.getGuild(), event.getChannel());
                    } else {
                        //self
                        member = event.getMember();
                    }
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
                    String[] stats_answer = core.databaseHandler.database(event.getGuild().getId(),
                            "select sum(casual_correct), sum(casual_false), sum(ranked_correct), sum(ranked_false), max(casual_streak), max(ranked_streak), " +
                                    "max(peak), sum(casual_games), sum(ranked_games) from quizusers where id = '" + member.getId() + "'" + seasonal);
                    assert stats_answer != null;
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
                    String statsranked = "**RANKED**" +
                            "\nSpiele: " + numberFormat.format(Integer.parseInt(stats_answer[8])) +
                            "\nKorrekte Antworten: " + numberFormat.format(Integer.parseInt(stats_answer[2])) +
                            "\nFalsche Antworten: " + numberFormat.format(Integer.parseInt(stats_answer[3])) +
                            "\nFehlerquote: " + numberFormat.format((Integer.parseInt(stats_answer[3]) * 1000) / (Integer.parseInt(stats_answer[2]) + Integer.parseInt(stats_answer[3])) / 10) + "%";
                    stats.setDescription(
                           statsall + statscasual + statsranked
                    );
                    event.getChannel().sendMessage(stats.build()).queue();
                    break;
                case "ranking":
                case "leaderboard":
                    //send leaderboard
                    season = -1;
                    if (args.length > 1) {
                        season = Integer.parseInt(args[1]);
                    }
                    seasonal = "";
                    if (season != -1)
                        seasonal = " where season = " + season;
                    int start;
                    try {
                        start = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        start = Integer.parseInt(Objects.requireNonNull(databaseHandler.database(event.getGuild().getId(), "select * from (select row_number() over (), id from (select id, max(elo), sum(ranked_games), avg(ranked_false / (ranked_correct + ranked_false)) from quizusers" + seasonal + " group by id order by max(elo) desc) as tmp) as temp where id = '" + event.getAuthor().getId() + "'"))[0]) - 5;
                    }
                    if (start <= 0)
                        start = 1;
                    int k;
                    String name;
                    String elo;
                    String games;
                    String quote;

                    String[] answer = core.databaseHandler.database(event.getGuild().getId(), "select id, max(elo), sum(ranked_games), avg(cast(ranked_false as double) * 1000 / (cast(ranked_correct as double) + cast(ranked_false as double)) / 10) from quizusers" + seasonal + " group by id order by max(elo) desc offset " + (start - 1) + " rows fetch next 10 rows only");

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

                        if (name.equals(event.getAuthor().getAsTag()) && j == 0) {
                            sb.append("```css\n");
                        } else if (!name.equals(event.getAuthor().getAsTag()) && j == 0) {
                            sb.append("```");
                        } else if (name.equals(event.getAuthor().getAsTag())) {
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
                        if (name.equals(event.getAuthor().getAsTag()) && !(j == 9)) {
                            sb.append("\n``````");
                        }

                    }
                    sb.append("```");

                    event.getChannel().sendMessage(sb.toString()).queue();
                    break;
                case "report":
                    //TODO: Errorhandling
                    if (args.length > 2) {
                        String id = args[1].replace("#", "");
                        ArrayList<String> list = new ArrayList<>(Arrays.asList(args).subList(2, args.length));
                        StringBuilder reason = new StringBuilder();
                        for (String str : list) {
                            reason.append(str);
                            reason.append(" ");
                        }
                        String[] question = null;
                        try {
                            question = core.databaseHandler.database("388969412889411585", "select * from quizquestions where id = " + id);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        assert question != null;
                        EmbedBuilder expert = new EmbedBuilder();
                        expert.setColor(Color.RED);
                        expert.setTitle("Eine Frage von " + Objects.requireNonNull(event.getJDA().getUserById(question[16])).getAsTag() + " wurde reportet!");
                        expert.setDescription("Die Frage **#" + question[0] + "** von **" + Objects.requireNonNull(event.getJDA().getUserById(question[16])).getAsTag() + "**" +
                                        " wurde von **" + event.getAuthor().getAsTag() + "** mit dem Grund `" + reason.toString() + "` reportet!\n" +
                                "Bitte kontrolliert folgende Frage und ihre Antworten und reagiert mit den entsprechenden Emotes.\n" +
                                "Insgesamt m\u00fcssen bei dieser Frage **" + question[12] + " Antworten** gegeben werden.");
                        expert.addField("Frage", question[1], false);
                        ArrayList<String> answer_list = new ArrayList<>(Arrays.asList(question).subList(2, question.length - 4));
                        for (int i = 0; i < 10; i++) {
                            if (answer_list.get(i).length() != 0)
                                expert.addField((i + 1) + ". Antwort", answer_list.get(i), false);
                        }
                        String[] finalQuestion = question;
                        Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(388969412889411585L)).getTextChannelById(588823110250266641L)).sendMessage(expert.build()).queue(msg -> {
                            msg.addReaction("\u2705").queue();
                            msg.addReaction("\u26D4").queue();
                            msg.addReaction("\u270F").queue();
                            msg.addReaction("\u21A9").queue();
                            msg.addReaction("\uD83C\uDFAD").queue();
                            try {
                                core.databaseHandler.database("388969412889411585", "update quizquestions set status = " + msg.getId() + ", report_id = '" + event.getAuthor().getId() + "', reason = '" + reason.toString() + "' where id = " + finalQuestion[0]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    break;
                case "add":
                    //add question
                    core.databaseHandler.database(event.getGuild().getId(), "delete from reports where victim_id = '" + event.getAuthor().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4' or report_id = '5')");
                    core.databaseHandler.database(event.getGuild().getId(), "delete from quizquestions where author_id = '" + event.getAuthor().getId() + "' and status < 14");
                    core.databaseHandler.database(event.getGuild().getId(), "insert into quizquestions (question, answer1, answer2, answer3, answer4, answer5, " +
                            "answer6, answer7, answer8, answer9, answer10, threshhold, correct, incorrect, status, author_id, report_id, reason, edit_id) " +
                            "values ('', '', '', '', '', '', '', '', '', '', '', 0, 0, 0, 1, '" + event.getAuthor().getId() + "', '', '', '')");
                    event.getAuthor().openPrivateChannel().queue(channel -> {
                        channel.sendMessage(">>> Hey **" + event.getAuthor().getAsTag() + "**,\n" +
                                "um eine Frage f\u00fcr das Quiz zu erstellen, musst du folgende Dinge angeben:").queue();
                        channel.sendMessage(">>> **Welche Frage m\u00f6chtest du hinzuf\u00fcgen?** Achte hier bitte darauf, die korrekte Rechtschreibung und Grammatik zu benutzen.\n" +
                                "Du kannst jederzeit neu anfangen, in dem du auf dem Server wieder den Befehl `/quiz add` ausf\u00fchrst.").queue();
                    });
                    break;
                default:
                    //send help
                    break;
            }
        } else {
            //send help
        }

    }
}
