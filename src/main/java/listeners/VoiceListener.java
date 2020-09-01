package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;
import util.STATIC;

import java.awt.*;
import java.util.Objects;

//TODO: Check this out as well
public class VoiceListener extends ListenerAdapter implements AudioReceiveHandler {
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (STATIC.getIsNarration() && event.getChannelJoined().equals(event.getGuild().getVoiceChannelById("469209414218285057")) && !STATIC.getIsDiscussion()) {
            if (!event.getMember().getUser().isBot())
                event.getMember().mute(true).queue();
        } else if (Objects.requireNonNull(event.getMember().getVoiceState()).isGuildMuted())
            event.getMember().mute(false).queue();
        if (event.getGuild().getRolesByName(event.getChannelJoined().getName(), true).isEmpty()) {
            event.getGuild().createRole().setColor(Color.LIGHT_GRAY).setName(event.getChannelJoined().getName())
                    .setMentionable(true).setHoisted(false).queue();
        }
        Permission[] voicepermission = new Permission[]{Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL};

        if (event.getGuild().getTextChannelsByName(event.getChannelJoined().getName().replace(" ", "-"), true).isEmpty()) {
            TextChannel text = event.getGuild().getCategoriesByName(Objects.requireNonNull(event.getChannelJoined().getParent()).getName(), true).get(0)
                    .createTextChannel(event.getChannelJoined().getName()).setTopic("Chat zu " + event.getChannelJoined().getName()).complete();
            text.createPermissionOverride(event.getGuild().getPublicRole())
                    .setDeny(Permission.ALL_TEXT_PERMISSIONS).queue();
            text.createPermissionOverride(event.getGuild().getRolesByName(event.getChannelJoined().getName(), true).get(0))
                    .setAllow(voicepermission).queue();

        }
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild()
                .getRolesByName(event.getChannelJoined().getName(), true).get(0)).queue();

            SET_CHANNEL set_channel2 = CHANNEL.getSetChannel("voicelog", event.getGuild().getId());
            if (set_channel2.getMsg()) {
                Objects.requireNonNull(event.getGuild().getDefaultChannel())
                        .sendMessage("Du musst noch den Channel \"voicelog\" festlegen! \n `/channel set voicelog #channel`").queue();
            } else {
                TextChannel voicelog = event.getGuild().getTextChannelById(set_channel2.getChannel());
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.green);
                embed.setDescription("[" + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + "] **" + event.getVoiceState().getMember().getUser().getName() + "** joined **" + event.getChannelJoined().getName() + "**!");
                assert voicelog != null;
                voicelog.sendMessage(embed.build()).queue();
            }

    }

    //TODO: add more xp for narrators

    //TODO: On rename voice chat -> rename voice role as well


    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                boolean onlyBots = true;
                for (Member m : event.getChannelLeft().getMembers()) {
                    if (!m.getUser().isBot()) {
                        onlyBots = false;
                    }
                }
                if (onlyBots) {
                    event.getGuild().getAudioManager().setSendingHandler(null);
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }
        }
        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName(event.getChannelLeft().getName(), true).get(0)).queue();
        SET_CHANNEL set_channel = CHANNEL.getSetChannel("spam", event.getGuild().getId());
        if (set_channel.getMsg()) {
            Objects.requireNonNull(event.getGuild().getDefaultChannel())
                    .sendMessage("Du musst noch den Channel \"spam\" festlegen! \n `/channel set spam #channel`").queue();
        } else {
            if (event.getChannelLeft().getName().equals("\uD83D\uDCDA-Lyrikecke")) {
                if (event.getMember().getRoles().contains(event.getGuild()
                        .getRolesByName("mute", true).get(0)) && !event.getMember().getUser().isBot()) {
                    event.getGuild().removeRoleFromMember(event.getMember(),
                            event.getGuild().getRolesByName("mute", true).get(0)).queue();
                }
            }

                SET_CHANNEL set_channel3 = CHANNEL.getSetChannel("voicelog", event.getGuild().getId());
                if (set_channel3.getMsg()) {
                    Objects.requireNonNull(event.getGuild().getDefaultChannel())
                            .sendMessage("Du musst noch den Channel \"voicelog\" festlegen! \n `/channel set voicelog #channel`").queue();
                } else {
                    TextChannel voicelog = event.getGuild().getTextChannelById(set_channel3.getChannel());
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red);
                    embed.setDescription("[" + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + "] **" + event.getVoiceState().getMember().getUser().getName() + "** left **" + event.getChannelLeft().getName() + "**!");
                    assert voicelog != null;
                    voicelog.sendMessage(embed.build()).queue();
                }

        }
    }

    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        if (STATIC.getIsNarration() && !STATIC.getIsDiscussion()) {
            if (event.getChannelJoined().equals(event.getGuild().getVoiceChannelById("469209414218285057")) && !STATIC.getIsDiscussion()) {
                if (!event.getMember().getUser().isBot())
                    event.getMember().mute(true).queue();
            } else if (event.getChannelLeft().equals(event.getGuild().getVoiceChannelById("469209414218285057")) && !STATIC.getIsDiscussion()) {
                if (!event.getMember().getUser().isBot())
                    event.getMember().mute(false).queue();
            }
        }

        event.getGuild().addRoleToMember(event.getMember(), event.getGuild()
                .getRolesByName(event.getChannelJoined().getName(), true).get(0)).queue();
        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName(event.getChannelLeft().getName(), true).get(0)).queue();


            SET_CHANNEL set_channel4 = CHANNEL.getSetChannel("voicelog", event.getGuild().getId());
            if (set_channel4.getMsg()) {
                Objects.requireNonNull(event.getGuild().getDefaultChannel())
                        .sendMessage("Du musst noch den Channel \"voicelog\" festlegen! \n `/channel set voicelog #channel`").queue();
            } else {
                TextChannel voicelog = event.getGuild().getTextChannelById(set_channel4.getChannel());
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.orange);
                embed.setDescription("[" + java.time.LocalDate.now() + " " + java.time.LocalTime.now() + "] **" + event.getVoiceState().getMember().getUser().getName() + "** moved to **" + event.getChannelJoined().getName() + "**!");
                assert voicelog != null;
                voicelog.sendMessage(embed.build()).queue();
                if (event.getGuild().getRolesByName(event.getChannelJoined().getName(), true).isEmpty()) {
                    event.getGuild().createRole().setColor(Color.LIGHT_GRAY).setName(event.getChannelJoined().getName())
                            .setMentionable(true).setHoisted(false).queue();
                }

        }
    }

    @Override
    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {
        event.getGuild().getRolesByName(event.getChannel().getName(), true).get(0).delete().queue();
        try {
            event.getGuild().getTextChannelsByName(event.getChannel().getName(), true).get(0).delete().queue();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onVoiceChannelUpdateName(@NotNull VoiceChannelUpdateNameEvent event) {
        event.getGuild().getRolesByName(event.getOldName(), true).get(0).getManager().setName(event.getNewName()).queue();
    }

    @Override
    public boolean canReceiveCombined() {
        return false;
    }

    @Override
    public boolean canReceiveUser() {
        return false;
    }

    @Override
    public void handleCombinedAudio(@NotNull CombinedAudio combinedAudio) { }

    @Override
    public void handleUserAudio(@NotNull UserAudio userAudio) { }
}
