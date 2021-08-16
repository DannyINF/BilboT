package listeners;

import audio.InitScreamBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STREAM;

import javax.annotation.Nonnull;
import java.util.Objects;

public class VoiceListenerAddon extends ListenerAdapter {
    //TODO: comment
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                boolean onlyBotsHere = true;
                boolean onlyBotsEverywhere = true;
                for (Guild g : event.getJDA().getGuilds()) {
                    if (g.getAudioManager().isConnected()) {
                        for (Member m : Objects.requireNonNull(g.getAudioManager().getConnectedChannel()).getMembers()) {
                            if (!m.getUser().isBot()) {
                                onlyBotsEverywhere = false;
                                if (g.equals(event.getGuild())) {
                                    onlyBotsHere = false;
                                }
                            }
                        }
                    }
                }

                if (onlyBotsEverywhere) {
                    if (event.getJDA().equals(InitScreamBot.screamJda)) {
                        STREAM.streammanager.setSendingHandler(null);
                        STREAM.streammanager.setReceivingHandler(null);
                        STREAM.streammanager.closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                        STREAM.streamJda.shutdown();
                    }
                    event.getJDA().shutdown();
                } else if (onlyBotsHere) {
                    if (event.getJDA().equals(InitScreamBot.screamJda)) {
                        STREAM.streammanager.setSendingHandler(null);
                        STREAM.streammanager.setReceivingHandler(null);
                        STREAM.streammanager.closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                        event.getGuild().getAudioManager().setSendingHandler(null);
                        event.getGuild().getAudioManager().setReceivingHandler(null);
                        event.getGuild().getAudioManager().closeAudioConnection();
                    } else {
                        event.getGuild().getAudioManager().setSendingHandler(null);
                        event.getGuild().getAudioManager().closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                boolean onlyBotsHere = true;
                boolean onlyBotsEverywhere = true;
                for (Guild g : event.getJDA().getGuilds()) {
                    if (g.getAudioManager().isConnected()) {
                        for (Member m : Objects.requireNonNull(g.getAudioManager().getConnectedChannel()).getMembers()) {
                            if (!m.getUser().isBot()) {
                                onlyBotsEverywhere = false;
                                if (g.equals(event.getGuild())) {
                                    onlyBotsHere = false;
                                }
                            }
                        }
                    }
                }

                if (onlyBotsEverywhere) {
                    if (event.getJDA().equals(InitScreamBot.screamJda)) {
                        STREAM.streammanager.setSendingHandler(null);
                        STREAM.streammanager.setReceivingHandler(null);
                        STREAM.streammanager.closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                        STREAM.streamJda.shutdown();
                    }
                    event.getJDA().shutdown();
                } else if (onlyBotsHere) {
                    if (event.getJDA().equals(InitScreamBot.screamJda)) {
                        STREAM.streammanager.setSendingHandler(null);
                        STREAM.streammanager.setReceivingHandler(null);
                        STREAM.streammanager.closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                        event.getGuild().getAudioManager().setSendingHandler(null);
                        event.getGuild().getAudioManager().setReceivingHandler(null);
                        event.getGuild().getAudioManager().closeAudioConnection();
                    } else {
                        event.getGuild().getAudioManager().setSendingHandler(null);
                        event.getGuild().getAudioManager().closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                    }
                }
            } else if (event.getChannelJoined().equals(event.getGuild().getAudioManager().getConnectedChannel())){
                boolean onlyBotsHere = true;
                boolean onlyBotsEverywhere = true;
                for (Guild g : event.getJDA().getGuilds()) {
                    if (g.getAudioManager().isConnected()) {
                        for (Member m : Objects.requireNonNull(g.getAudioManager().getConnectedChannel()).getMembers()) {
                            if (!m.getUser().isBot()) {
                                onlyBotsEverywhere = false;
                                if (g.equals(event.getGuild())) {
                                    onlyBotsHere = false;
                                }
                            }
                        }
                    }
                }

                if (onlyBotsEverywhere) {
                    if (event.getJDA().equals(InitScreamBot.screamJda)) {
                        STREAM.streammanager.setSendingHandler(null);
                        STREAM.streammanager.setReceivingHandler(null);
                        STREAM.streammanager.closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                        STREAM.streamJda.shutdown();
                    }
                    event.getJDA().shutdown();
                } else if (onlyBotsHere) {
                    if (event.getJDA().equals(InitScreamBot.screamJda)) {
                        STREAM.streammanager.setSendingHandler(null);
                        STREAM.streammanager.setReceivingHandler(null);
                        STREAM.streammanager.closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                        event.getGuild().getAudioManager().setSendingHandler(null);
                        event.getGuild().getAudioManager().setReceivingHandler(null);
                        event.getGuild().getAudioManager().closeAudioConnection();
                    } else {
                        event.getGuild().getAudioManager().setSendingHandler(null);
                        event.getGuild().getAudioManager().closeAudioConnection();
                        if (STREAM.receivemanager.getGuild().equals(event.getGuild())) {
                            try {
                                STREAM.switchStream(STREAM.streams.get(0));
                            } catch (Exception ignored) {

                            }
                        } else {
                            STREAM.removeStream(event.getGuild());
                        }
                    }
                }
            }
        }
    }
}
