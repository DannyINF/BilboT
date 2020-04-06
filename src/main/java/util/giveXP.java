package util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.SQLException;

public class giveXP {
    public static void giveXPToMember(Member member, Guild guild, long amount) throws SQLException {
        if (amount != 0) {
            String[] arguments = {"users", "id = '" + member.getUser().getId() + "'", "1", "xp"};
            String[] answer;
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

            answer = core.databaseHandler.database(guild.getId(), "select", arguments);

            try {
                assert answer != null;
                currentXp = Integer.parseInt(answer[0]);
            } catch (Exception e) {
                currentXp = 0;
            }

            xp = (long) (serverboost * channelboost * userboost * amount);

            //adding the xp of the msg to the total xp of the user
            long newXP = currentXp + xp;

            //store the total xp of the user
            String[] arguments2 = {"users", "id = '" + member.getUser().getId() + "'", "xp", String.valueOf(newXP)};
            core.databaseHandler.database(guild.getId(), "update", arguments2);
        }
    }
}
