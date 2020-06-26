package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class reportListener extends ListenerAdapter {
//TODO: make this multi-guild-usable
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database("388969412889411585", "select * from reports where victim_id = '" + event.getAuthor().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() == 1) {
            switch (answer[0]) {
                case "1":
                    String user;
                    try {
                        if (event.getMessage().getContentRaw().contains("#"))
                            user = event.getJDA().getUserByTag(event.getMessage().getContentRaw()).getId();
                        else
                            user = event.getJDA().getUserById(event.getMessage().getContentRaw()).getId();
                    } catch (Exception e) {
                        event.getChannel().sendMessage(">>> Deine Angabe ist fehlerhaft. Bitte gib entweder die ID oder den Tag (Name#1234) an!").queue();
                        return;
                    }
                    event.getChannel().sendMessage(">>> Wo hat sich der Vorfall abgespielt? Gib hier bestenfalls die Channel-ID oder den Namen des Chats (egal ob Text oder Voice) an.").queue();
                    try {
                        core.databaseHandler.database("388969412889411585", "update reports set report_id = '2', offender_id = '" + user + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '1'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    event.getChannel().sendMessage(">>> Wie w\u00fcrdest du den Vorfall einfach kategorisieren? (beispielsweise \"Beleidigung\", \"Hetze\" oder \"Spam\")\n " +
                            "Halte diese Angabe so allgemein und unpers\u00f6nlich wie m\u00f6glich!").queue();
                    Guild guild = event.getJDA().getGuildById("388969412889411585");
                    String channel;
                    assert guild != null;
                    try {
                        channel = Objects.requireNonNull(guild.getTextChannelById(answer[3])).getAsMention();
                    } catch (Exception e) {
                        try {
                            channel = Objects.requireNonNull(guild.getTextChannelsByName(answer[3], true).get(0)).getAsMention();
                        } catch (Exception ex) {
                            try {
                                channel = Objects.requireNonNull(guild.getGuildChannelById(answer[3])).getName();
                            } catch (Exception exc) {
                                channel = answer[3];
                            }
                        }
                    }
                    try {
                        core.databaseHandler.database("388969412889411585", "update reports set report_id = '3', channel = '" + channel + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '2'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    event.getChannel().sendMessage(">>> Gibt es noch weitere Details oder Beschreibungen, die du angeben m\u00f6chtest? " +
                            "Je besser die Admins \u00fcber die Situation in Kenntnis gesetzt werden, desto genauer und fairer werden m\u00f6gliche Konsequenzen ausfallen!").queue();
                    try {
                        core.databaseHandler.database("388969412889411585", "update reports set report_id = '4', cause = '" + event.getMessage().getContentRaw() + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '3'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    event.getChannel().sendMessage(">>> Wenn du den Report absenden m\u00f6chtest, dann klicke hier auf den Haken!").queue(msg -> msg.addReaction("\u2705").queue());
                    try {
                        core.databaseHandler.database("388969412889411585", "update reports set report_id = '5', info = '" + event.getMessage().getContentRaw() + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '4'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database("388969412889411585", "select * from reports where victim_id = '" + event.getUserId() + "' and report_id = '5'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() == 1) {
            Guild guild = event.getJDA().getGuildById("388969412889411585");
            assert guild != null;
            TextChannel modlog = guild.getTextChannelById("434007950852489216");
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.RED);
            embed.setTitle("REPORT");
            embed.setDescription("Der Nutzer **" + Objects.requireNonNull(guild.getMemberById(answer[1])).getUser().getAsTag() + "** hat den Nutzer **" + Objects.requireNonNull(guild.getMemberById(answer[2])).getUser().getAsTag() + "** reportet.");
            embed.addField("Grund:", answer[4], true);
            try {
                embed.addField("Channel:", Objects.requireNonNull(guild.getTextChannelById(answer[3])).getAsMention(), true);
            } catch (Exception e) {
                try {
                    embed.addField("Channel:", Objects.requireNonNull(guild.getTextChannelsByName(answer[3], true).get(0)).getAsMention(), true);
                } catch (Exception ex) {
                    try {
                        embed.addField("Channel:", Objects.requireNonNull(guild.getGuildChannelById(answer[3])).getName(), true);
                    } catch (Exception exc) {
                        embed.addField("Channel:", answer[3], true);
                    }
                }
            }
            embed.addField("Beschreibung:", answer[5], true);
            assert modlog != null;
            modlog.sendMessage(embed.build()).queue(msg -> {
                msg.addReaction("\u21A9").queue();
                msg.addReaction("\u2705").queue();
                msg.addReaction("\uD83C\uDFAD").queue();
                msg.addReaction("\u2B55").queue();
                msg.addReaction("\u26D4").queue();
                msg.addReaction("\uD83D\uDD28").queue();
                try {
                    core.databaseHandler.database("388969412889411585", "update reports set report_id = '" + msg.getId() + "' where victim_id = '" + event.getUserId() + "' and report_id = '5'");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}