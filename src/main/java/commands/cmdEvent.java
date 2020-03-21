package commands;

import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class cmdEvent implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws SQLException {
        String chosen_event;
        try {
            chosen_event = args[0];
        } catch (Exception e) {
            chosen_event = null;
        }
        switch (chosen_event.toLowerCase()) {
            case "narration":
                eventNarration(args, event);
                break;
            default:
                EmbedBuilder embed = new EmbedBuilder();
                embed.setDescription(messageActions.getLocalizedString("event_not_found", "server", event.getGuild().getId()));
                embed.setTitle(messageActions.getLocalizedString("event_title", "server", event.getGuild().getId()));
                embed.setColor(Color.RED);
                event.getTextChannel().sendMessage(embed.build()).queue();
        }
    }

    /**
     * @param event MessageReceivedEvent
     */
    private void eventNarration(String[] args, MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.decode("#42adf5"));
        embed.setTitle("Narration");
        embed.setTimestamp(Instant.now());
        switch (args[1]) {
            case "help":
                //help
                break;
            case "start":
                if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                        permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                    if (!(event.getMember().getVoiceState() == null) && event.getMember().getVoiceState().getChannel().getId().equals("469209414218285057")) {
                        STATIC.changeIsNarration(true);
                        STATIC.changeNarrationChannel(event.getMember().getVoiceState().getChannel());
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
                        embed.setDescription("You need to join the voice channel **" + event.getGuild().getVoiceChannelById("469209414218285057").getName() + "** to execute this command.");
                    }
                } else {
                    embed.setColor(Color.RED);
                    embed.setDescription("You need the role **Leser** to execute this command.");
                }
                break;
            case "stop":
                if (STATIC.getIsNarration()) {
                    if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                        permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                        if (!(event.getMember().getVoiceState() == null) && event.getMember().getVoiceState().getChannel().getId().equals("469209414218285057")) {
                            STATIC.changeIsNarration(false);
                            STATIC.changeIsDiscussion(false);

                            for (Member member : event.getGuild().getMembers()) {
                                try {
                                    member.mute(false).queue();
                                } catch (Exception ignored) {}
                            }
                            embed.setDescription("The narration has ended.");
                        } else {
                            embed.setColor(Color.RED);
                            embed.setDescription("You need to join the voice channel **" + event.getGuild().getVoiceChannelById("469209414218285057").getName() + "** to execute this command.");
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
                switch (args[2]) {
                    case "start":
                        if (STATIC.getIsNarration()) {
                            if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                        permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                                if (!(event.getMember().getVoiceState() == null) && event.getMember().getVoiceState().getChannel().getId().equals("469209414218285057")) {
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
                                    embed.setDescription("You need to join the voice channel **" + event.getGuild().getVoiceChannelById("469209414218285057").getName() + "** to execute this command.");
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
                    case "stop":
                        if (STATIC.getIsNarration()) {
                            if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                        permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                                if (!(event.getMember().getVoiceState() == null) && event.getMember().getVoiceState().getChannel().getId().equals("469209414218285057")) {
                                    STATIC.changeIsDiscussion(false);

                                    for (Member member : STATIC.getNarrationChannel().getMembers()) {
                                        if (!Arrays.asList(STATIC.getReaders()).contains(member)) {
                                            try {
                                                member.mute(false).queue();
                                            } catch (Exception ignored) {}
                                        }
                                    }
                                    embed.setDescription("The discussion has ended.");
                                } else {
                                    embed.setColor(Color.RED);
                                    embed.setDescription("You need to join the voice channel **" + event.getGuild().getVoiceChannelById("469209414218285057").getName() + "** to execute this command.");
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
                break;
            case "narrator":
            case "storyteller":
            case "reader":
                switch (args[2]) {
                    case "set":
                    case "add":
                        if (STATIC.getIsNarration()) {
                            if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Reader", true).get(0)}, event.getMember()))   {
                                if (!(event.getMember().getVoiceState() == null) && event.getMember().getVoiceState().getChannel().getId().equals("469209414218285057")) {//689597232319954966
                                    try {
                                        StringBuilder sb = new StringBuilder();
                                        int i = 2;
                                        while (i < args.length) {
                                            sb.append(args[i]);
                                            sb.append(" ");
                                            i++;
                                        }
                                        Member member = util.getUser.getMemberFromInput(sb.toString().split(" "), event.getAuthor(), event.getGuild(), event.getTextChannel());
                                        member.mute(false).queue();
                                        java.util.List<Member> members = null;
                                        members.add(member);
                                        STATIC.addReader(members);
                                        embed.setDescription("**" + member.getUser().getAsTag() + "** is now a narrator.");
                                    } catch (Exception ignored) {
                                    }
                                } else {
                                    embed.setColor(Color.RED);
                                    embed.setDescription("You need to join the voice channel **" + event.getGuild().getVoiceChannelById("469209414218285057").getName() + "** to execute this command.");
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
                    case "remove":
                    case "delete":
                        if (STATIC.getIsNarration()) {
                            if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Reader", true).get(0)}, event.getMember()))   {
                                if (!(event.getMember().getVoiceState() == null) && event.getMember().getVoiceState().getChannel().getId().equals("469209414218285057")) {
                                    try {
                                        StringBuilder sb = new StringBuilder();
                                        int i = 2;
                                        while (i < args.length) {
                                            sb.append(args[i]);
                                            sb.append(" ");
                                            i++;
                                        }
                                        Member member = util.getUser.getMemberFromInput(sb.toString().split(" "), event.getAuthor(), event.getGuild(), event.getTextChannel());
                                        member.mute(true).queue();
                                        java.util.List<Member> members = null;
                                        members.add(member);
                                        STATIC.removeReader(members);
                                        embed.setDescription("**" + member.getUser().getAsTag() + "** is no longer a narrator.");
                                    } catch (Exception ignored) {
                                    }
                                } else {
                                    embed.setColor(Color.RED);
                                    embed.setDescription("You need to join the voice channel **" + event.getGuild().getVoiceChannelById("469209414218285057").getName() + "** to execute this command.");
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
                    case "get":
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
                    case "clear":
                        if (STATIC.getIsNarration()) {
                            if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) ||
                        permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Leser", true).get(0)}, event.getMember())) {
                                if (!(event.getMember().getVoiceState() == null) && event.getMember().getVoiceState().getChannel().getId().equals("469209414218285057")) {
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
                                    embed.setDescription("You need to join the voice channel **" + event.getGuild().getVoiceChannelById("469209414218285057").getName() + "** to execute this command.");
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
                break;
        }
        event.getTextChannel().sendMessage(embed.build()).queue();
    }


}

