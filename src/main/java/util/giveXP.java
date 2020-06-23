package util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.javatuples.Triplet;

import java.sql.SQLException;

public class giveXP {
    public static void giveXPToMember(Member member, Guild guild, long amount) throws SQLException {
        if (amount != 0 && !core.permissionChecker.checkRole(STATIC.getCam(), member)) {
            long currentXp;
            long xp;
            double userboost = 1; // Double-XP Event
            double channelboost = 1;
            double serverboost = 1;

            Role nitro = null;

            for (Role r : guild.getRolesByName("Nitro Booster", true)) {
                if (r.isManaged()) {
                    nitro = r;
                }
            }

            if (member.getRoles().contains(nitro)) {
                userboost = 1.5;
            }

            Triplet answer = STATIC.getExperienceUser(member.getId(), guild.getId());

            try {
                currentXp = (Long) answer.getValue0();
            } catch (Exception e) {
                currentXp = 0;
            }

            xp = (long) (serverboost * channelboost * userboost * amount);

            //adding the xp of the msg to the total xp of the user
            STATIC.updateExperienceUser(member.getId(), guild.getId(), xp, 0L, 0L);
            long newXP = currentXp + xp;

            //store the total xp of the user
            STATIC.exec.execute(() -> {
                try {
                    core.databaseHandler.database(guild.getId(), "update users set xp = " + newXP + " where id = '" + member.getId() + "'");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
