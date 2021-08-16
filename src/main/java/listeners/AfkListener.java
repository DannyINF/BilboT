package listeners;

import audio.AudioPlayerSendHandler;
import audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;
import util.STREAM;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class AfkListener extends ListenerAdapter {

    //TODO: disconnect from voice channels and kill player when moved into afk-channel

}
