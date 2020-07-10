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
     * - reports(report_id, victim_id, offender_id, channel, cause, info, ruling)
     * - exil(id, roles, duration)
     * - quizcasual(id, streak, question_id)
     * - quizranked(id, question, quest1, quest2, quest3, quest4, quest5, quest6, quest7, quest8, quest9, quest10, elo)
     * - quizusers(id, season, casual_correct, casual_false, ranked_correct, ranked_false, casual_streak, ranked_streak, ranked_building_streak, elo, peak, casual_games, ranked_games)
     * - quizquestions(id, question, answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9, answer10, threshhold, correct, incorrect, status, author_id, report_id, reason, edit_id)
     * - TODO: quizreport-command
     * Zeit für Antwort: insg. Zeichen * 1 Sekunde * antworten / threshhold
     * maximale Zeit für Antwort: insg. Zeichen * 3 Sekunde * antworten / threshhold + Zeichen der Frage / 20
     * 0-30; 10-40; 20-50; 30-60; 40-70; 50-80; 60-90; 70+
     * Start: -9.000, pro Frage max. +2.000; 10 Fragen = ELO = Punkte / 10 (e.g. 1.234; 4.546; ...)
     *
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
     * - reports(report_id varchar(20), victim_id varchar(20), offender_id varchar(20), channel varchar(100), cause varchar(200), info clob(2000), ruling varchar(20))
     * - exil(id varchar(20), roles clob(2000), duration int)
     * - quizcasual(id varchar(20), streak bigint, question_id bigint)
     * - quizranked(id varchar(20), question bigint, quest1 boolean, quest2 boolean, quest3 boolean, quest4 boolean, quest5 boolean, quest6 boolean, quest7 boolean, quest8 boolean,
     * quest9 boolean, quest10 boolean, elo bigint)
     * - quizusers(id varchar(20), season bigint, casual_correct bigint, casual_false bigint, ranked_correct bigint, ranked_false bigint, casual_streak bigint, ranked_streak bigint,
     * ranked_building_streak bigint, elo bigint, peak bigint, casual_games bigint, ranked_games bigint)
     * - quizquestions(id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), question clob(2000), answer1 clob(2000), answer2 clob(2000), answer3 clob(2000), answer4 clob(2000), answer5 clob(2000), answer6 clob(2000),
     * answer7 clob(2000), answer8 clob(2000), answer9 clob(2000), answer10 clob(2000), threshhold bigint, correct bigint, incorrect bigint, status bigint, author_id varchar(20), report_id varchar(20), reason clob(2000), edit_id varchar(20))
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
     * moderator varchar(20), moderator_color varchar(7), supporter varchar(20), support_color varchar(7), custom clob(64000))
     * - channels(id varchar(20), spam varchar(20), modlog varchar(20), log varchar(20), voicelog varchar(20), cmdlog varchar(20))
     * - events(id varchar(20), narration clob(2000))
     * <p>
     * usersettings
     * - users(id varchar(20), language varchar(20), country varchar(20), sex varchar(20), profile clob(64000), spammer boolean, verified boolean)
     */
    private static Connection conn;

    public static String[] database(String database, String statement_string) throws SQLException {
        databaseHandler app = new databaseHandler();

        app.connectionToDerby(database);
        Statement statement = conn.createStatement();
        if (statement_string.split(" ")[0].toLowerCase().equals("select")) {
            ResultSet resultSet = statement.executeQuery(statement_string);
            ArrayList<String> list = new ArrayList<>();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    list.add(resultSet.getString(i));
                }
            }
            String[] result = new String[list.size()];
            list.toArray(result);

            return result;
        }

        statement.execute(statement_string);
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
}
