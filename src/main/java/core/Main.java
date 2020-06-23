package core;

import audio.PlayerControl;
import commands.*;
import commands.server_settings.cmdLanguage_Server;
import commands.server_settings.cmdModules_Server;
import commands.server_settings.cmdOptimalSettings_Server;
import commands.user_settings.cmdLanguage;
import commands.user_settings.cmdProfile;
import listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;
import special.announcements;
import util.ActivityChecker;
import util.SECRETS;
import util.STATIC;
import util.voiceXP;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//TODO: Tidying up and simplifying the usage of databases
//TODO: make command to export database to JSON-file for transfer
//TODO: comment fucking everything
//TODO: more efficient loading of information (only safe / one-time-pull)

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
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "/help help | " + STATIC.VERSION));

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

        STATIC.setVerified(new Role[]{Objects.requireNonNull(jda.getGuildById(388969412889411585L)).getRolesByName("verified", true).get(0)});
        STATIC.setCam(new Role[]{Objects.requireNonNull(jda.getGuildById(388969412889411585L)).getRolesByName("Cam", true).get(0)});

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        ZonedDateTime nextActivityRun = now.withHour(3).withMinute(30).withSecond(30);
        if (now.compareTo(nextActivityRun) > 0)
            nextActivityRun = nextActivityRun.plusDays(1);

        Duration durationActivity = Duration.between(now, nextActivityRun);
        long initialDelayActivity = durationActivity.getSeconds();

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

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            try {
                voiceXP.giveVoiceXP(finalJDA);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(ZonedDateTime.now());
        }, 0, 1, TimeUnit.MINUTES);
    }


    private static void addCommands() {
        commandHandler.commands.put("map", new cmdMap());
        commandHandler.commands.put("maps", new cmdMap());

        commandHandler.commands.put("help", new cmdHelp());

        commandHandler.commands.put("botinfo", new cmdBotinfo());

        commandHandler.commands.put("rules", new cmdRules());

        commandHandler.commands.put("clear", new cmdClear());

        commandHandler.commands.put("talk", new cmdTalk());

        commandHandler.commands.put("xp", new cmdXp());

        commandHandler.commands.put("language", new cmdLanguage());

        commandHandler.commands.put("zitate", new cmdQuote());

        commandHandler.commands.put("report", new cmdReport());

        commandHandler.commands.put("search", new cmdSearch());

        commandHandler.commands.put("setprofile", new cmdSetProfile());

        commandHandler.commands.put("profile", new cmdProfile());

        commandHandler.commands.put("deleteroles", new cmdDeleteRoles());
        commandHandler.commands.put("createroles", new cmdCreateRoles());

        commandHandler.commands.put("coins", new cmdCoins());

        commandHandler.commands.put("music", new PlayerControl());

        commandHandler.commands.put("intro", new cmdIntro());

        commandHandler.commands.put("ban", new cmdBan());

        commandHandler.commands.put("kick", new cmdKick());

        commandHandler.commands.put("event", new cmdEvent());

        commandHandler.commands.put("newspaper", new cmdNewspaper());

        commandHandler.commands.put("jackpot", new cmdJackpot());

        commandHandler.commands.put("channel", new cmdChannel());

        commandHandler.commands.put("transfer", new cmdTransfer());

        commandHandler.commands.put("statistik", new cmdStats());
        commandHandler.commands.put("statistic", new cmdStats());
        commandHandler.commands.put("statistics", new cmdStats());

        commandHandler.commands.put("stats", new cmdStats());

        commandHandler.commands.put("roles", new cmdRole());
        commandHandler.commands.put("role", new cmdRole());

        commandHandler.commands.put("server modules", new cmdModules_Server());

        commandHandler.commands.put("server optimalsettings", new cmdOptimalSettings_Server());

        commandHandler.commands.put("server language", new cmdLanguage_Server());

        commandHandler.commands.put("exil", new cmdExil());

        commandHandler.commands.put("edit", new cmdEdit());

        commandHandler.commands.put("activity", new cmdActivity());
    }

    private static void addListeners() {
        builder.addEventListeners(new readyListener());
        builder.addEventListeners(new voiceListener());
        builder.addEventListeners(new commandsListener());
        builder.addEventListeners(new joinListener());
        builder.addEventListeners(new leaveListener());
        builder.addEventListeners(new privateListener());
        builder.addEventListeners(new banListener());
        builder.addEventListeners(new channelListener());
        builder.addEventListeners(new chatfilterListener());
        builder.addEventListeners(new xpListener());
        builder.addEventListeners(new onlineListener());
        builder.addEventListeners(new loginListener());
        builder.addEventListeners(new introListener());
        builder.addEventListeners(new muteListener());
        builder.addEventListeners(new afkListener());
        builder.addEventListeners(new selfJoinListener());
        builder.addEventListeners(new spamListener());
        builder.addEventListeners(new emptyChannelListener());
        builder.addEventListeners(new statisticsListener());
        builder.addEventListeners(new verificationListener());
        builder.addEventListeners(new announcements());
    }
}