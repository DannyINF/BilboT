package audio;

import com.neovisionaries.ws.client.WebSocketFactory;
import commands.cmdBotinfo;
import core.commandHandler;
import listeners.commandsListener;
import listeners.introListener;
import listeners.readyListener;
import listeners.voiceListenerAddon;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class initScreamBot extends ListenerAdapter {
    private static JDABuilder builder;

    public static void main(String token, GuildVoiceJoinEvent event, VoiceChannel userVoiceChannel, String intro) throws InterruptedException {

        WebSocketFactory ws = new WebSocketFactory();
        ws.setVerifyHostname(false);

        builder = new JDABuilder(AccountType.BOT);
        builder.setWebsocketFactory(ws);
        builder.setToken(token);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, " RAAAAAAAAAAAAAAAAAAAAAH"));

        addListeners();
        addCommands();

        JDA jda = null;

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        assert jda != null;
        jda.awaitReady();

        Member member = null;
        Guild guild = null;
        VoiceChannel voiceChannel = null;


        try {
            member = jda.getGuildById(event.getGuild().getId()).getMember(jda.getUserById(event.getMember().getUser().getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            guild = jda.getGuildById(event.getGuild().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            voiceChannel = jda.getVoiceChannelById(userVoiceChannel.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        introListener il = new introListener();
        il.playIntro(intro, member, guild, voiceChannel);
    }

    private static void addCommands() {
    }

    private static void addListeners() {
        builder.addEventListeners(new readyListener());
        builder.addEventListeners(new introListener());
    }
}
