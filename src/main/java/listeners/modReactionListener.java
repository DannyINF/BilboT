package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class modReactionListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getChannel().getId().equals("434007950852489216")) {
            if (event.getMember().getUser().isBot())
                return;
            int reactionCount;
            String mode;
            switch (event.getReactionEmote().getEmoji()) {
                case "\u21A9":
                    reactionCount = 1;
                    mode = "troll";
                    break;
                case "\u2705":
                    reactionCount = 2;
                    mode = "nothing";
                    break;
                case "\uD83C\uDFAD":
                    reactionCount = 1;
                    mode = "discussion";
                    break;
                case "\u2B55":
                    reactionCount = 1;
                    mode = "exil";
                    break;
                case "\u26D4":
                    reactionCount = 2;
                    mode = "kick";
                    break;
                case "\uD83D\uDD28":
                    reactionCount = 3;
                    mode = "ban";
                    break;
                default:
                    return;
            }
            event.getChannel().retrieveMessageById(event.getMessageId()).queue(msg -> msg.retrieveReactionUsers(event.getReactionEmote().getEmoji()).queue(users -> {
                System.out.println(users.size());
                if (users.size() > reactionCount)
                    executeAction(event, mode, msg, users);
            }));

        }
    }

    private static void executeAction(GuildMessageReactionAddEvent event, String mode, Message msg, List<User> users) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database(event.getGuild().getId(), "select * from reports where report_id = '" + msg.getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        URL jump = null;
        try {
            jump = new URL("https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessageId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder stimmen = new StringBuilder();
        for (User user : users) {
            if (!user.isBot()) {
                stimmen.append(user.getAsTag());
                stimmen.append(", ");
            }
        }
        stimmen.deleteCharAt(stimmen.length()-2);
        Member victim = Objects.requireNonNull(event.getGuild().getMemberById(answer[1]));
        Member offender = Objects.requireNonNull(event.getGuild().getMemberById(answer[2]));
        switch (mode) {
            case "troll":
                try {
                    core.databaseHandler.database(event.getGuild().getId(), "update users set coins = coins - (coins * 0.05) where id = '" + victim.getId() + "'");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.CYAN);
                embed.setTitle("Report bearbeitet: TROLL");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen. \nDem Nutzer **" + victim.getUser().getAsTag() + "**" +
                        " wurden f\u00fcr Trolling 5% seiner Coins abgezogen!\n" +
                        "Zust\u00e4ndige Administratoren: " + stimmen.toString());
                event.getChannel().sendMessage(embed.build()).queue();

                EmbedBuilder pm = new EmbedBuilder();
                pm.setColor(Color.red);
                pm.setTitle("Verwarnung f\u00fcr Trolling");
                pm.setDescription("Dein Report \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" + answer[4] + "` wurde als Trolling eingestuft.\n" +
                        "Als Bestrafung wurden dir auf dem **Tolkien Discord** 5% deiner Coins abgezogen.\n" +
                        "Unterlasse in Zukunft das Erstellen von Trollreports, da sonst zu h\u00e4rteren Strafen gegriffen wird!");
                victim.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessage(pm.build()).queue()
                );

                msg.clearReactions().queue();
                msg.addReaction("\u2611").queue();
                break;
            case "nothing":
                embed = new EmbedBuilder();
                embed.setColor(Color.GREEN);
                embed.setTitle("Report bearbeitet: GESCHLOSSEN");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Zust\u00e4ndige Administratoren: " + stimmen.toString());
                event.getChannel().sendMessage(embed.build()).queue();

                msg.clearReactions().queue();
                msg.addReaction("\u2611").queue();
                break;
            case "discussion":
                event.getReaction().removeReaction(event.getUser()).queue();
                assert jump != null;
                Objects.requireNonNull(event.getGuild().getTextChannelById("461434087857586177")).sendMessage(
                        event.getGuild().getRolesByName("Vala", true).get(0).getAsMention()).queue();
                embed = new EmbedBuilder();
                embed.setColor(Color.YELLOW);
                embed.setTitle("Report bearbeiten: " + event.getMember().getUser().getAsTag());
                embed.setDescription("Seht euch mal diesen [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` an!");
                event.getChannel().sendMessage(embed.build()).queue();
                break;
            case "exil":
                Role exil = event.getGuild().getRolesByName("exil", true).get(0);
                if (offender.getRoles().contains(exil)) {
                    event.getReaction().removeReaction(event.getUser()).queue();
                    break;
                }
                embed = new EmbedBuilder();
                embed.setColor(Color.ORANGE);
                embed.setTitle("Report bearbeitet: EXIL");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde in das Exil verschoben.\n" +
                        "Zust\u00e4ndige Administratoren: " + stimmen.toString());
                event.getChannel().sendMessage(embed.build()).queue();

                pm = new EmbedBuilder();
                pm.setColor(Color.red);
                pm.setTitle("Du wurdest in das Exil verschoben!");
                pm.setDescription("Aufgrund von `" + answer[4] + "` wurdest du auf dem **Tolkien Discord** in das Exil verschoben.\n" +
                        "Im Exilchannel kannst du nun alles Weitere mit den Administratoren besprechen.");
                offender.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessage(pm.build()).queue()
                );

                try {
                    commands.cmdExil.exileMember(event.getGuild(), offender);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                msg.clearReactions().queue();
                msg.addReaction("\u2611").queue();
                break;
            case "kick":
                embed = new EmbedBuilder();
                embed.setColor(Color.RED);
                embed.setTitle("Report bearbeitet: KICK");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde vom Server gekickt.\n" +
                        "Zust\u00e4ndige Administratoren: " + stimmen.toString());
                event.getChannel().sendMessage(embed.build()).queue();

                pm = new EmbedBuilder();
                pm.setColor(Color.red);
                pm.setTitle("Du wurdest gekickt!");
                pm.setDescription("Aufgrund von `" + answer[4] + "` wurdest du vom **Tolkien Discord** gekickt.\n" +
                        "Du kannst weiterhin dem Server beitreten.");
                String[] finalAnswer1 = answer;
                offender.getUser().openPrivateChannel().queue(channel -> {
                    channel.sendMessage(pm.build()).queue();
                    offender.kick(finalAnswer1[4]).queue();
                });

                msg.clearReactions().queue();
                msg.addReaction("\u2611").queue();
                break;
            case "ban":
                embed = new EmbedBuilder();
                embed.setColor(Color.BLACK);
                embed.setTitle("Report bearbeitet: BANN");
                embed.setDescription("Der [Report](" + jump + ") von **" + victim.getUser().getAsTag() + "**" +
                        " \u00fcber **" + offender.getUser().getAsTag() + "** mit dem Grund `" +
                        answer[4] + "` wurde geschlossen.\n" +
                        "Der Nutzer **" + offender.getUser().getAsTag() + "** wurde vom Server gebannt.\n" +
                        "Zust\u00e4ndige Administratoren: " + stimmen.toString());
                event.getChannel().sendMessage(embed.build()).queue();

                pm = new EmbedBuilder();
                pm.setColor(Color.red);
                pm.setTitle("Du wurdest gebannt!");
                pm.setDescription("Aufgrund von `" + answer[4] + "` wurdest du vom **Tolkien Discord** gebannt.\n" +
                        "Kontaktiere einen Administrator, falls du mehr \u00fcber deine Banstrafe erfahren m\u00f6chtest.");
                String[] finalAnswer = answer;
                offender.getUser().openPrivateChannel().queue(channel -> {
                    channel.sendMessage(pm.build()).queue();
                    offender.ban(1, finalAnswer[4]).queue();
                });

                msg.clearReactions().queue();
                msg.addReaction("\u2611").queue();
                break;
        }
    }
}
