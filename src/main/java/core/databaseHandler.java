package core;

import java.sql.*;
import java.util.ArrayList;

public class databaseHandler {

    //TODO: simplify the handler and make it more viable for different commands

    /**
     * Databases:
     * <p>
     * === without data types ===
     * <p>
     * [SERVERID]
     * - users(id, xp, level, coins, ticket, intro, profile, words, msg, chars, voicetime, reports, moderations, loginstreak,
     * nextlogin, verifystatus, activity, banlog)
     * <p>
     * serversettings
     * - addons(id, screambot, music1, music2, music3, embed, roles, kirinki, muede)
     * <p>
     * - modules(id, rules, clear, botinfo, maps, quotes, language, profiles, joining, leaving, xp, event, voicechannel, quiz,
     * search, report, voice, chatfilter, welcome, verification, lottery, voicelog, modlog, log, spam, intro, addons,
     * activity)
     * - msgs(id, language, country, welcomeserver, welcomepriv, leaving, ban, kick,
     * rules1, rules2, rules3, rules4, rules5, rules6, rules7, rules8, rules9, rules10, rules11, rules12,
     * rules13, rules14, rules15, rules16, rules17, rules18, rules19, rules20)
     * - ranks(id, rank1, rank2, rank3, rank4, rank5, owner, admin, moderator, supporter, custom)  -> #123456=RankName
     * - channels(id, spam, modlog, log, voicelog, cmdlog)
     * - events(id, narration)
     * <p>
     * usersettings
     * - users(id, language, country, sex, profile, spammer, verified)
     * <p>
     * <p>
     * === with data types ===
     * <p>
     * [SERVERID]
     * <p>
     * - users(id varchar(20), xp bigint, level bigint, coins bigint, ticket bigint, intro clob(64000), profile clob(64000),
     * words bigint, msg bigint, chars bigint, voicetime bigint, report bigint, moderations bigint, loginstreak bigint,
     * nextlogin bigint, verifystatus boolean, activity bigint, banlog clob(64000))
     * <p>
     * serversettings
     * - addons(id varchar(20), screambot boolean, music1 boolean, music2 boolean, music3 boolean, embed boolean,
     * roles boolean, kirinki boolean, muede boolean)
     * <p>
     * - modules(id varchar(20), rules varchar(20), clear varchar(20), botinfo varchar(20), maps varchar(20), quotes varchar(20),
     * language varchar(20), profiles varchar(20), joining varchar(20) leaving varchar(20), xp varchar(20), event varchar(20),
     * voicechannel varchar(20), quiz varchar(20), search varchar(20), report varchar(20), voice varchar(20),
     * chatfilter varchar(20), welcome varchar(20), verification varchar(20), lottery varchar(20), voicelog varchar(20),
     * modlog varchar(20), log varchar(20), spam varchar(20), intro varchar(20), addons varchar(20), activity varchar(20))
     * - msgs(id varchar(20), language varchar(20), country varchar(20), welcomeserver clob(2000), welcomepriv clob(2000), leaving clob(2000),
     * ban clob(2000), kick clob(2000), rules1 clob(2000), rules2 clob(2000), rules3 clob(2000), rules4 clob(2000),
     * rules5 clob(2000), rules6 clob(2000), rules7 clob(2000), rules8 clob(2000), rules9 clob(2000), rules10 clob(2000),
     * rules11 clob(2000), rules12 clob(2000), rules13 clob(2000), rules14 clob(2000), rules15 clob(2000),
     * rules16 clob(2000), rules17 clob(2000), rules18 clob(2000), rules19 clob(2000), rules20 clob(2000))
     * - ranks(id varchar(20), rank1 varchar(20), rank1_color varchar(7), rank2 varchar(20), rank2_color varchar(7),
     * rank3 varchar(20), rank3_color varchar(7), rank4 varchar(20), rank4_color varchar(7), rank5 varchar(20),
     * rank5_color varchar(7), owner varchar(20), owner_color varchar(7), admin varchar(20), admin_color varchar(7),
     * moderator varchar(20), moderator_color varchar(7), supporter varchar(20), support_color varchar(7), custom clob(64000)) //TODO: fully implement custom
     * - channels(id varchar(20), spam varchar(20), modlog varchar(20), log varchar(20), voicelog varchar(20), cmdlog varchar(20))
     * - events(id varchar(20), narration clob(2000))
     * <p>
     * usersettings
     * - users(id varchar(20), language varchar(20), country varchar(20), sex varchar(20), profile clob(64000), spammer boolean, verified boolean)
     */
    private Connection conn;

