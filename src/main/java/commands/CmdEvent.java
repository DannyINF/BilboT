package commands;

import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import util.STATIC;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class CmdEvent {
    //TODO: comment
    public static void narration(SlashCommandEvent event) {
        eventNarration(event);
    }

    /**
     * @param event GuildMessageReceivedEvent
     */
    private static void eventNarration(SlashCommandEvent event) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.decode("#42adf5"));
        embed.setTitle("Narration");
        embed.setTimestamp(Instant.now());
        switch (event.getSubcommandName()) {
            case "help":
                //help
                break;
            case "start":
                if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                        PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                    if (!(Objects.requireNonNull(event.getMember()).getVoiceState() == null) && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals("469209414218285057")) {
                        STATIC.changeIsNarration(true);
                        STATIC.changeNarrationChannel(event.getMember().getVoiceState().getChannel());
                        event.getGuild().getManager().setAfkTimeout(Guild.Timeout.SECONDS_3600).queue();
                        for (Member member : STATIC.getNarrationChannel().getMembers()) {
                            try {
                                if (!member.getUser().isBot())
                                    member.mute(true).queue();
                            } catch (Exception ignored) {
                            }
                        }
                        embed.setDescription("The narration has begun.");
                    } else {
                        embed.setColor(Color.RED);
                        embed.setDescription("You need to join the voice channel **" + Objects.requireNonNull(event.getGuild().getVoiceChannelById("469209414218285057")).getName() + "** to execute this command.");
                    }
                } else {
                    embed.setColor(Color.RED);
                    embed.setDescription("You need the role **Leser** to execute this command.");
                }
                break;
            case "stop":
                if (STATIC.getIsNarration()) {
                    if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                        PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                        if (!(Objects.requireNonNull(event.getMember()).getVoiceState() == null) && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals("469209414218285057")) {
                            STATIC.changeIsNarration(false);
                            STATIC.changeIsDiscussion(false);
                            event.getGuild().getManager().setAfkTimeout(Guild.Timeout.SECONDS_300).queue();

                            for (Member member : event.getGuild().getMembers()) {
                                try {
                                    member.mute(false).queue();
                                } catch (Exception ignored) {}
                            }
                            embed.setDescription("The narration has ended.");
                        } else {
                            embed.setColor(Color.RED);
                            embed.setDescription("You need to join the voice channel **" + Objects.requireNonNull(event.getGuild().getVoiceChannelById("469209414218285057")).getName() + "** to execute this command.");
                        }
                    } else {
                        embed.setColor(Color.RED);
                        embed.setDescription("You need the role **Leser** to execute this command.");
                    }
                } else {
                    embed.setColor(Color.RED);
                    embed.setDescription("The narration has not started yet.");
                }
                break;
            case "discussion":
                if (!STATIC.getIsDiscussion()) {
                    if (STATIC.getIsNarration()) {
                        if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                                PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                            if (!(Objects.requireNonNull(event.getMember()).getVoiceState() == null) && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals("469209414218285057")) {
                                STATIC.changeIsDiscussion(true);

                                for (Member member : STATIC.getNarrationChannel().getMembers()) {
                                    try {
                                        member.mute(false).queue();
                                    } catch (Exception ignored) {
                                    }
                                }
                                embed.setDescription("The discussion has begun.");

                            } else {
                                embed.setColor(Color.RED);
                                embed.setDescription("You need to join the voice channel **" + Objects.requireNonNull(event.getGuild().getVoiceChannelById("469209414218285057")).getName() + "** to execute this command.");
                            }
                        } else {
                            embed.setColor(Color.RED);
                            embed.setDescription("You need the role **Leser** to execute this command.");
                        }
                    } else {
                        embed.setColor(Color.RED);
                        embed.setDescription("The narration has not started yet.");
                    }
                } else {
                    if (STATIC.getIsNarration()) {
                        if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                                PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                            if (!(Objects.requireNonNull(event.getMember()).getVoiceState() == null) && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals("469209414218285057")) {
                                STATIC.changeIsDiscussion(false);

                                for (Member member : STATIC.getNarrationChannel().getMembers()) {
                                    if (!Objects.equals(STATIC.getReaders(), member)) {
                                        try {
                                            member.mute(false).queue();
                                        } catch (Exception ignored) {}
                                    }
                                }
                                embed.setDescription("The discussion has ended.");
                            } else {
                                embed.setColor(Color.RED);
                                embed.setDescription("You need to join the voice channel **" + Objects.requireNonNull(event.getGuild().getVoiceChannelById("469209414218285057")).getName() + "** to execute this command.");
                            }
                        } else {
                            embed.setColor(Color.RED);
                            embed.setDescription("You need the role **Reader** or **Moderator** to execute this command.");
                        }
                    } else {
                        embed.setColor(Color.RED);
                        embed.setDescription("The narration has not started yet.");
                    }
                }

            case "set-reader":
                if (STATIC.getIsNarration()) {
                    if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                            PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                        if (!(Objects.requireNonNull(event.getMember()).getVoiceState() == null) && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals("469209414218285057")) {//689597232319954966
                            Member member = event.getOption("narration_set_user").getAsMember();
                            member.mute(false).queue();
                            java.util.List<Member> members = null;
                            Objects.requireNonNull(members).add(member);
                            STATIC.addReader(members);
                            embed.setDescription("**" + member.getUser().getAsTag() + "** is now a narrator.");
                        } else {
                            embed.setColor(Color.RED);
                            embed.setDescription("You need to join the voice channel **" + Objects.requireNonNull(event.getGuild().getVoiceChannelById("469209414218285057")).getName() + "** to execute this command.");
                        }
                    } else {
                        embed.setColor(Color.RED);
                        embed.setDescription("You need the role **Leser** to execute this command.");
                    }
                } else {
                    embed.setColor(Color.RED);
                    embed.setDescription("The narration has not started yet.");
                }
                break;
            case "remove-reader":
                /*if (STATIC.getIsNarration()) {
                    if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                            PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                        if (!(Objects.requireNonNull(event.getMember()).getVoiceState() == null) && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals("469209414218285057")) {
                                Member member = event.getOption("narration_remove_user").getAsMember();
                                member.mute(true).queue();
                                java.util.List<Member> members = null;
                                Objects.requireNonNull(members).add(member);
                                STATIC.removeReader(members);
                                embed.setDescription("**" + member.getUser().getAsTag() + "** is no longer a narrator.");

                        } else {
                            embed.setColor(Color.RED);
                            embed.setDescription("You need to join the voice channel **" + Objects.requireNonNull(event.getGuild().getVoiceChannelById("469209414218285057")).getName() + "** to execute this command.");
                        }
                    } else {
                        embed.setColor(Color.RED);
                        embed.setDescription("You need the role **Leser** to execute this command.");
                    }
                } else {
                    embed.setColor(Color.RED);
                    embed.setDescription("The narration has not started yet.");
                }*/
                break;
            case "get-reader":
                if (STATIC.getIsNarration()) {
                    List<Member> readers = STATIC.getReaders();
                    if (readers.size() == 0)
                        embed.setDescription("Currently there are no narrators.");
                    else if (readers.size() == 1)
                        embed.setDescription("Currently **" + readers.get(0).getUser().getAsTag() + "** is the narrator.");
                    else {
                        StringBuilder narrators = new StringBuilder();
                        int i = 0;
                        while (i < readers.size() - 1) {
                            narrators.append("**").append(readers.get(i).getUser().getAsTag()).append("**, ");
                            i++;
                        }
                        i++;
                        narrators.append("and **").append(readers.get(i).getUser().getAsTag()).append("**");
                        embed.setDescription("Currently " + narrators.toString() + " are the narrators.");
                    }
                } else {
                    embed.setColor(Color.RED);
                    embed.setDescription("The narration has not started yet.");
                }
                break;
            case "clear-reader":
                if (STATIC.getIsNarration()) {
                    if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                        if (!(Objects.requireNonNull(event.getMember()).getVoiceState() == null) && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getId().equals("469209414218285057")) {
                            STATIC.clearReaders();
                            for (Member member : STATIC.getNarrationChannel().getMembers()) {
                                try {
                                    if (!member.getUser().isBot())
                                        member.mute(true).queue();
                                } catch (Exception ignored) {
                                }
                            }
                            embed.setDescription("The list of narrators has been cleared.");
                        } else {
                            embed.setColor(Color.RED);
                            embed.setDescription("You need to join the voice channel **" + Objects.requireNonNull(event.getGuild().getVoiceChannelById("469209414218285057")).getName() + "** to execute this command.");
                        }
                    } else {
                        embed.setColor(Color.RED);
                        embed.setDescription("You need the role **Leser** to execute this command.");
                    }
                } else {
                    embed.setColor(Color.RED);
                    embed.setDescription("The narration has not started yet.");
                }
                break;
        }
        event.replyEmbeds(embed.build()).queue();
    }
}