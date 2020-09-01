package listeners;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExpertReactionListener extends ListenerAdapter {
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getChannel().getId().equals(STATIC.QUESTION_CHANNEL)) {
            if (event.getMember().getUser().isBot())
                return;
            int reactionCount;
            String mode;
            switch (event.getReactionEmote().getEmoji()) {
                case "\u2705":
                    reactionCount = 2;
                    mode = "accept";
                    break;
                case "\u26D4":
                    reactionCount = 2;
                    mode = "deny";
                    break;
                case "\u270F":
                    reactionCount = 1;
                    mode = "edit";
                    break;
                case "\u21A9":
                    reactionCount = 2;
                    mode = "troll";
                    break;
                case "\uD83C\uDFAD":
                    reactionCount = 2;
                    mode = "report";
                    break;
                case "\u2611":
                    reactionCount = 1;
                    mode = "remove";
                    break;
                default:
                    return;
            }
            event.getChannel().retrieveMessageById(event.getMessageId()).queue(msg -> msg.retrieveReactionUsers(event.getReactionEmote().getEmoji()).queue(users -> {
                if (users.size() > reactionCount)
                    executeAction(event, mode, msg, users);
            }));
        }
    }

    private static void executeAction(GuildMessageReactionAddEvent event, String mode, Message msg, List<User> users) {
        String[] answer = null;
        boolean isReport = false;
        User user_question = null;
        User user_reporter = null;
        StringBuilder stimmen = new StringBuilder();
        URL jump = null;
        if (!mode.equals("remove")) {

            try {
                answer = DatabaseHandler.database(event.getGuild().getId(), "select * from quizquestions where status = " + msg.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert answer != null;
            try {
                jump = new URL("https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessageId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            for (User user : users) {
                if (!user.isBot()) {
                    stimmen.append(user.getAsTag());
                    stimmen.append(", ");
                    if (!mode.equals("edit")) {
                        try {
                            DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins + 1 where id = '" + user.getId() + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            stimmen.deleteCharAt(stimmen.length()-2);

            user_question = event.getJDA().getUserById(answer[16]);
            if (answer[17].length() != 0) {
                user_reporter = event.getJDA().getUserById(answer[17]);
                isReport = true;
            }
        }
        switch (mode) {
            case "remove":
                if (users.contains(event.getJDA().getSelfUser()))
                    event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> message.delete().queue());
                break;
            case "accept":
                EmbedBuilder embed_expert = new EmbedBuilder();
                EmbedBuilder pm_question = new EmbedBuilder();
                EmbedBuilder pm_reporter = new EmbedBuilder();

                if (isReport) {
                    embed_expert.setColor(Color.GREEN);
                    embed_expert.setTitle("Report bearbeitet: GESCHLOSSEN");
                    assert user_reporter != null;
                    assert user_question != null;
                    embed_expert.setDescription("Der Report von **" + user_reporter.getAsTag() + "**" +
                            " \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" +
                            answer[18] + "` wurde geschlossen.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_reporter.setColor(Color.GREEN);
                    pm_reporter.setTitle("Dein Report wurde geschlossen!");
                    pm_reporter.setDescription("Dein Report \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" + answer[18] + "` wurde geschlossen.");
                    user_reporter.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_reporter.build()).queue()
                    );
                    try {
                        DatabaseHandler.database(event.getGuild().getId(), "update quizquestions set status = 111 where id = " + answer[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    embed_expert.setColor(Color.GREEN);
                    embed_expert.setTitle("Frage bearbeitet: ANGENOMMEN");
                    assert user_question != null;
                    embed_expert.setDescription("Die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** wurde angenommen.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();
                    //status = 111 -> ready
                    try {
                        DatabaseHandler.database(event.getGuild().getId(), "update quizquestions set status = 111 where id = " + answer[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    int coins;
                    if (answer[19].length() == 0) {
                        try {
                            DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins + 7 where id = '" + user_question.getId() + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        coins = 7;
                    } else {
                        try {
                            DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins + 3 where id = '" + user_question.getId() + "' or id = '" + answer[19] + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        coins = 3;
                    }
                    pm_question.setColor(Color.GREEN);
                    pm_question.setTitle("Deine Frage wurde angenommen!");
                    pm_question.setDescription("Deine Frage `" + answer[1] + "` wurde angenommen und du erh\u00e4ltst `" + coins + "` Coins.");
                    user_question.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_question.build()).queue()
                    );
                }
                msg.delete().queue();
                break;
            case "deny":
                if (isReport) {
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.RED);
                    embed_expert.setTitle("Report bearbeitet: GEL\u00d6SCHT");
                    assert user_reporter != null;
                    assert user_question != null;
                    embed_expert.setDescription("Aufgrund des Reports von **" + user_reporter.getAsTag() + "**" +
                            " \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" +
                            answer[18] + "` wurde diese gel\u00f6scht.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_question = new EmbedBuilder();
                    pm_question.setColor(Color.red);
                    pm_question.setTitle("Deine Frage wurde gel\u00f6scht.");
                    pm_question.setDescription("Aufgrund von `" + answer[18] + "` wurde deine Frage **#" + answer[0] + "** `" + answer[1] + "` gel\u00f6scht.");
                    user_question.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_question.build()).queue()
                    );

                    pm_reporter = new EmbedBuilder();
                    pm_reporter.setColor(Color.red);
                    pm_reporter.setTitle("Eine Frage wurde entfernt!");
                    pm_reporter.setDescription("Aufgrund deines Reports mit dem Grund `" + answer[18] + "` wurde die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** gel\u00f6scht!");
                    user_reporter.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_reporter.build()).queue()
                    );
                } else {
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.RED);
                    embed_expert.setTitle("Frage bearbeitet: ABGELEHNT");
                    assert user_question != null;
                    embed_expert.setDescription("Die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** wurde abgelehnt.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_question = new EmbedBuilder();
                    pm_question.setColor(Color.red);
                    pm_question.setTitle("Deine Frage wurde abgelehnt.");
                    pm_question.setDescription("Deine Frage `" + answer[1] + "` wurde abgelehnt.");
                    user_question.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_question.build()).queue()
                    );
                }
                try {
                    DatabaseHandler.database(event.getGuild().getId(), "delete from quizquestions where id = " + answer[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                msg.delete().queue();
                break;
            case "edit":
                if (isReport) {
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.GREEN);
                    embed_expert.setTitle("Report bearbeitet: EDITIERT");
                    assert user_reporter != null;
                    assert user_question != null;
                    embed_expert.setDescription("Der Report von **" + user_reporter.getAsTag() + "**" +
                            " \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" +
                            answer[18] + "` wurde geschlossen und die Frage von **" + stimmen.toString() + "** wird editiert.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_reporter = new EmbedBuilder();
                    pm_reporter.setColor(Color.GREEN);
                    pm_reporter.setTitle("Eine Frage wird editiert!");
                    pm_reporter.setDescription("Aufgrund deines Reports mit dem Grund `" + answer[18] + "` wird die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** editiert!");
                    user_reporter.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_reporter.build()).queue()
                    );
                } else {
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.GREEN);
                    embed_expert.setTitle("Frage bearbeitet: EDITIERT");
                    assert user_question != null;
                    embed_expert.setDescription("Die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** wird editiert.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_question = new EmbedBuilder();
                    pm_question.setColor(Color.GREEN);
                    pm_question.setTitle("Deine Frage wird editiert!");
                    pm_question.setDescription("Deine Frage `" + answer[1] + "` wird editiert.");
                    user_question.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_question.build()).queue()
                    );
                }
                msg.delete().queue();
                try {
                    DatabaseHandler.database(event.getGuild().getId(), "update quizquestions set edit_id = '" + event.getUserId() + "', status = 14 where id = " + answer[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int answer_count = 11;
                ArrayList<String> list = new ArrayList<>(Arrays.asList(answer).subList(2, answer.length - 8));
                for (String str : list)
                    if (str.length() == 0)
                        answer_count--;
                int finalAnswer_count = answer_count;
                Objects.requireNonNull(event.getJDA().getUserById(event.getUserId())).openPrivateChannel().queue(channel -> channel.sendMessage(
                        ">>> Was m\u00f6chtest du an der Frage bearbeiten? (Frage: \uD83C\uDDF6, Antworten: 1\u20E3 bis \uD83D\uDD1F, Antwort hinzuf\u00fcgen: \uD83C\uDDE6, Anzahl ben\u00f6tigter Antworten: \uD83D\uDD22, Klicke auf \u2705, wenn du fertig bist.)"
                ).queue(message -> {
                    message.addReaction("\uD83C\uDDF6").queue();
                    for (int i = 1; i < finalAnswer_count; i++) {
                        if (i == 10) {
                            message.addReaction("\uD83D\uDD1F").queue();
                            break;
                        }
                        message.addReaction(i + "\u20E3").queue();
                    }
                    if (finalAnswer_count < 11)
                        message.addReaction("\uD83C\uDDE6").queue();
                    message.addReaction("\uD83D\uDD22").queue();
                    message.addReaction("\u2705").queue();
                }));

                break;
            case "troll":
                if (isReport) {
                    assert user_reporter != null;
                    try {
                        DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins - 7 where id = '" + user_reporter.getId() + "'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.CYAN);
                    embed_expert.setTitle("Report bearbeitet: TROLL");
                    assert user_question != null;
                    embed_expert.setDescription("Der Report von **" + user_reporter.getAsTag() + "**" +
                            " \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" +
                            answer[18] + "` wurde als Trolling eingestuft.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_reporter = new EmbedBuilder();
                    pm_reporter.setColor(Color.red);
                    pm_reporter.setTitle("Verwarnung f\u00fcr Trolling");
                    pm_reporter.setDescription("Dein Report \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" +
                            answer[18] + "` wurde als Trolling eingestuft.\n" +
                            "Als Bestrafung wurden dir auf dem **Tolkien Discord** `7` Coins abgezogen.\n" +
                            "Unterlasse in Zukunft das Erstellen von Trollreports, da sonst zu h\u00e4rteren Strafen gegriffen wird!");
                    user_reporter.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_reporter.build()).queue()
                    );
                    try {
                        DatabaseHandler.database(event.getGuild().getId(), "update quizquestions set status = 111 where id = " + answer[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    assert user_question != null;
                    try {
                        DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins - 7 where id = '" + user_question.getId() + "'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.CYAN);
                    embed_expert.setTitle("Frage bearbeitet: TROLL");
                    embed_expert.setDescription("Die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** wurde als Trolling eingestuft.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_question = new EmbedBuilder();
                    pm_question.setColor(Color.red);
                    pm_question.setTitle("Verwarnung f\u00fcr Trolling");
                    pm_question.setDescription("Deine Frage **#" + answer[0] + "** `" + answer[1] + "` wurde als Trolling eingestuft.\n" +
                            "Als Bestrafung wurden dir auf dem **Tolkien Discord** `7` Coins abgezogen.\n" +
                            "Unterlasse in Zukunft das Erstellen von Trollfragen, da sonst zu h\u00e4rteren Strafen gegriffen wird!");
                    user_question.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_question.build()).queue()
                    );
                    try {
                        DatabaseHandler.database(event.getGuild().getId(), "delete from quizquestions where id = " + answer[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                msg.delete().queue();
                break;
            case "report":
                String victim_id = null;
                EmbedBuilder report = new EmbedBuilder();
                report.setColor(Color.RED);
                report.setTitle("REPORT");
                if (isReport) {
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.GREEN);
                    embed_expert.setTitle("Report bearbeitet: REPORT");
                    assert user_reporter != null;
                    assert user_question != null;
                    embed_expert.setDescription("Der Report von **" + user_reporter.getAsTag() + "**" +
                            " \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" +
                            answer[18] + "` wurde an die Administratoren weitergeleitet.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_reporter = new EmbedBuilder();
                    pm_reporter.setColor(Color.GREEN);
                    pm_reporter.setTitle("Dein Report wurde weitergeleitet!");
                    pm_reporter.setDescription("Dein Report \u00fcber die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** mit dem Grund `" +
                            answer[18] + "` wurde an die Administratoren weitergeleitet.");
                    user_reporter.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_reporter.build()).queue()
                    );
                    victim_id = user_reporter.getId();
                } else {
                    embed_expert = new EmbedBuilder();
                    embed_expert.setColor(Color.GREEN);
                    embed_expert.setTitle("Frage bearbeitet: REPORT");
                    assert user_question != null;
                    embed_expert.setDescription("Die Frage **#" + answer[0] + "** von **" + user_question.getAsTag() + "** wurde an die Administratoren weitergeleitet.\n" +
                            "Zust\u00e4ndige Experten: " + stimmen.toString());
                    event.getChannel().sendMessage(embed_expert.build()).queue();

                    pm_question = new EmbedBuilder();
                    pm_question.setColor(Color.red);
                    pm_question.setTitle("Deine Frage wurde abgelehnt.");
                    pm_question.setDescription("Deine Frage `" + answer[1] + "` wurde abgelehnt.");
                    user_question.openPrivateChannel().queue(channel ->
                            channel.sendMessage(pm_question.build()).queue()
                    );
                    for (User user : users)
                        if (!user.isBot()) {
                            victim_id = user.getId();
                            break;
                        }
                }
                assert victim_id != null;
                report.setDescription("Der Nutzer **" + Objects.requireNonNull(event.getJDA().getUserById(victim_id)).getAsTag() + "** hat den Nutzer **" + user_question.getAsTag() + "** reportet.");
                report.addField("Grund:", answer[4], false);
                report.addField("Channel:", Objects.requireNonNull(event.getGuild().getTextChannelById(STATIC.QUESTION_CHANNEL)).getAsMention(), false);
                report.addField("Beschreibung:", "Dieser Report wurde automatisch generiert.", false);
                String[] finalAnswer = answer;
                String finalVictim_id = victim_id;
                URL finalJump = jump;
                User finalUser_question = user_question;
                Objects.requireNonNull(event.getGuild().getTextChannelById(434007950852489216L)).sendMessage(report.build()).queue(message -> {
                    message.addReaction("\u21A9").queue();
                    message.addReaction("\u2705").queue();
                    message.addReaction("\uD83C\uDFAD").queue();
                    message.addReaction("\u2B55").queue();
                    message.addReaction("\u26D4").queue();
                    message.addReaction("\uD83D\uDD28").queue();
                    try {
                        DatabaseHandler.database(event.getGuild().getId(), "insert into reports(report_id, victim_id, offender_id, channel, cause, info, ruling) " +
                                "values ('" + message.getId() + "', '" + finalVictim_id + "', '" + finalUser_question.getId() + "', '" + event.getGuild().getTextChannelById(STATIC.QUESTION_CHANNEL) +
                                "', '" + finalAnswer[18] + "', 'Dieser [Report](" + finalJump + ") wurde automatisch generiert.', '')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                msg.delete().queue();
                break;
        }
    }
}