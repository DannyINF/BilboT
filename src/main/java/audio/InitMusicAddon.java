package audio;

import com.neovisionaries.ws.client.WebSocketFactory;
import commands.CmdTarget;
import core.CommandHandlerMusic;
import core.SlashCommandHandler;
import listeners.CommandsMusicListener;
import listeners.ReadyListener;
import listeners.VoiceListenerAddon;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import util.STREAM;

import javax.security.auth.login.LoginException;
import java.util.Objects;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class InitMusicAddon extends ListenerAdapter {
    private static JDABuilder builder;

    public static void main(String token, SlashCommandEvent event, EmbedBuilder embed, VoiceChannel userVoiceChannel) throws InterruptedException {

        WebSocketFactory ws = new WebSocketFactory();
        ws.setVerifyHostname(false);

        builder = JDABuilder.create(token,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGES);
        builder.setWebsocketFactory(ws);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, " \u266A"));

        addListeners();
        addCommands();

        JDA jda = null;

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        addSlashCommands(jda);

        assert jda != null;
        jda.awaitReady();

        TextChannel textChannel = null;
        Member member = null;
        Message message = null;
        Guild guild = null;
        VoiceChannel voiceChannel = null;

        try {
            textChannel = jda.getTextChannelById(event.getChannel().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            member = Objects.requireNonNull(jda.getGuildById(event.getGuild().getId())).getMember(Objects.requireNonNull(jda.getUserById(event.getUser().getId())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*try {
            message = Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(event.getGuild().getId())).getTextChannelById(event.getChannel().getId())).retrieveMessageById(event.getMessage().getId()).complete();
            Objects.requireNonNull(jda.getTextChannelById(event.getChannel().getId())).retrieveMessageById(event.getMessageId()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }*/ //TODO: FIXING
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

        SlashCommandHandler pc = new SlashCommandHandler();
        pc.musicPlayer(textChannel, member, guild, embed, voiceChannel, event);

    }

    private static void addCommands() {

    }

    private static void addSlashCommands(JDA jda) {
        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("music", "Musikbefehl")
                        .addSubcommands(new SubcommandData("join", "Tritt einem Voicechannel bei.").addOptions(new OptionData(STRING, "channel", "Channel oder ID")))
                        .addSubcommands(new SubcommandData("leave", "Verl\u00E4sst einen Voicechannel."))
                        .addSubcommands(new SubcommandData("play", "Spielt einen Track ab.").addOptions(new OptionData(STRING, "url", "Link")))
                        .addSubcommands(new SubcommandData("pplay", "F\u00FCgt eine Playlist hinzu.").addOptions(new OptionData(STRING, "url", "Link").setRequired(true)))
                        .addSubcommands(new SubcommandData("skip", "Skipt einen Track."))
                        .addSubcommands(new SubcommandData("pause", "Pausiert einen Track oder beendet eine Pausierung."))
                        .addSubcommands(new SubcommandData("stop", "Stoppt den Abspieler und entleert die Liste."))
                        .addSubcommands(new SubcommandData("volume", "Gibt die Lautst\u00E4rke zur\u00FCck oder setzt diese auf einen Wert.").addOptions(new OptionData(INTEGER, "amount", "Lautst\u00E4rke von 10 - 100")))
                        .addSubcommands(new SubcommandData("restart", "Startet den spielenden Track neu."))
                        .addSubcommands(new SubcommandData("repeat", "Setzt den Abspieler in einer Schleife oder beendet diese."))
                        .addSubcommands(new SubcommandData("reset", "Setzt den Abspieler komplett zur\u00FCck."))
                        .addSubcommands(new SubcommandData("info", "Gibt Informationen zum gerade spielenden Track aus."))
                        .addSubcommands(new SubcommandData("list", "Gibt die Wiedergabeliste aus."))
                        .addSubcommands(new SubcommandData("shuffle", "Mischt die Wiedergabeliste aus."))
        );

        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }

    private static void addListeners() {
        builder.addEventListeners(new VoiceListenerAddon());
        builder.addEventListeners(new CommandsMusicListener());
    }

}
