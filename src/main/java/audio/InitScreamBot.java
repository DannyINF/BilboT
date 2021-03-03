package audio;

import com.neovisionaries.ws.client.WebSocketFactory;
import listeners.IntroListener;
import listeners.ReadyListener;
import listeners.VoiceListenerAddon;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.STREAM;

import javax.security.auth.login.LoginException;

public class InitScreamBot extends ListenerAdapter {
    private static JDABuilder builder;
    public static boolean isStarting;
    public static JDA screamJda;

    public static void main(String token, GenericEvent event, Guild guild, VoiceChannel userVoiceChannel, String intro) throws InterruptedException {
        WebSocketFactory ws = new WebSocketFactory();
        ws.setVerifyHostname(false);

        builder = JDABuilder.create(token,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_PRESENCES);
        builder.setWebsocketFactory(ws);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, " RAAAAAAAAAAAAAAAAAAAAAH"));

        addListeners();
        addCommands();

        try {
            screamJda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        assert screamJda != null;
        screamJda.awaitReady();

        Guild guild1 = null;
        VoiceChannel voiceChannel = null;

        try {
            guild1 = screamJda.getGuildById(guild.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            voiceChannel = screamJda.getVoiceChannelById(userVoiceChannel.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        IntroListener il = new IntroListener();
        il.playIntro(intro, guild1, voiceChannel);
        isStarting = false;
        if (!STREAM.isTargeted)
            if (!STREAM.isStarted)
                STREAM.startStream(guild);
            else
                STREAM.switchStream(guild);
        else
            STREAM.saveStream(guild);
    }

    private static void addCommands() {

    }

    private static void addListeners() {
        builder.addEventListeners(new IntroListener());
        builder.addEventListeners(new VoiceListenerAddon());
    }
}