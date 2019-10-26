package core;

import audio.PlayerControl;
import commands.*;
import commands.server_settings.cmdLanguage_Server;
import commands.server_settings.cmdModules_Server;
import commands.server_settings.cmdOptimalSettings_Server;
import commands.user_settings.cmdLanguage;
import commands.user_settings.cmdProfile;
import listeners.*;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import util.ActivityChecker;
import util.SECRETS;
import util.voiceXP;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Main {
    private static JDABuilder builder;

    public static void main(String[] args) {
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(SECRETS.TOKEN);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "/help help | v2.3.2"));

        addListeners();
        addCommands();

        JDA jda = null;

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        final JDA finalJDA = jda;

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        ZonedDateTime nextRun = now.withHour(3).withMinute(30).withSecond(30);
        if (now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
                    try {
                        new ActivityChecker().activity(finalJDA);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                },
                initialDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            try {
                assert finalJDA != null;
                voiceXP.giveVoiceXP(finalJDA);
            } catch (SQLException | InterruptedException e) {
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
        commandHandler.commands.put("zitate", new cmdZitat());
        commandHandler.commands.put("report", new cmdReport());
        commandHandler.commands.put("search", new cmdSearch());
        commandHandler.commands.put("setprofile", new cmdSetProfile());
        commandHandler.commands.put("profile", new cmdProfile());
        commandHandler.commands.put("deleteroles", new cmdDeleteRoles());
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

        commandHandler.commands.put("proptodb", new cmdPropToDb());
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
        builder.addEventListeners(new addonCommunicationListener());
        builder.addEventListeners(new statisticsListener());
        builder.addEventListeners(new verificationListener());
    }
}
