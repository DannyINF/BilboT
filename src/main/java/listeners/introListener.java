package listeners;

import audio.GuildMusicManager;
import audio.initScreamBot;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.databaseHandler;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.PlaylistCheckerScreamBot;
import util.SECRETS;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static audio.PlayerControl.DEFAULT_VOLUME;
import static audio.initScreamBot.isStarting;
import static audio.initScreamBot.screamJda;

public class introListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (!event.getMember().getUser().isBot()) {
            boolean print;
            print = !event.getChannelJoined().getName().equals("\uD83D\uDCDA-Lyrikecke");
            boolean isOnServer = false;
            boolean isOnline = false;
            try {
                isOnServer = event.getGuild().getMemberById("454613079804608522").getUser().getId().equals("454613079804608522");
                if (isOnServer) {
                    try {
                        isOnline = event.getGuild().getMemberById("454613079804608522").getOnlineStatus().equals(OnlineStatus.ONLINE);
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}

            if (print) {
                if(isOnServer) {
                    if (isOnline) {
                        if (event.getJDA().getSelfUser().getId().equals("454613079804608522")) {
                            String intro;
                            String[] arguments = {"users", "id = '" + event.getMember().getUser().getId() + "'", "1", "intro"};
                            try {
                                intro = databaseHandler.database(event.getGuild().getId(), "select", arguments)[0].split("#")[0];
                                playIntro(intro, event.getGuild(), event.getChannelJoined());
                            } catch (NullPointerException | SQLException ignored) { }
                        }
                    } else if (!isStarting){
                        String intro;
                        String[] arguments = {"users", "id = '" + event.getMember().getUser().getId() + "'", "1", "intro"};
                        try {
                            intro = databaseHandler.database(event.getGuild().getId(), "select", arguments)[0].split("#")[0];
                            try {
                                if (!intro.equals("nothing")) {
                                    isStarting = true;
                                    initScreamBot.main(SECRETS.TOKENSCREAM, event, event.getChannelJoined(), intro);
                                }
                            } catch (InterruptedException e) {
                                isStarting = false;
                                e.printStackTrace();
                            }
                        } catch (NullPointerException | SQLException ignored) { }
                    } else {
                        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                        exec.scheduleAtFixedRate(() -> {
                            if (event.getGuild().getMemberById("454613079804608522").getOnlineStatus().equals(OnlineStatus.ONLINE)) {
                                Guild guild = null;
                                VoiceChannel voiceChannel = null;

                                try {
                                    guild = screamJda.getGuildById(event.getGuild().getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    voiceChannel = screamJda.getVoiceChannelById(event.getChannelJoined().getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String intro;
                                String[] arguments = {"users", "id = '" + event.getMember().getUser().getId() + "'", "1", "intro"};
                                try {
                                    intro = databaseHandler.database(event.getGuild().getId(), "select", arguments)[0].split("#")[0];
                                    if (!intro.equals("nothing"))
                                        playIntro(intro, guild, voiceChannel);
                                } catch (NullPointerException | SQLException ignored) { }

                                exec.shutdown();
                            }
                        }, 0, 1, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }
    private AudioPlayerManager playerManager;
    private Map<String, GuildMusicManager> musicManagers;

    public void playIntro(String intro, Guild guild, VoiceChannel userVoiceChannel) {
        java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        musicManagers = new HashMap<>();

        GuildMusicManager mng = getMusicManager(guild);
        String pl = null;
        String introindex;

        if (intro.contains("-")) {
            switch (intro.split("-")[0]) {
                case "common":
                    pl = "PL_epOfFugDagfFqqNqvykuieqyxngEISt";
                    break;
                case "rare":
                    pl = "PL_epOfFugDagG-R8IjY42YW2Qwy2S45jK";
                    break;
                case "epic":
                    pl = "PL_epOfFugDagi0OcxJvJ3ZdDvRUOOuGQJ";
                    break;
                case "legendary":
                    pl = "PL_epOfFugDaivDeYCaiEJXZklYvpUyv3-";
                    break;
                case "custom":
                    pl = "PL_epOfFugDag4MhC046KsXgG4eg9mVesk";
                    break;
            }
            introindex = intro.split("-")[1];

            ArrayList pl_info = null;

            try {
                pl_info = PlaylistCheckerScreamBot.check(pl, introindex);
                System.out.println(pl_info);
            } catch (IOException ignored) {}

            String input = null;
            try {
                assert pl_info != null;
                input = String.valueOf(pl_info.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }


            assert input != null;
            input = "https://www.youtube.com/watch?v=" + input;

            guild.getAudioManager().setSendingHandler(mng.sendHandler);
            guild.getAudioManager().openAudioConnection(userVoiceChannel);

            loadAndPlay(mng, input);
        }
    }

    private void loadAndPlay(GuildMusicManager mng, String url)
    {
        final String trackUrl;

        //Strip <>'s that prevent discord from embedding link resources
        if (url.startsWith("<") && url.endsWith(">"))
            trackUrl = url.substring(1, url.length() - 1);
        else
            trackUrl = url;

        playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                mng.scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                List<AudioTrack> tracks = playlist.getTracks();
                tracks.forEach(mng.scheduler::queue);
            }

            @Override
            public void noMatches() {}

            @Override
            public void loadFailed(FriendlyException e) {}

        });
    }

    private GuildMusicManager getMusicManager(Guild guild) {
        String guildId = guild.getId();
        GuildMusicManager mng = musicManagers.get(guildId);
        if (mng == null) {
            synchronized (musicManagers) {
                mng = musicManagers.get(guildId);
                if (mng == null) {
                    mng = new GuildMusicManager(playerManager);
                    mng.player.setVolume(DEFAULT_VOLUME);
                    musicManagers.put(guildId, mng);
                }
            }
        }
        return mng;
    }
}