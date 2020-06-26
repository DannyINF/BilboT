package util;

import core.databaseHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.sql.SQLException;

import static java.lang.Math.sqrt;

public class voiceXP {
    public static void giveVoiceXP(JDA jda) throws SQLException {
        for (Guild guild : jda.getGuilds()) {
            //if (isReady.isReady(guild)) {
            for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
                double membercount = voiceChannel.getMembers().size();
                for (Member m : voiceChannel.getMembers()) {
                    if (m.getUser().isBot() || core.permissionChecker.checkRole(STATIC.getCam(), m)) {
                        membercount--;
                    }
                }
                if (membercount == 1) {
                    membercount = 0;
                }
                double channelboost = 1;

                for (Member member : voiceChannel.getMembers()) {
                    long xp;

                    xp = (long) (sqrt(2520 * membercount - 671) / 9 - 43 / 9);

                    giveXP.giveXPToMember(member, guild, xp);

                    //statistics
                    if (membercount>1) {
                        databaseHandler.database(guild.getId(), "update users set voicetime = voicetime + 1 where id = '" + member.getId() + "'");
                    }
                }
            }
            //}
        }
    }
}
