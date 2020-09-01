package listeners;

import core.DatabaseHandler;
import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;
import util.GiveXP;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class XpListener extends ListenerAdapter {

    /**
     * @param event GuildMessageReceivedEvent
     */
    private static void giveXP(GuildMessageReceivedEvent event) throws SQLException {
        int xp;
        int amount = event.getMessage().getContentRaw().length();

        // only the first 140 characters count
        xp = Math.min(amount, 140);

        try {
            event.getMessage().getAttachments().get(0);
            xp += 15;
        } catch (Exception ignored) {
        }

        // different multiplicators for different channels
        String channelname = event.getChannel().getName();
        String channeltopic;
        try {
            channeltopic = event.getChannel().getTopic();
            assert channeltopic != null;
            if (channeltopic.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            channeltopic = " ";
        }
        if (channelname.contains("spam") || channelname.contains("willkommen")) {
            xp = 0;
        } else if (channelname.contains("woechentliches-thema")) {
            xp = xp * 15 / 10;
        } else if (!channelname.contains("diskussion")) {
            if (channeltopic.contains("Chat zu")) {
                xp = xp / 8;
            } else {
                xp = xp / 3;
            }
        }

        if (event.getGuild().getCategoriesByName("\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550| INFO |\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550", true).get(0).getTextChannels().contains(event.getChannel())) {
            xp = 0;
        }

        GiveXP.giveXPToMember(event.getMember(), event.getGuild(), xp);
    }

    /**
     * @param event GuildMessageReceivedEvent
     */
    private static void checkLevel(GuildMessageReceivedEvent event) throws SQLException {
        if (PermissionChecker.checkRole(STATIC.getCam(), event.getMember()))
            return;

        long currentlevel;

        // getting level and xp of the user
        String[] level = DatabaseHandler.database(event.getGuild().getId(), "select level from users where id = '" + event.getAuthor().getId() + "'");

        try {
            currentlevel = Long.parseLong(level[0]);
        } catch (Exception e) {
            currentlevel = 0;
        }

        long newlevel = util.LevelChecker.checker(Objects.requireNonNull(event.getMember()), event.getGuild());

        DatabaseHandler.database(event.getGuild().getId(), "update users set level = " + newlevel + " where id = '" + event.getAuthor().getId() + "'");

        // if your current xp are bigger than the xp needed for the next level you receive a level up
        if (newlevel != currentlevel) {

            if (newlevel > currentlevel) {
                DatabaseHandler.database(event.getGuild().getId(), "update users set activity = activity + 5 where id = '" + Objects.requireNonNull(event.getMember()).getId() + "'");
            }

            //adding the coins received through level-up to the total coins-count
            if (newlevel > 50) {
                DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins + " + (25 * (newlevel - currentlevel)) + " where id = '" + event.getAuthor().getId() + "'");
            } else {
                long sum = 0;
                if (newlevel < currentlevel) {
                    for (long i = currentlevel; i > newlevel; i--) {
                        sum -= i / 2;
                    }
                } else {
                    for (long i = currentlevel + 1; i < newlevel + 1; i++) {
                        sum += i / 2;
                    }
                }
                DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins + " + sum + " where id = '" + event.getAuthor().getId() + "'");
            }
            // creating level-up msg
            if (!event.getAuthor().isBot()) {

                Message msg = event.getChannel().sendMessage(MessageActions.getLocalizedString("xp_level_up", "user", event.getAuthor().getId())
                        .replace("[USER]", event.getAuthor().getAsMention()).replace("[LEVEL]", String.valueOf(newlevel))).complete();
                // msg deletes itself after 15000 milliseconds
                if (!(newlevel == 10 || newlevel == 50 || newlevel == 150 || newlevel == 300 || newlevel == 500 || newlevel == 750 || newlevel == 1050)) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 15000);
                }
            }
        }

        DatabaseHandler.database(event.getGuild().getId(), "update users set level = " + newlevel + " where id = '" + event.getAuthor().getId() + "'");

    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isFake())
            return;
        try {
            String test = Objects.requireNonNull(DatabaseHandler.database("usersettings", "select id from users where id = '" + event.getAuthor().getId() + "'"))[0];
            if (test == null) {
                try {
                    DatabaseHandler.database("usersettings", "insert into users (id, language, country, sex, profile, spammer, verified) values " +
                            "('" + event.getAuthor().getId() + "', 'de', 'de', 'm', '', FALSE, FALSE)");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            try {
                DatabaseHandler.database("usersettings", "insert into users (id, language, country, sex, profile, spammer, verified) values " +
                        "('" + event.getAuthor().getId() + "', 'de', 'de', 'm', '', FALSE, FALSE)");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        try {
            String test = Objects.requireNonNull(DatabaseHandler.database(event.getGuild().getId(), "select id from users where id = '" + event.getAuthor().getId() + "'"))[0];
            if (test == null) {
                try {
                    DatabaseHandler.database(event.getGuild().getId(), "insert into users (id, xp, level, coins, ticket, intro, profile, words, msg, chars, " +
                            "voicetime, reports, moderations, loginstreak, nextlogin, verifystatus, activity, banlog) values (" +
                            "'" + event.getAuthor().getId() + "', 0, 0, 0, 0, '', '', 0, 0, 0, 0, 0, 0, 0, 0, TRUE, 120, '')");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            try {
                DatabaseHandler.database(event.getGuild().getId(), "insert into users (id, xp, level, coins, ticket, intro, profile, words, msg, chars, " +
                        "voicetime, reports, moderations, loginstreak, nextlogin, verifystatus, activity, banlog) values (" +
                        "'" + event.getAuthor().getId() + "', 0, 0, 0, 0, '', '', 0, 0, 0, 0, 0, 0, 0, 0, TRUE, 120, '')");
            } catch (SQLException ignored) {
            }
        }

        try {
            giveXP(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            checkLevel(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}