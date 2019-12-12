package listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

//TODO: Check this!
public class selfJoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //while (!isReady.isReady(event.getGuild())) {
        //try {
        //     Thread.sleep(10);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        //}
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] createArgs3 = {"addons", "id varchar(20)", "screambot boolean", "music1 boolean",
                "music2 boolean", "music3 boolean", "embed boolean", "roles boolean", "kirinki boolean", "muede boolean", "defender boolean"};
        try {
            core.databaseHandler.database("serversettings", "create", createArgs3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] insertArgs2 = {"addons", "id", "'" + event.getGuild().getId() + "'", "screambot", "FALSE", "music1", "FALSE",
                "music2", "FALSE", "music3", "FALSE", "embed", "FALSE", "roles", "FALSE", "kirinki", "FALSE", "defender", "FALSE"};
        try {
            core.databaseHandler.database("serversettings", "insert", insertArgs2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] createArgs = {"users", "id varchar(20)", "xp bigint", "level bigint", "coins bigint", "ticket bigint",
                "intro clob(64000)", "profile clob(64000)", "words bigint", "msg bigint", "chars bigint", "voicetime bigint",
                "reports bigint", "moderations bigint", "loginstreak bigint", "nextlogin bigint", "verifystatus boolean",
                "activity bigint", "banlog clob(64000)"};
        try {
            core.databaseHandler.database(event.getGuild().getId(), "create", createArgs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] createArgs2 = {"modules", "id varchar(20)", "rules varchar(20)", "clear varchar(20)", "botinfo varchar(20)",
                "maps varchar(20)", "quotes varchar(20)", "language varchar(20)", "profiles varchar(20)",
                "joining varchar(20)", "leaving varchar(20)", "xp varchar(20)", "event varchar(20)",
                "voicechannel varchar(20)", "quiz varchar(20)", "search varchar(20)", "report varchar(20)",
                "voice varchar(20)", "chatfilter varchar(20)", "welcome varchar(20)", "verification varchar(20)",
                "lottery varchar(20)", "voicelog varchar(20)", "modlog varchar(20)", "log varchar(20)",
                "spam varchar(20)", "intro varchar(20)", "addons varchar(20)", "activity varchar(20)"};
        try {
            core.databaseHandler.database("serversettings", "create", createArgs2);
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
        try {
            core.databaseHandler.database("serversettings", "insert", insertArgs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs4 = {"msgs", "id varchar(20)", "language varchar(20)", "country varchar(20)", "welcomeserver clob(2000)",
                "welcomepriv clob(2000)", "leaving clob(2000)", "ban clob(2000)", "kick clob(2000)", "rules1 clob(2000)",
                "rules2 clob(2000)", "rules3 clob(2000)", "rules4 clob(2000)", "rules5 clob(2000)", "rules6 clob(2000)",
                "rules7 clob(2000)", "rules8 clob(2000)", "rules9 clob(2000)", "rules10 clob(2000)", "rules11 clob(2000)",
                "rules12 clob(2000)", "rules13 clob(2000)", "rules14 clob(2000)", "rules15 clob(2000)", "rules16 clob(2000)",
                "rules17 clob(2000)", "rules18 clob(2000)", "rules19 clob(2000)", "rules20 clob(2000)"};

        try {
            core.databaseHandler.database("serversettings", "create", createArgs4);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs5 = {"msgs", "id", "'" + event.getGuild().getId() + "'",
                "language", "'en'", "country", "'gb'", "welcomeserver", "''", "welcomepriv", "''", "leaving", "''",
                "ban", "''", "kick", "''", "rules1", "''", "rules2", "''", "rules3", "''", "rules4", "''", "rules5",
                "''", "rules6", "''", "rules7", "''", "rules8", "''", "rules9", "''", "rules10", "''", "rules11",
                "''", "rules12", "''", "rules13", "''", "rules14", "''", "rules15", "''", "rules16", "''", "rules17",
                "''", "rules18", "''", "rules19", "''", "rules20"};

        try {
            core.databaseHandler.database("serversettings", "insert", createArgs5);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs7 = {"ranks", "id varchar(20)", "rank1 varchar(20)", "rank1_color varchar(7)", "rank2 varchar(20)",
                "rank2_color varchar(7)", "rank3 varchar(20)", "rank3_color varchar(7)", "rank4 varchar(20)",
                "rank4_color varchar(7)", "rank5 varchar(20)", "rank5_color varchar(7)", "owner varchar(20)",
                "owner_color varchar(7)", "admin varchar(20)", "admin_color varchar(7)", "moderator varchar(20)",
                "moderator_color varchar(7)", "supporter varchar(20)", "support_color varchar(7)", "custom clob(64000)"};

        try {
            core.databaseHandler.database("serversettings", "create", createArgs7);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs10 = {"ranks", "id", "'" + event.getGuild().getId() + "'", "rank1", "''", "rank1_color", "''", "rank2", "''",
                "rank2_color", "''", "rank3", "''", "rank3_color", "''", "rank4", "''",
                "rank4_color", "''", "rank5", "''", "rank5_color", "''", "owner", "''",
                "owner_color", "''", "admin", "''", "admin_color", "''", "moderator", "''",
                "moderator_color", "''", "supporter", "''", "support_color", "''", "custom", "''"};

        try {
            core.databaseHandler.database("serversettings", "insert", createArgs10);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs9 = {"channels", "id varchar(20)", "spam varchar(20)", "modlog varchar(20)", "log varchar(20)", "voicelog varchar(20)", "cmdlog varchar(20)"};

        try {
            core.databaseHandler.database("serversettings", "create", createArgs9);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] insertArgs9 = {"channels", "id", "'" + event.getGuild().getId() + "'", "spam", "''", "modlog", "''",
                "log", "''", "voicelog", "''", "cmdlog", "''"};

        try {
            core.databaseHandler.database("serversettings", "insert", insertArgs9);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs6 = {"users", "id varchar(20)", "language varchar(20)", "country varchar(20)", "sex varchar(20)",
                "profile clob(64000)", "spammer boolean", "verified boolean"};

        try {
            core.databaseHandler.database("usersettings", "create", createArgs6);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs8 = {"events", "id varchar(20)", "narration clob(64000)"};

        try {
            core.databaseHandler.database("serversettings", "create", createArgs8);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] createArgs12 = {"events", "id", "'" + event.getGuild().getId() + "'", "narration", "''"};

        try {
            core.databaseHandler.database("serversettings", "insert", createArgs12);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Permission[] channelperm = new Permission[]{Permission.VIEW_CHANNEL, Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE};
        TextChannel tc = event.getGuild().createTextChannel("bilbot-communication-channel").setTopic("Please mute this channel!").complete();
        String[] ids = {"393375474056953856", "454613079804608522", "486085339530788894", "486085339530788894", "486089728437780480", "447320058918600714", "467696459941412884", "476175371809587200", "466006333624156170"};
        for (String id : ids) {
            try {
                tc.createPermissionOverride(Objects.requireNonNull(event.getGuild().getMemberById(id))).setAllow(channelperm).queue();
            } catch (Exception ignored) {
            }
        }

        tc.createPermissionOverride(event.getGuild().getPublicRole()).setDeny(channelperm).queue();
    }
}