package listeners;

import core.databaseHandler;
import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.giveXP;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class xpListener extends ListenerAdapter {

    /**
     * @param event MessageReceivedEvent
     */
    private static void giveXP(MessageReceivedEvent event) throws SQLException {
        int xp;
        int amount = event.getMessage().getContentRaw().length();

        // only the first 140 characters count
        if (amount < 140) {
            xp = amount;
        } else {
            xp = 140;
        }

        try {
            event.getMessage().getAttachments().get(0);
            xp += 15;
        } catch (Exception ignored) {
        }

        // different multiplicators for different channels
        String channelname = event.getTextChannel().getName();
        String channeltopic;
        try {
            channeltopic = event.getTextChannel().getTopic();
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

        if (event.getGuild().getCategoriesByName("\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550| INFO |\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550", true).get(0).getTextChannels().contains(event.getTextChannel())) {
            xp = 0;
        }

        giveXP.giveXPToMember(event.getMember(), event.getGuild(), xp);
    }
    //}

    /**
     * @param event MessageReceivedEvent
     */
    private static void checkLevel(MessageReceivedEvent event) throws SQLException {
        long currentlevel;
        long currentXp;

        // getting level and xp of the user
        String[] arguments = {"users", "id = '" + event.getAuthor().getId() + "'", "2", "xp", "level"};
        String[] answer;

        answer = databaseHandler.database(event.getGuild().getId(), "select", arguments);

        try {
            assert answer != null;
            currentXp = Integer.parseInt(answer[0]);
        } catch (Exception e) {
            currentXp = 0;
        }
        try {
            currentlevel = Integer.parseInt(answer[1]);
        } catch (Exception e) {
            currentlevel = 0;
        }

        long newlevel = util.LevelChecker.checker(event.getMember(), event.getGuild(), currentXp);

        // if your current xp are bigger than the xp needed for the next level you receive a level up
        if (newlevel != currentlevel) {

            if (newlevel > currentlevel) {
                String[] arguments3 = {"users", "id = '" + Objects.requireNonNull(event.getMember()).getUser().getId() + "'", "1", "activity"};
                String[] answer3 = null;
                try {
                    answer3 = databaseHandler.database(event.getGuild().getId(), "select", arguments3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                assert answer3 != null;
                long newActivity = Long.parseLong(answer3[0]) + 5;

                String[] arguments2 = {"users", "id = '" + event.getMember().getUser().getId() + "'", "activity", String.valueOf(newActivity)};
                try {
                    databaseHandler.database(event.getGuild().getId(), "update", arguments2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            int coins;

            String[] arguments2 = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "coins"};
            String[] answer2;
            answer2 = databaseHandler.database(event.getGuild().getId(), "select", arguments2);

            try {
                assert answer2 != null;
                coins = Integer.parseInt(answer2[0]);
            } catch (Exception e) {
                coins = 0;
            }

            //adding the coins received through level-up to the total coins-count
            long newCoins;
            if (newlevel > 50) {
                newCoins = (25 * (newlevel - currentlevel)) + coins;
            } else {
                int sum = 0;
                if (newlevel < currentlevel) {
                    for (long i = currentlevel; i > newlevel; i--) {
                        sum += i / 2;
                    }
                } else {
                    for (long i = currentlevel; i < newlevel; i++) {
                        sum += i / 2;
                    }
                }
                newCoins = coins + sum;
            }

            String[] arguments3 = {"users", "id = '" + event.getAuthor().getId() + "'", "coins", String.valueOf(newCoins)};
            databaseHandler.database(event.getGuild().getId(), "update", arguments3);

            // creating level-up msg
            if (!event.getAuthor().isBot()) {

                Message msg = event.getTextChannel().sendMessage(messageActions.getLocalizedString("xp_level_up", "user", event.getAuthor().getId())
                        .replace("[USER]", event.getAuthor().getAsMention()).replace("[LEVEL]", String.valueOf(newlevel))).complete();
                // msg deletes itself after 15000 milliseconds
                if (!(newlevel == 50 || newlevel == 150 || newlevel == 300 || newlevel == 500)) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 15000);
                }

            }
        }
        // storing level
        String[] arguments4 = {"users", "id = '" + event.getAuthor().getId() + "'", "level", String.valueOf(newlevel)};
        databaseHandler.database(event.getGuild().getId(), "update", arguments4);
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //if (isReady.isReady(event.getGuild())) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] arguments = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "id"};
        String[] args2 = {"users", "id", "'" + event.getAuthor().getId() + "'", "language", "'en'", "country", "'gb'",
                "sex", "'m'", "profile", "''", "spammer", "FALSE", "FALSE"};
        String[] args3 = {"users", "id", "'" + event.getAuthor().getId() + "'", "xp", "0", "level", "0", "coins", "0",
                "ticket", "0", "intro", "''", "profile", "''", "words", "0", "msg", "0", "chars", "0",
                "voicetime", "0", "reports", "0", "moderations", "0", "loginstreak", "0", "nextlogin", "0",
                "verifystatus", "TRUE", "activity", "120", "banlog", "''"};
        try {
            String test = Objects.requireNonNull(databaseHandler.database("usersettings", "select", arguments))[0];
            if (test == null) {
                try {
                    databaseHandler.database("usersettings", "insert", args2);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            try {
                databaseHandler.database("usersettings", "insert", args2);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        try {
            String test = Objects.requireNonNull(databaseHandler.database(event.getGuild().getId(), "select", arguments))[0];
            if (test == null) {
                try {
                    databaseHandler.database(event.getGuild().getId(), "insert", args3);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            try {
                databaseHandler.database(event.getGuild().getId(), "insert", args3);
            } catch (SQLException ignored) {
            }
        }

        String status = "deactivated";

        try {
            status = modulesChecker.moduleStatus("xp", event.getGuild().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (status.equals("activated")) {

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
}