package util;

import core.databaseHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.sql.SQLException;

public class voiceXP {
    public static void giveVoiceXP(JDA jda) throws SQLException, InterruptedException {
        for (Guild guild : jda.getGuilds()) {
            //if (isReady.isReady(guild)) {
            Thread.sleep(1000);
            for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
                double membercount = voiceChannel.getMembers().size();
                for (Member m : voiceChannel.getMembers()) {
                    if (m.getUser().isBot()) {
                        membercount--;
                    }
                }
                if (membercount == 1) {
                    membercount = 0;
                }
                double channelboost = 1;

                for (Member member : voiceChannel.getMembers()) {
                    long xp;

                    if (membercount < 9) {
                        xp = (long) (membercount * 2.5 * channelboost);
                    } else {
                        xp = (long) (20 * channelboost);
                    }

                    giveXP.giveXPToMember(member, guild, xp);

                    //statistics
                    if (membercount>1) {
                        String[] arguments1 = {"users", "id = '" + member.getUser().getId() + "'", "1", "voicetime"};
                        String[] answer1;
                        answer1 = databaseHandler.database(guild.getId(), "select", arguments1);
                        int voicetime;
                        try {
                            assert answer1 != null;
                            voicetime = Integer.parseInt(answer1[0]);
                        } catch (Exception e) {
                            voicetime = 0;
                        }

                        int newVoicetime = voicetime + 1;

                        String[] arguments3 = {"users", "id = '" + member.getUser().getId() + "'", "voicetime", String.valueOf(newVoicetime)};
                        databaseHandler.database(guild.getId(), "update", arguments3);
                    }

                }
            }
            //}
        }
    }
}
