package listeners;

import core.messageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static core.databaseHandler.database;

public class loginListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String topic = event.getChannel().getTopic() + "";
        if (!topic.contains("{SPAM}")) {
            MessageEmbed emsg = null;
            try {
                emsg = login(event.getGuild().getId(), event.getAuthor());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (emsg != null) {
                messageActions.selfDestroyEmbedMSG(emsg, 3000, event);
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        MessageEmbed emsg = null;
        try {
            emsg = login(event.getGuild().getId(), event.getMember().getUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert emsg != null;
        event.getGuild().getTextChannelsByName(event.getChannelJoined().getName(), true).get(0).sendMessage(emsg).queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
    }

    private MessageEmbed login(String guild_id, User author) throws SQLException {
        MessageEmbed emsg = null;
        if (!author.isBot()) {

                int loginint = LocalDate.now().getDayOfYear() + LocalDate.now().getYear() * 365;
                int next_loginint = LocalDate.now().plusDays(1).getDayOfYear() + LocalDate.now().plusDays(1).getYear() * 365;

                int getint;
                int streak;
                String[] answer = {};
                try {
                    answer = database(guild_id, "select nextlogin, loginstreak from users where id = '" + author.getId() + "'");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    assert answer != null;
                    getint = Integer.parseInt(answer[0]);
                } catch (Exception e) {
                    getint = 0;
                }
                try {
                    streak = Integer.parseInt(answer[1]);
                } catch (Exception e) {
                    streak = 0;
                }


                if (getint == loginint) {
                    streak++;
                } else {
                    streak = 0;
                }

                if (loginint >= getint) {
                    String[] answer3 = null;
                    try {
                        answer3 = core.databaseHandler.database(guild_id, "select activity from users where id = '" + author.getId() + "'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    assert answer3 != null;
                    long activity = Long.parseLong(answer3[0]);

                    long newActivity;

                    if (activity < 120) {
                        newActivity = 120;
                    } else {
                        newActivity = activity + 5;
                    }

                    try {
                        core.databaseHandler.database(guild_id, "update users set activity = " + newActivity + " where id = '" + author.getId() + "'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    long streakboost;
                    if (streak < 8) {
                        streakboost = 30 + streak * 10;
                    } else {
                        streakboost = 100;
                    }

                    EmbedBuilder msg = new EmbedBuilder();
                    msg.setColor(Color.GREEN);
                    msg.setTitle(messageActions.getLocalizedString("login_title", "user", author.getId()));
                    msg.setDescription(messageActions.getLocalizedString("login_msg", "server", guild_id)
                            .replace("[USER]", author.getAsMention()).replace("[XP]", String.valueOf(streakboost)));
                    emsg = msg.build();

                    database(guild_id, "update users set xp = xp + " + streakboost + ", nextlogin = " + next_loginint + ", loginstreak = " + streak + " where id = '" + author.getId() + "'");
            }
        }
        return emsg;
    }
}
