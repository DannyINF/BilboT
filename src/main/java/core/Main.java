package core;

import commands.*;
import commands.server_settings.CmdLanguage_Server;
import commands.server_settings.CmdModules_Server;
import commands.server_settings.CmdOptimalSettings_Server;
import commands.user_settings.CmdLanguage;
import commands.user_settings.CmdProfile;
import listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import special.Announcements;
import util.*;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

//TODO: comment fucking everything!

class Main {
    private static JDABuilder builder;

    public static void main(String[] args) throws InterruptedException {
        builder = JDABuilder.create(SECRETS.TOKEN,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGES);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "/help | " + STATIC.VERSION + " | shorturl.at/aHQ23"));

        SHOPS.initShop();

        addListeners();
        addCommands();

        JDA jda = null;

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        addSlashCommands(jda);

        final JDA finalJDA = jda;

        assert jda != null;
        jda.awaitReady();

        STATIC.setVerified(new Role[]{Objects.requireNonNull(jda.getGuildById(STATIC.GUILD_ID)).getRolesByName("verified", true).get(0)});
        STATIC.setCam(new Role[]{Objects.requireNonNull(jda.getGuildById(STATIC.GUILD_ID)).getRolesByName("Cam", true).get(0)});

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        ZonedDateTime nextActivityRun = now.withHour(3).withMinute(30).withSecond(30);
        if (now.compareTo(nextActivityRun) > 0)
            nextActivityRun = nextActivityRun.plusDays(1);

        ZonedDateTime nextRankingRun = now.withHour(9).withMinute(0).withSecond(0);
        if (now.compareTo(nextRankingRun) > 0)
            nextRankingRun = nextRankingRun.plusDays(1);

        Duration durationActivity = Duration.between(now, nextActivityRun);
        long initialDelayActivity = durationActivity.getSeconds();

        LocalDateTime localDate = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
        localDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY)).withHour(13).withMinute(0);

        Duration durationRanking = Duration.between(now, nextRankingRun);
        long initialDelayRanking = durationRanking.getSeconds();

        ScheduledExecutorService schedulerActivity = Executors.newScheduledThreadPool(1);
        schedulerActivity.scheduleAtFixedRate(() -> {
                    try {
                        new ActivityChecker().activity(finalJDA);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                },
                initialDelayActivity,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        ScheduledExecutorService schedulerRanking = Executors.newScheduledThreadPool(1);
        JDA finalJda = jda;
        schedulerRanking.scheduleAtFixedRate(() -> {
                    Objects.requireNonNull(Objects.requireNonNull(finalJda.getGuildById(STATIC.GUILD_ID)).getTextChannelById("409055450802159616")).sendMessage("/xp ranking 1").queue();
                    Objects.requireNonNull(Objects.requireNonNull(finalJda.getGuildById(STATIC.GUILD_ID)).getTextChannelById("409055450802159616")).sendMessage("/xp ranking 11").queue();
                    Objects.requireNonNull(Objects.requireNonNull(finalJda.getGuildById(STATIC.GUILD_ID)).getTextChannelById("409055450802159616")).sendMessage("/xp ranking 21").queue();
                    },
                initialDelayRanking,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            try {
                VoiceXP.giveVoiceXP(finalJDA);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(ZonedDateTime.now());
        }, 0, 1, TimeUnit.MINUTES);
    }


    private static void addCommands() {

        CommandHandler.commands.put("rules", new CmdRules());

        CommandHandler.commands.put("language", new CmdLanguage());

        CommandHandler.commands.put("zitate", new CmdQuote());

        CommandHandler.commands.put("setprofile", new CmdSetProfile());

        CommandHandler.commands.put("profile", new CmdProfile());

        CommandHandler.commands.put("newspaper", new CmdNewspaper());

        CommandHandler.commands.put("jackpot", new CmdJackpot());

        CommandHandler.commands.put("transfer", new CmdTransfer());


        CommandHandler.commands.put("server modules", new CmdModules_Server());

        CommandHandler.commands.put("server optimalsettings", new CmdOptimalSettings_Server());

        CommandHandler.commands.put("server language", new CmdLanguage_Server());
    }

    private static void addSlashCommands(JDA jda) {
        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("2x", "Aktiviert oder deaktiviert das Doppel-XP-Event.")
        );

        commands.addCommands(
                new CommandData("activity", "Gibt die Aktivit\u00E4t eines Nutzers zur\u00FCck.")
                    .addOptions(new OptionData(USER, "activity_user", "Nutzer, dessen Aktivit\u00E4t zur\u00FCckgegeben werden soll.")
                        .setRequired(true))
        );

        commands.addCommands(
                new CommandData("ban", "Bannt einen Nutzer.")
                    .addOptions(new OptionData(USER, "ban_user", "Nutzer, der gebannt werden soll.")
                        .setRequired(true))
                    .addOptions(new OptionData(INTEGER, "del_days", "L\u00F6scht die Nachrichten der letzten Tage."))
                    .addOptions(new OptionData(STRING, "ban_reason", "Grund f\u00FCr den Ban."))
        );

        commands.addCommands(
                new CommandData("botinfo", "Gibt Informationen zum Bot aus.")
        );

        commands.addCommands(
                new CommandData("channel", "Setzt Channel.")
                .addSubcommands(new SubcommandData("set", "Setzt einen Channeltyp.")
                        .addOptions(new OptionData(STRING, "channel_type", "Channeltyp")
                            .addChoice("Log", "log").addChoice("Modlog", "modlog").addChoice("Spam", "spam")
                            .addChoice("Voicelog", "voicelog").addChoice("CMDlog", "cmdlog").setRequired(true))
                .addOptions(new OptionData(CHANNEL, "channel_channel", "Channel").setRequired(true)))
        );

        commands.addCommands(
                new CommandData("clear", "L\u00F6scht Nachrichten aus diesem Channel.")
                    .addOptions(new OptionData(INTEGER, "clear_amount", "Wie viele Nachrichten gel\u00F6scht werden sollen. (Standard: 100)"))
        );

        commands.addCommands(
                new CommandData("coins", "Gibt die Coins eines Nutzers aus.")
                    .addSubcommands(
                            new SubcommandData("give", "F\u00FCge einem Nutzer Coins hinzu.")
                                .addOptions(new OptionData(INTEGER, "coins_give_amount", "Anzahl an Coins").setRequired(true))
                                .addOptions(new OptionData(USER, "coins_give_user", "Nutzer der die Coins erhalten soll.").setRequired(true))
                    )
                    .addSubcommands(
                            new SubcommandData("gift", "Schenke einem Nutzer einen Teil deiner eigenen Coins.")
                                    .addOptions(new OptionData(INTEGER, "coins_gift_amount", "Anzahl an Coins").setRequired(true))
                                    .addOptions(new OptionData(USER, "coins_gift_user", "Nutzer der die Coins erhalten soll.").setRequired(true))
                    )
                    .addSubcommands(
                            new SubcommandData("get", "Gibt die Coins aus.")
                                .addOptions(new OptionData(USER, "coins_user", "Coins des Nutzers"))
                    )
        );

        commands.addCommands(
                new CommandData("edit", "Erm\u00F6glicht arbeiten an der Datenbank.")
                    .addOptions(new OptionData(STRING, "edit_id", "ID des Servers.")
                        .setRequired(true))
                    .addOptions(new OptionData(STRING, "edit_query", "Datenbankanfrage")
                        .setRequired(true))
        );

        commands.addCommands(
                new CommandData("narration", "Lyrikecke")
                .addSubcommands(new SubcommandData("start", "Lyrikecke"))
                .addSubcommands(new SubcommandData("stop", "Lyrikecke"))
                .addSubcommands(new SubcommandData("discussion", "Lyrikecke"))
                .addSubcommands(new SubcommandData("set-reader", "Setzt einen Nutzer als Leser.").addOptions(new OptionData(USER, "narration_set_user", "Setzt den Nutzer als Leser.")))
                .addSubcommands(new SubcommandData("get-reader", "Gibt die Leser zur\u00FCck."))
                .addSubcommands(new SubcommandData("remove-reader", "Entfernt einen Leser.").addOptions(new OptionData(USER, "narration_remove_user", "Entfernt diesen Nutzer als Leser.")))
                .addSubcommands(new SubcommandData("clear-reader", "Niemand ist mehr Leser."))
        );

        commands.addCommands(
                new CommandData("exil", "Exiliert oder deexiliert einen Nutzer.")
                    .addOptions(new OptionData(USER, "exil_user", "De/exiliert diesen Nutzer.")
                        .setRequired(true))
        );

        commands.addCommands(
                new CommandData("fakejoin", "description")
        );

        commands.addCommands(
                new CommandData("help", "Gibt die Hilfe f\u00FCr verschiedene Themen aus.")
                    .addOptions(new OptionData(STRING, "help_topic", "Zeigt Hilfe zu diesem Suchbegriff an."))

        );

        commands.addCommands(
                new CommandData("intro", "Intro")
                .addSubcommands(new SubcommandData("list", "Gibt eine Liste aller Intros aus."))
                .addSubcommands(new SubcommandData("set", "R\u00FCstet ein Intro aus.")
                    .addOptions(new OptionData(STRING, "intro_set_intro", "R\u00FCstet dieses Intro aus.").setRequired(true)))
                .addSubcommands(new SubcommandData("inventory", "Gibt eine Liste der eigenen Intros aus."))
                .addSubcommands(new SubcommandData("price", "Gibt eine Preistabelle für die Intros aus."))
                .addSubcommands(new SubcommandData("buy", "Kauft ein Intro.")
                    .addOptions(new OptionData(STRING, "intro_buy_intro", "Kauft dieses Intro oder ein zuf\u00E4lliges Intro dieser Art.").setRequired(true)))
        );

        commands.addCommands(
                new CommandData("kick", "Kickt einen Nutzer.")
                    .addOptions(new OptionData(USER, "kick_user", "Nutzer, der gekickt werden soll.")
                        .setRequired(true))
                    .addOptions(new OptionData(STRING, "kick_reason", "Grund f\u00FCr den Kick."))
        );

        commands.addCommands(
                new CommandData("map", "Gibt eine Karte aus.")
                    .addOptions(new OptionData(STRING, "map", "Karte, die ausgegeben werden soll.")
                        .setRequired(true))
        );

        commands.addCommands(
                new CommandData("quiz", "Startet das Quiz in einem Modus.")
                .addSubcommands(new SubcommandData("stats", "Gibt die Quizstatistiken aus.").addOptions(new OptionData(INTEGER, "quiz_stats_season", "Stats der Saison"))
                    .addOptions(new OptionData(USER, "quiz_stats_user", "Statistiken dieses Nutzers.")))
                .addSubcommands(new SubcommandData("casual", "Startet eine Casual-Runde."))
                .addSubcommands(new SubcommandData("ranked", "Startet eine Ranked-Runde."))
                .addSubcommands(new SubcommandData("add", "F\u00FCgt eine Frage hinzu."))
                .addSubcommands(new SubcommandData("report", "Meldet eine Frage.").addOptions(new OptionData(STRING, "report_question", "Meldet diese Frage.").setRequired(true))
                    .addOptions(new OptionData(STRING, "quiz_report_reason", "Begr\u00FCndung").setRequired(true)))
                .addSubcommands(new SubcommandData("ranking", "Zeigt das Leaderboard.").addOptions(new OptionData(INTEGER, "quiz_ranking_rank", "... ab dieser Platzierung."))
                    .addOptions(new OptionData(INTEGER, "quiz_ranking_season", "Rangliste der Saison.")))
        );

        commands.addCommands(
                new CommandData("report", "Erstellt einen Report")
        );

        commands.addCommands(
                new CommandData("role", "Rollenmanagement")
                    .addSubcommands(new SubcommandData("add", "Vergibt eine Rolle")
                            .addOptions(new OptionData(STRING, "add_role", "Name der Rolle")
                                    .setRequired(true)))
                    .addSubcommands(new SubcommandData("remove", "Entfernt eine Rolle")
                            .addOptions(new OptionData(STRING, "remove_role", "Name der Rolle")
                                    .setRequired(true)))
                    .addSubcommands(new SubcommandData("create", "Erstellt eine Rolle")
                            .addOptions(new OptionData(STRING, "create_role", "Name der Rolle")
                                    .setRequired(true)))
                    .addSubcommands(new SubcommandData("delete", "L\u00F6scht eine Rolle")
                            .addOptions(new OptionData(STRING, "delete_role", "Name der Rolle")
                                    .setRequired(true)))
                    .addSubcommands(new SubcommandData("list", "Listet alle Rollen auf"))
        );

        commands.addCommands(
                new CommandData("search", "Sucht nach einer Anfrage auf verschiedenen Seiten.")
                    .addOptions(new OptionData(STRING, "search_site", "Seite, auf der gesucht werden soll.").addChoice("Wikipedia", "wikipedia").addChoice("Ardapedia", "ardapedia")
                        .setRequired(true))
                    .addOptions(new OptionData(STRING, "search_query", "Suchanfrage")
                        .setRequired(true))
        );

        commands.addCommands(
                new CommandData("shutdown", "F\u00E4hrt den Bot herunter.")
        );

        commands.addCommands(
                new CommandData("stats", "Gibt Statistiken aus.")
                    .addOptions(new OptionData(USER, "stats_user", "Statistiken dieses Nutzers"))
        );

        commands.addCommands(
                new CommandData("say", "L\u00E4sst den Bot reden.")
                    .addOptions(new OptionData(STRING, "say_query", "Was der Bot sagen soll.")
                        .setRequired(true))
        );

        commands.addCommands(
                new CommandData("target", "desc")
                    .addOptions(new OptionData(STRING, "target_bot", "desc")
                        .setRequired(true))
        );

        commands.addCommands(
                new CommandData("xp", "Gibt deine XP aus.")
                .addSubcommands(new SubcommandData("ranking", "Gibt eine XP-Rangliste aus. (Standard: eigene Platzierung)")
                        .addOptions(new OptionData(INTEGER, "xp_rank", "Rangliste ab dieser Platzierung.")))
                .addSubcommands(new SubcommandData("give", "Vergibt XP an einen Nutzer")
                        .addOptions(new OptionData(USER, "xp_give_user", "Nutzer, der XP erhalten soll.").setRequired(true)))
                .addSubcommands(new SubcommandData("next", "Zeigt an, wie viele XP zum n\u00E4chsten Level und zum n\u00E4chsten Rang ben\u00F6tigt werden.")
                        .addOptions(new OptionData(USER, "xp_next_user", "F\u00FCr diesen Nutzer.")))
                .addSubcommands(new SubcommandData("get", "Gibt die XP aus.")
                                .addOptions(new OptionData(USER, "xp_user", "XP des Nutzers")))
        );

        commands.addCommands(
                new CommandData("shop", "desc")
        );

        commands.addCommands(
                new CommandData("music", "Musikbefehl")
                    .addSubcommands(new SubcommandData("join", "Tritt einem Voicechannel bei.").addOptions(new OptionData(STRING, "music_join_channel", "Channel oder ID")))
                    .addSubcommands(new SubcommandData("leave", "Verl\u00E4sst einen Voicechannel."))
                    .addSubcommands(new SubcommandData("play", "Spielt einen Track ab.").addOptions(new OptionData(STRING, "music_play_url", "Link")))
                    .addSubcommands(new SubcommandData("pplay", "F\u00FCgt eine Playlist hinzu.").addOptions(new OptionData(STRING, "music_pplay_url", "Link").setRequired(true)))
                    .addSubcommands(new SubcommandData("skip", "Skipt einen Track."))
                    .addSubcommands(new SubcommandData("pause", "Pausiert einen Track oder beendet eine Pausierung."))
                    .addSubcommands(new SubcommandData("stop", "Stoppt den Abspieler und entleert die Liste."))
                    .addSubcommands(new SubcommandData("volume", "Gibt die Lautst\u00E4rke zur\u00FCck oder setzt diese auf einen Wert.").addOptions(new OptionData(INTEGER, "music_volume_amount", "Lautst\u00E4rke von 10 - 100")))
                    .addSubcommands(new SubcommandData("restart", "Startet den spielenden Track neu."))
                    .addSubcommands(new SubcommandData("repeat", "Setzt den Abspieler in einer Schleife oder beendet diese."))
                    .addSubcommands(new SubcommandData("reset", "Setzt den Abspieler komplett zur\u00FCck."))
                    .addSubcommands(new SubcommandData("info", "Gibt Informationen zum gerade spielenden Track aus."))
                    .addSubcommands(new SubcommandData("list", "Gibt die Wiedergabeliste aus."))
                    .addSubcommands(new SubcommandData("shuffle", "Mischt die Wiedergabeliste aus."))
        );

        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        //commands.queue();
    }

    private static void addListeners() {
        builder.addEventListeners(new ReadyListener());
        builder.addEventListeners(new VoiceListener());
        builder.addEventListeners(new CommandsListener());
        builder.addEventListeners(new JoinListener());
        builder.addEventListeners(new LeaveListener());
        builder.addEventListeners(new ReportListener());
        builder.addEventListeners(new BanListener());
        builder.addEventListeners(new ChannelListener());
        builder.addEventListeners(new ChatfilterListener());
        builder.addEventListeners(new XpListener());
        builder.addEventListeners(new OnlineListener());
        builder.addEventListeners(new LoginListener());
        builder.addEventListeners(new IntroListener());
        builder.addEventListeners(new MuteListener());
        builder.addEventListeners(new AfkListener());
        builder.addEventListeners(new SelfJoinListener());
        builder.addEventListeners(new SpamListener());
        builder.addEventListeners(new EmptyChannelListener());
        builder.addEventListeners(new StatisticsListener());
        builder.addEventListeners(new VerificationListener());
        builder.addEventListeners(new Announcements());
        builder.addEventListeners(new ModReactionListener());
        builder.addEventListeners(new NewQuestionListener());
        builder.addEventListeners(new EditQuestionListener());
        builder.addEventListeners(new ExpertReactionListener());
        builder.addEventListeners(new CasualQuizListener());
        builder.addEventListeners(new ShopReactionListener());
        builder.addEventListeners(new SlashCommandHandler());
    }
}