package util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class LevelChecker {
    public static long checker(Member member, Guild guild, long xp) {
        long level;

        if (xp <= 50000) {
            level = (int) (Math.sqrt(3 * xp + 10000) - 100) / 6;
        } else {
            level = (xp + 25000) / 1500;
        }

        String str_lvl = "0" + level;

        long display_lvl;
        if (str_lvl.length() < 3) {
            display_lvl = 0;
        } else {
            display_lvl = Integer.parseInt(str_lvl.substring(1, str_lvl.length() - 1));
        }

        String role;
        String part1 = String.valueOf(display_lvl * 10);
        String part2 = String.valueOf(display_lvl * 10 + 9);

        if (level > 499) {
            role = "Maia";
            part1 = "500";
            part2 = "\u221E";
        } else if (level > 299) {
            role = "Calaquende";
        } else if (level > 149) {
            role = "Moriquende";
        } else if (level > 49) {
            role = "Dunadan";
        } else {
            role = "Adan";
        }

        if (!member.getRoles().contains(guild.getRolesByName("Level " + part1 + " bis " + part2, true).get(0))) {
            for (int i = 0; i < 50; i++) {
                if (member.getRoles().contains(guild.getRolesByName("Level " + (i * 10) + " bis " + (i * 10 + 9), true).get(0))) {
                    try {
                        guild.removeRoleFromMember(member, guild
                                .getRolesByName("Level " + (i * 10) + " bis " + (i * 10 + 9), true).get(0)).queue();
                    } catch (Exception ignored) {
                    }
                }
            }

            if (member.getRoles().contains(guild.getRolesByName("Level 500 bis \u221E", true).get(0))) {
                try {
                    guild.removeRoleFromMember(member, guild
                            .getRolesByName("Level 500 bis \u221E", true).get(0)).queue();
                } catch (Exception ignored) {
                }
            }

            if (!member.getRoles().contains(guild.getRolesByName("BilboT-Add-Ons", true).get(0))) {
                try {
                    if (level > 499) {
                        try {
                            guild.addRoleToMember(member, guild
                                    .getRolesByName("Level 500 bis \u221E", true).get(0)).queue();
                        } catch (Exception ignored) {
                        }
                    } else {
                        try {
                            guild.addRoleToMember(member, guild
                                    .getRolesByName("Level " + (display_lvl * 10) + " bis " + (display_lvl * 10 + 9), true).get(0)).queue();
                        } catch (Exception ignored) {
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }

        if (!member.getRoles().contains(guild.getRolesByName(role, true).get(0))) {
            String[] role_names = {"Maia", "Calaquende", "Moriquende", "Dunadan", "Adan"};
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
        double level2;
        long xp2;
        long xp3;
        if (xp <= 50000L) {
            level = 3 * xp;
            level += 10000;
            level2 = Math.sqrt(Double.parseDouble(String.valueOf(level)));
            level2 = level2 - 100;
            level2 = level2 / 6;
            level = (long) (Math.ceil(level2));
        } else {
            level = (xp + 25000) / 1500;
        }

        level++;

        if (level <= 50L) {
            xp2 = Long.parseLong(String.valueOf(Math.pow(level, 2D)));
            System.out.println(xp2);
            xp2 = xp2 * 12;
            System.out.println(xp2);
            xp3 = 400 * level;
            System.out.println(xp3);
            xp2 = xp2 + xp3;
            System.out.println(xp2);
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
        long xp3;
        String rank;


        if (xp <= 50000L) {
            level = (long) (Math.ceil((Math.sqrt((3 * xp) + 10000) - 100) / 6));
        } else {
            level = (xp + 25000) / 1500;
        }


        if (level < 50L) {
            rank = "Dunadan";
            level = 50;
        } else if (level < 150) {
            rank = "Moriquende";
            level = 150;
        } else if (level < 300) {
            rank = "Calaquende";
            level = 300;
        } else if (level < 500) {
            rank = "Maia";
            level = 500;
        } else {
            rank = "max";
            level = -1;
        }

        //TODO: Make /xp next work for users with less than 50 levels

        if (level < 0L) {
            xpToRank = -1;
        } else {
            if (level <= 50) {
                xp2 = Long.parseLong(String.valueOf(Math.pow(level, 2D)));
                System.out.println(xp2);
                xp2 = xp2 * 12;
                System.out.println(xp2);
                xp3 = 400 * level;
                System.out.println(xp3);
                xp2 = xp2 + xp3;
                System.out.println(xp2);
            } else {
                xp2 = level * 1500 - 25000;
            }
            xpToRank = xp2 - xp;
        }


        return new String[]{String.valueOf(xpToRank), rank};
    }
}
