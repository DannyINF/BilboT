package util;

import core.DatabaseHandler;
import core.PermissionChecker;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.SQLException;

public class GiveXP {
    //TODO: comment
    public static void giveXPToMember(Member member, Guild guild, long amount) throws SQLException {
        if (amount != 0 && !PermissionChecker.checkRole(STATIC.getCam(), member)) {
            long xp;
            double userboost = 1; // Double-XP Event
            double channelboost = 1;
            double serverboost = 1;

            if (STATIC.is2x)
                userboost = 2;

            Role nitro = null;

            for (Role r : guild.getRolesByName("Nitro Booster", true)) {
                if (r.isManaged()) {
                    nitro = r;
                }
            }

            if (member.getRoles().contains(nitro)) {
                userboost = 1.5;
            }

            xp = (long) (serverboost * channelboost * userboost * amount);

            DatabaseHandler.database(guild.getId(), "update users set xp = xp + " + xp + " where id = '" + member.getId() + "'");

        }
    }
}