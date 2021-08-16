package listeners;

import core.DatabaseHandler;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

//TODO: update and make it less stale
public class SelfJoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            DatabaseHandler.database("serversettings", "create table addons (id varchar(20), screambot boolean, music1 boolean, music2 boolean, " +
                    "music3 boolean, embed boolean, roles boolean, kirinki boolean, muede boolean, defender boolean)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DatabaseHandler.database("serversettings", "insert into addons (id, screambot, music1, music2, musci3, embed, roles, kirinki, defender) values " +
                    "'" + event.getGuild().getId() + "', FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DatabaseHandler.database(event.getGuild().getId(), "create table users (id varchar(20), xp bigint, level bigint, coins bigint, ticket bigint," +
                    " intro clob(64000), profile clob(64000), words bigint, msg bigint, chars bigint, voicetime bigint," +
                    " reports bigint, moderations bigint, loginstreak bigint, nextlogin bigint, verifystatus boolean," +
                    " activity bigint, banlog clob(64000))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DatabaseHandler.database("serversettings", "create table modules (id varchar(20), rules varchar(20), clear varchar(20), botinfo varchar(20)," +
                    " maps varchar(20), quotes varchar(20), language varchar(20), profiles varchar(20)," +
                    " joining varchar(20), leaving varchar(20), xp varchar(20), event varchar(20)," +
                    " voicechannel varchar(20), quiz varchar(20), search varchar(20), report varchar(20)," +
                    " voice varchar(20), chatfilter varchar(20), welcome varchar(20), verification varchar(20)," +
                    " lottery varchar(20), voicelog varchar(20), modlog varchar(20), log varchar(20)," +
                    " spam varchar(20), intro varchar(20), addons varchar(20), activity varchar(20))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] insertArgs = {"modules", "id", "'" + event.getGuild().getId() + "'", "rules", "'activated'", "clear",
                "'activated'", "botinfo", "'deactivated'", "maps", "'deactivated'", "quotes", "'deactivated'", "language",
                "'activated'", "profiles", "'activated'", "joining", "'activated'", "leaving", "'activated'", "xp", "'activated'",
                "event", "'deactivated'", "voicechannel", "'deactivated'", "quiz", "'deactivated'", "search", "'activated'",
                "report", "'deactivated'", "voice", "'deactivated'", "chatfilter", "'deactivated'", "welcome", "'activated'",
                "verification", "'deactivated'", "lottery", "'deactivated'", "voicelog", "'deactivated'", "modlog",
                "'activated'", "log", "'deactivated'", "spam", "'activated'", "intro", "'deactivated'", "addons",
                "'activated'", "activity", "'deactivated'"};

    }
}