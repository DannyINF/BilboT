package util;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ActivityChecker {
    public void activity(JDA jda) throws SQLException {
        for (Guild guild : jda.getGuilds()) {

            Role role1 = null;
            Role role2 = null;
            Role role3 = null;
            Role role4 = null;
            Role role5 = null;
            Role role6 = null;
            Role role7 = null;
            Role role8 = null;
            Role role9 = null;
            Role role10 = null;
            Role role11 = null;

            try {
                role1 = guild.getRolesByName("Ilúvatar", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role2 = guild.getRolesByName("Vala", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role4 = guild.getRolesByName("Maia", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role5 = guild.getRolesByName("Calaquende", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role6 = guild.getRolesByName("Moriquende", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role7 = guild.getRolesByName("Peredhel", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role8 = guild.getRolesByName("Dúnadan", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role9 = guild.getRolesByName("Adan", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role10 = guild.getRolesByName("Hobbit", true).get(0);
            } catch (Exception ignored) {
            }
            try {
                role11 = guild.getRolesByName("Drúadan", true).get(0);
            } catch (Exception ignored) {
            }

            for (Member member : guild.getMembers()) {
                if (!member.getUser().isBot() && !member.getRoles().contains(guild.getRolesByName("Vacation", true).get(0))
                        && !member.getRoles().contains(guild.getRolesByName("YouTuber", true).get(0))) {
                    String[] answer;
                    long oldActivity;
                    try {
                        answer = DatabaseHandler.database(guild.getId(), "select activity from users where id = '" + member.getId() + "'");
                        if (answer == null)
                            oldActivity = 120L;
                        else
                            oldActivity = Long.parseLong(answer[0]);
                    } catch (Exception e) {
                        oldActivity = 120L;
                    }

                    List<Role> roles = member.getRoles();

                    if (!roles.contains(role1) && !roles.contains(role2)) {
                        if (roles.contains(role3))
                            if (oldActivity > 3600)
                                oldActivity = 3600;
                        else if (roles.contains(role4))
                            if (oldActivity > 1500)
                                oldActivity = 1500;
                        else if (roles.contains(role5))
                            if (oldActivity > 1100)
                                oldActivity = 1100;
                        else if (roles.contains(role6))
                            if (oldActivity > 720)
                                oldActivity = 720;
                        else if (roles.contains(role7))
                            if (oldActivity > 600)
                                oldActivity = 600;
                        else if (roles.contains(role8))
                            if (oldActivity > 480)
                                oldActivity = 480;
                        else if (roles.contains(role9))
                            if (oldActivity > 320)
                                oldActivity = 320;
                        else if (roles.contains(role10))
                            if (oldActivity > 240)
                                oldActivity = 240;
                        else if (roles.contains(role11))
                            if (oldActivity > 160)
                                oldActivity = 160;
                    }

                    long newActivity = oldActivity - 4;

                    DatabaseHandler.database(guild.getId(), "update users set activity = " + newActivity + " where id = '" + member.getId() + "'");

                    SET_CHANNEL set_channel = CHANNEL.getSetChannel("modlog", guild.getId());
                    TextChannel modlog = guild.getTextChannelById(set_channel.getChannel());

                    if (newActivity < 1) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.RED);
                        embed.setTitle("Kick!");
                        embed.setDescription("Du wurdest aufgrund von Inaktivit\u00e4t vom Server **" + guild.getName() + "** gekickt. Wenn du wieder joinen willst, findest du hier eine Einladung: " + STATIC.getInvite(guild));
                        embed.setThumbnail(guild.getIconUrl());

                        EmbedBuilder embed1 = new EmbedBuilder();
                        embed1.setColor(Color.RED);
                        embed1.setTitle("Kick!");
                        embed1.setDescription(member.getAsMention() + " wurde aufgrund von Inaktivit\u00e4t gekickt.");
                        assert modlog != null;
                        modlog.sendMessageEmbeds(embed1.build()).queue();

                        PrivateChannel channel = member.getUser().openPrivateChannel().complete();
                        channel.sendMessageEmbeds(embed.build()).queue();
                        guild.kick(member, "Inaktivit\u00e4t").queue();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else if (newActivity < 30 && newActivity > 25) {

                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.RED);
                        embed.setTitle("Vorsicht!");
                        embed.setDescription("Aufgrund deiner Inaktivit\u00e4t k\u00f6nnte es zeitnah zum Kick vom Server **" + guild.getName() + "** kommen!");
                        embed.setThumbnail(guild.getIconUrl());

                        EmbedBuilder embed1 = new EmbedBuilder();
                        embed1.setColor(Color.RED);
                        embed1.setTitle("Verwarnung f\u00fcr Inaktivit\u00e4t");
                        embed1.setDescription(member.getUser().getAsTag() + " wurde aufgrund von Inaktivit\u00e4t verwarnt.");

                        assert modlog != null;
                        modlog.sendMessageEmbeds(embed1.build()).queue();

                        PrivateChannel channel = member.getUser().openPrivateChannel().complete();
                        channel.sendMessageEmbeds(embed.build()).queue();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
