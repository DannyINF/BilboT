package listeners;

import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

import static core.databaseHandler.database;

public class loginListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String topic = event.getTextChannel().getTopic() + "";
        if (!topic.contains("{SPAM}")) {
            MessageEmbed emsg = login(event.getGuild().getId(), event.getAuthor());
            if (emsg != null) {
                messageActions.selfDestroyEmbedMSG(emsg, 3000, event);
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        MessageEmbed emsg = login(event.getGuild().getId(), event.getMember().getUser());
        if (emsg != null) {
            messageActions.selfDestroyEmbedMSG(emsg, 3000, event);
        }
    }

    private MessageEmbed login(String guild_id, User author) {
        MessageEmbed emsg = null;
        if (!author.isBot()) {
            String status = "deactivated";
            try {
                status = modulesChecker.moduleStatus("xp", guild_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (status.equals("activated")) {

                int loginint = LocalDate.now().getDayOfYear() + LocalDate.now().getYear() * 365;
                int next_loginint = LocalDate.now().plusDays(1).getDayOfYear() + LocalDate.now().plusDays(1).getYear() * 365;

                int getint;
                int streak;
                String[] selectArgs = {"users", "id = '" + author.getId() + "'", "2", "nextlogin", "loginstreak"};
                String[] answer = {};
                try {
                    answer = database(guild_id, "select", selectArgs);
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

                    String[] arguments = {"users", "id = '" + author.getId() + "'", "1", "activity"};
                    String[] answer3 = null;
                    try {
                        answer3 = core.databaseHandler.database(guild_id, "select", arguments);
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

                    String[] arguments2 = {"users", "id = '" + author.getId() + "'", "activity", String.valueOf(newActivity)};
                    try {
                        core.databaseHandler.database(guild_id, "update", arguments2);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    int Xp;

                    String[] selectArgs2 = {"users", "id = '" + author.getId() + "'", "1", "xp"};
                    String[] answer2 = {};
                    try {
                        answer2 = database(guild_id, "select", selectArgs2);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert answer2 != null;
                        Xp = Integer.parseInt(answer2[0]);
                    } catch (Exception e) {
                        Xp = 0;
                    }

                    int newXP;
                    int streakboost;
                    if (streak < 8) {
                        streakboost = 30 + streak * 10;
                    } else {
                        streakboost = 100;
                    }

                    newXP = streakboost + Xp;

                    String[] updateArgs = {"users", "id = '" + author.getId() + "'", "xp", String.valueOf(newXP), "nextlogin",
                            String.valueOf(next_loginint), "loginstreak", String.valueOf(streak)};
                    try {
                        database(guild_id, "update", updateArgs);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    EmbedBuilder msg = new EmbedBuilder();
                    msg.setColor(Color.GREEN);
                    msg.setTitle(messageActions.getLocalizedString("login_title", "user", author.getId()));
                    msg.setDescription(messageActions.getLocalizedString("login_msg", "server", guild_id)
                            .replace("[USER]", author.getAsMention()).replace("[XP]", String.valueOf(streakboost)));
                    emsg = msg.build();
                }
            }
        }
        return emsg;
    }
}
