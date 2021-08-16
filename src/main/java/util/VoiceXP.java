package util;

import core.DatabaseHandler;
import core.PermissionChecker;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.sql.SQLException;

import static java.lang.Math.sqrt;

public class VoiceXP {
    //TODO: comment
    public static void giveVoiceXP(JDA jda) throws SQLException {
        for (Guild guild : jda.getGuilds()) {
            for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
                double membercount = voiceChannel.getMembers().size();
                for (Member m : voiceChannel.getMembers()) {
                    if (m.getUser().isBot() || PermissionChecker.checkRole(STATIC.getCam(), m)) {
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

                    GiveXP.giveXPToMember(member, guild, xp);

                    //statistics
                    if (membercount>1) {
                        DatabaseHandler.database(guild.getId(), "update users set voicetime = voicetime + 1 where id = '" + member.getId() + "'");
                    }
                }
            }
        }
    }
}
