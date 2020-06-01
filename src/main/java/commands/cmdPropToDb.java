package commands;

import core.databaseHandler;
import core.permissionChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class cmdPropToDb implements Command {

    private final Properties propxp = new Properties();
    private final Properties propintro = new Properties();
    private InputStream inputxp = null;
    private OutputStream output1 = null;
    private InputStream inputintro = null;
    private OutputStream outputintro = null;

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            for (Guild g : event.getJDA().getGuilds()) {
                String gid = g.getId();
                for (Member m : g.getMembers()) {
                    String mid = m.getUser().getId();
                    try {
                        inputxp = new FileInputStream("Properties/XP/xp.properties");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        propxp.load(inputxp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String xp;
                    String level;
                    String coins;
                    String streak;
                    String nextlogin;
                    try {
                        xp = propxp.getProperty("xp_" + mid);
                        if (xp == null) {
                            xp = "0";
                        }
                    } catch (Exception ignored) {
                        xp = "0";
                    }
                    try {
                        level = propxp.getProperty("level_" + mid);
                        if (level == null) {
                            level = "0";
                        }
                    } catch (Exception e) {
                        level = "0";
                    }
                    try {
                        coins = propxp.getProperty("coins_" + mid);
                        if (coins == null) {
                            coins = "0";
                        }
                    } catch (Exception e) {
                        coins = "0";
                    }
                    try {
                        streak = propxp.getProperty("streak_" + mid);
                        if (streak == null) {
                            streak = "0";
                        }
                    } catch (Exception e) {
                        streak = "0";
                    }
                    try {
                        nextlogin = propxp.getProperty("nextlogin_" + mid);
                        if (nextlogin == null) {
                            nextlogin = "0";
                        }
                    } catch (Exception e) {
                        nextlogin = "0";
                    }

                    try {
                        inputxp.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        inputintro = new FileInputStream("Properties/UserIntros/userintro.properties");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        propintro.load(inputintro);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String intro;
                    try {
                        intro = propintro.getProperty("userintros_" + mid);
                        if (intro == null) {
                            intro = "0";
                        }
                    } catch (Exception ignored) {
                        intro = "";
                    }
                    try {
                        inputintro.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String[] args2 = {"users", "id", "'" + mid + "'", "language", "'de'", "country", "'de'",
                            "sex", "'m'", "profile", "''", "spammer", "FALSE", "verified", "FALSE"};
                    String[] args2update = {"users", "id = '" + mid + "'", "language", "'de'", "country", "'de'",
                            "sex", "'m'", "profile", "''", "spammer", "FALSE", "verified", "FALSE"};

                    String[] arguments = {"users", "id = '" + mid + "'", "1", "id"};

                    String[] args3 = {"users", "id", "'" + mid + "'", "xp", xp, "level", level, "coins", coins,
                            "ticket", "0", "intro", "'" + intro + "'", "profile", "''", "words", "0", "msg", "0", "chars", "0",
                            "voicetime", "0", "reports", "0", "moderations", "0", "loginstreak", streak, "nextlogin", nextlogin,
                            "verifystatus", "TRUE", "activity", "120", "banlog", "''"};

                    String[] args3update = {"users", "id = '" + mid + "'", "xp", xp, "level", level, "coins", coins,
                            "ticket", "0", "intro", "'" + intro + "'", "loginstreak", streak, "nextlogin", nextlogin,
                            "activity", "120"};

                    try {
                        String test = databaseHandler.database("usersettings", "select", arguments)[0];
                        if (test == null) {
                            try {
                                databaseHandler.database("usersettings", "insert", args2);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            try {
                                databaseHandler.database("usersettings", "update", args2update);
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
                        String test = databaseHandler.database(event.getGuild().getId(), "select", arguments)[0];
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
                }
            }
        }

    }


}