    /**
     * @param database input the database you want to address
     * @param usage    give your choice of action
     * @param args     arguments that are used in later functions
     */
    public static String[] database(String database, String usage, String[] args) throws SQLException {
        databaseHandler app = new databaseHandler();

        app.connectionToDerby(database);

        switch (usage) {
            case "create":
                app.createDbUsage(args);
                break;
            case "insert":
                app.insertDbUsage(args);
                break;
            case "update":
                app.updateDbUsage(args);
                break;
            case "drop":
                app.dropDbUsage(args);
                break;
            case "alter":
                app.alterDbUsage(args);
                break;
            case "select":
                return app.selectDbUsage(args);
        }
        return null;
    }

    private void connectionToDerby(String database) throws SQLException {
        // -------------------------------------------
        // URL format is
        // jdbc:derby:<local directory to save data>
        // -------------------------------------------
        String dbUrl = "jdbc:derby:Data/" + database + ";create=true";
        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        conn = DriverManager.getConnection(dbUrl);
    }

    /**
     * @param args 0: table name; 1: row name; 2: data type
     */
    private void alterDbUsage(String[] args) throws SQLException {
        Statement statement = conn.createStatement();
        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (i < args.length - 3) {
            sb.append(args[i]);
            sb.append(", ");
            i += 2;
        }
        sb.append(args[i]);
        StringBuilder sb2 = new StringBuilder();
        int k = 2;
        while (k < args.length - 2) {
            sb2.append(args[k]);
            sb2.append(", ");
            k += 2;
        }
        sb2.append(args[k]);
        statement.executeUpdate("alter table " + args[0] + " add column " + args[1] + " " + args[2]);
    }

    /**
     * @param args 0: table name; 1+: values
     */
    private void insertDbUsage(String[] args) throws SQLException {
        Statement statement = conn.createStatement();


        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (i < args.length - 3) {
            sb.append(args[i]);
            sb.append(", ");
            i += 2;
        }
        sb.append(args[i]);
        StringBuilder sb2 = new StringBuilder();
        int k = 2;
        while (k < args.length - 2) {
            sb2.append(args[k]);
            sb2.append(", ");
            k += 2;
        }
        sb2.append(args[k]);
        statement.executeUpdate("insert into " + args[0] + "(" + sb.toString() + ") values (" + sb2.toString() + ")");
    }

    /**
     * @param args 0: name of table; 1+: arguments for table
     */
    private void createDbUsage(String[] args) throws SQLException {
        Statement statement = conn.createStatement();

        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (i < args.length - 1) {
            sb.append(args[i]);
            sb.append(", ");
            i++;
        }
        sb.append(args[i]);
        statement.executeUpdate("Create table " + args[0] + " (" + sb.toString() + ")");

        statement.executeQuery("SELECT * FROM " + args[0]);


    }

    /**
     * @param args 0: target table; 1: condition; 2+: changes;
     */
    private void updateDbUsage(String[] args) throws SQLException {
        Statement statement = conn.createStatement();

        StringBuilder sb = new StringBuilder();
        int i = 2;
        while (i < args.length - 2) {
            sb.append(args[i]);
            i++;
            sb.append(" = ");
            sb.append(args[i]);
            i++;
            sb.append(", ");
        }
        sb.append(args[i]);
        i++;
        sb.append(" = ");
        sb.append(args[i]);
        StringBuilder where = new StringBuilder();
        if (args[1].length() > 3) {
            where.append(" where ").append(args[1]);
        }

        statement.executeUpdate("update " + args[0] + " set " + sb.toString() + where.toString());

    }

    /**
     * @param args 0: table name; 1: condition; 2: size of arguments; 3+: column names
     */
    private String[] selectDbUsage(String[] args) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet;

        StringBuilder sb = new StringBuilder();
        int i = 3;
        while (i < args.length - 1) {
            sb.append(args[i]);
            sb.append(", ");
            i++;
        }
        sb.append(args[i]);
        StringBuilder where = new StringBuilder();
        if (args[1].length() > 3) {
            where.append(" where ").append(args[1]);
        }
        resultSet = statement.executeQuery("select " + sb.toString() + " from " + args[0] + where.toString());
        String[] rsIDs = new String[Integer.parseInt(args[2])]; // decide the size
        ArrayList<String> list = new ArrayList<>();
        while (resultSet.next()) {
            for (i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                list.add(resultSet.getString(i));
            }
        }
        list.toArray(rsIDs);

        return rsIDs;
    }

    /**
     * @param args 0: table name
     */
    private void dropDbUsage(String[] args) throws SQLException {
        Statement statement = conn.createStatement();

        statement.executeUpdate("drop table " + args[0]);

    }

    //TODO: delete
}
