package util;

import core.DatabaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class LevelChecker {
    public static long checker(Member member, Guild guild) throws SQLException {
        long xp = Long.parseLong(Objects.requireNonNull(DatabaseHandler.database(guild.getId(), "select xp from users where id = '" + member.getId() + "'"))[0]);
        long level;

        if (xp <= 50000) {
            level = (int) (Math.sqrt(3 * xp + 10000) - 100) / 6;
        } else {
            level = (xp + 25000) / 1500;
        }

        String str_lvl = "0" + level;

        long display_lvl;
        if (str_lvl.length() < 3)
            display_lvl = 0;
        else
            display_lvl = Integer.parseInt(str_lvl.substring(1, str_lvl.length() - 1));

        String role;
        String part1 = String.valueOf(display_lvl * 10);
        String part2 = String.valueOf(display_lvl * 10 + 9);

        if (level > 1049) {
            role = "Maia";
            part1 = "1050";
            part2 = "\u221E";
        } else if (level > 749)
            role = "Calaquende";
        else if (level > 499)
            role = "Moriquende";
        else if (level > 299)
          role = "Peredhel";
        else if (level > 149)
          role = "D\u00fanadan";
        else if (level > 49)
          role = "Adan";
        else if (level > 9)
          role = "Hobbit";
        else
          role = "Dr\u00faadan";


        if (!member.getRoles().contains(guild.getRolesByName("Level " + part1 + " bis Level " + part2, true).get(0))) {
            List<Role> memberroles = member.getRoles();
            for (int i = 0; i < 105; i++) {
                if (memberroles.contains(guild.getRolesByName("Level " + (i * 10) + " bis Level " + (i * 10 + 9), true).get(0))) {
                    try {
                        guild.removeRoleFromMember(member, guild
                                .getRolesByName("Level " + (i * 10) + " bis Level " + (i * 10 + 9), true).get(0)).queue();
                    } catch (Exception ignored) {
                    }
                }
            }

            if (member.getRoles().contains(guild.getRolesByName("Level 1050 bis Level \u221E", true).get(0))) {
                try {
                    guild.removeRoleFromMember(member, guild
                            .getRolesByName("Level 1050 bis Level \u221E", true).get(0)).queue();
                } catch (Exception ignored) {
                }
            }

            if (!member.getRoles().contains(guild.getRolesByName("BilboT-Add-Ons", true).get(0))) {
                try {
                    if (level > 1049) {
                        try {
                            guild.addRoleToMember(member, guild
                                    .getRolesByName("Level 1050 bis Level \u221E", true).get(0)).queue();
                        } catch (Exception ignored) {
                        }
                    } else {
                        try {
                            guild.addRoleToMember(member, guild
                                    .getRolesByName("Level " + (display_lvl * 10) + " bis Level " + (display_lvl * 10 + 9), true).get(0)).queue();
                        } catch (Exception ignored) {
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }

        if (!member.getRoles().contains(guild.getRolesByName(role, true).get(0))) {
            String[] role_names = {"Maia", "Calaquende", "Moriquende", "Peredhel", "D\u00fanadan", "Adan", "Hobbit", "Dr\u00faadan"};
            for (String role_name : role_names) {
                if (member.getRoles().contains(guild.getRolesByName(role_name, true).get(0))) {
                    try {
                        guild.removeRoleFromMember(member, guild
                                .getRolesByName(role_name, true).get(0)).queue();
                    } catch (Exception ignored) {
                    }
                }
            }
            if (!member.getRoles().contains(guild.getRolesByName("BilboT-Add-Ons", true).get(0))) {
                try {
                    guild.addRoleToMember(member, guild
                            .getRolesByName(role, true).get(0)).queue();
                } catch (Exception ignored) {
                }
            }
        }
        return level;
    }

    public static long nextLevel(long xp) {
        long xpToLevel;
        long level;
        long xp2;
        if (xp <= 50000L) {
            level = (int) (Math.sqrt(3 * xp + 10000) - 100) / 6;
        } else {
            level = (xp + 25000) / 1500;
        }

        level++;

        if (level <= 50L) {
            xp2 = ((((level * 6) + 100) * ((level * 6) + 100)) - 10000) / 3;
        } else {
            xp2 = level * 1500 - 25000;
        }

        xpToLevel = xp2 - xp;

        return xpToLevel;
    }

    public static String[] nextRank(long xp) {
        long xpToRank;
        long level;
        long xp2;
        String rank;


        if (xp <= 50000L) {
            level = (long) (Math.ceil((Math.sqrt((3 * xp) + 10000) - 100) / 6));
        } else {
            level = (xp + 25000) / 1500;
        }


        if (level < 10) {
            rank = "Hobbit";
            level = 10;
        } else if (level < 50) {
            rank = "Adan";
            level = 50;
        } else if (level < 150) {
            rank = "D\u00fanadan";
            level = 150;
        } else if (level < 300) {
            rank = "Peredhel";
            level = 300;
        } else if (level < 500) {
            rank = "Moriquende";
            level = 500;
        } else if (level < 750) {
            rank = "Calaquende";
            level = 750;
        } else if (level < 1050) {
            rank = "Maia";
            level = 1050;
        } else {
            rank = "H\u00f6chster Rang erreicht!";
            level = -1;
        }

        if (level < 0L)
            xpToRank = -1;
        else {
            if (level == 10)
                xp2 = 5200L;
            else if (level == 50)
                xp2 = 50000L;
            else
                xp2 = level * 1500 - 25000;
            xpToRank = xp2 - xp;
        }

        return new String[]{String.valueOf(xpToRank), rank};
    }
}
