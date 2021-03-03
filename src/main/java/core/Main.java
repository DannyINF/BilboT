package core;

import audio.PlayerControl;
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
import net.dv8tion.jda.api.requests.GatewayIntent;
import special.Announcements;
import util.*;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        CommandHandler.commands.put("map", new CmdMap());
        CommandHandler.commands.put("maps", new CmdMap());

        CommandHandler.commands.put("help", new CmdHelp());

        CommandHandler.commands.put("botinfo", new CmdBotinfo());

        CommandHandler.commands.put("rules", new CmdRules());

        CommandHandler.commands.put("clear", new CmdClear());

        CommandHandler.commands.put("talk", new CmdTalk());

        CommandHandler.commands.put("xp", new CmdXp());

        CommandHandler.commands.put("language", new CmdLanguage());

        CommandHandler.commands.put("zitate", new CmdQuote());

        CommandHandler.commands.put("report", new CmdReport());

        CommandHandler.commands.put("search", new CmdSearch());

        CommandHandler.commands.put("setprofile", new CmdSetProfile());

        CommandHandler.commands.put("profile", new CmdProfile());

        CommandHandler.commands.put("deleteroles", new CmdDeleteRoles());
        CommandHandler.commands.put("createroles", new CmdCreateRoles());

        CommandHandler.commands.put("coins", new CmdCoins());

        CommandHandler.commands.put("music", new PlayerControl());

        CommandHandler.commands.put("intro", new CmdIntro());

        CommandHandler.commands.put("ban", new CmdBan());

        CommandHandler.commands.put("kick", new CmdKick());

        CommandHandler.commands.put("event", new CmdEvent());

        CommandHandler.commands.put("newspaper", new CmdNewspaper());

        CommandHandler.commands.put("jackpot", new CmdJackpot());

        CommandHandler.commands.put("channel", new CmdChannel());

        CommandHandler.commands.put("transfer", new CmdTransfer());

        CommandHandler.commands.put("statistik", new CmdStats());
        CommandHandler.commands.put("statistic", new CmdStats());
        CommandHandler.commands.put("statistics", new CmdStats());

        CommandHandler.commands.put("stats", new CmdStats());

        CommandHandler.commands.put("roles", new CmdRole());
        CommandHandler.commands.put("role", new CmdRole());

        CommandHandler.commands.put("server modules", new CmdModules_Server());

        CommandHandler.commands.put("server optimalsettings", new CmdOptimalSettings_Server());

        CommandHandler.commands.put("server language", new CmdLanguage_Server());

        CommandHandler.commands.put("exil", new CmdExil());

        CommandHandler.commands.put("edit", new CmdEdit());

        CommandHandler.commands.put("activity", new CmdActivity());

        CommandHandler.commands.put("quiz", new CmdQuiz());

        CommandHandler.commands.put("shutdown", new CmdShutdown());

        CommandHandler.commands.put("2x", new Cmd2x());

        CommandHandler.commands.put("shop", new ShopCommand());
    }

    private static void addListeners() {
        builder.addEventListeners(new ReadyListener());
        //builder.addEventListeners(new VoiceListener());
        builder.addEventListeners(new CommandsListener());
        /*builder.addEventListeners(new JoinListener());
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
        builder.addEventListeners(new CasualQuizListener());*/
        builder.addEventListeners(new ShopReactionListener());
    }
}