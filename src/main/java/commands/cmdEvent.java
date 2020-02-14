package commands;

import core.messageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.sql.SQLException;
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
    private void eventNarration(String[] args, MessageReceivedEvent event) throws SQLException {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(messageActions.getLocalizedString("event_narration_title", "server", event.getGuild().getId())
                .replace("[NARRATION]", "NARRATION")); //TODO: defining the title as a servermod in initalisation
        embed.setColor(Color.ORANGE);
        VoiceChannel voice;
        voice = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();
        //TODO: AFK-Timer anpassen!
        switch (args[1].toLowerCase()) {
            case "help":
                embed.setTitle(messageActions.getLocalizedString("event_help_title", "server", event.getGuild().getId())
                        .replace("[EVENT]", args[1]));
                if (args[2].equals("narration")) { //TODO: Add more events here!
                    embed.setDescription(
                            "```" + messageActions.getLocalizedString("event_help_" + args[1], "server", event.getGuild().getId()) + "```");
                } else {
                    embed.setDescription(messageActions.getLocalizedString("event_not_found", "server", event.getGuild().getId()));
                }

                break;
            case "start":/*
                if (voice == null) {
                    embed.setDescription("Du musst dich in einem VoiceChannel befinden!");
                } else {
                    for (Member m : voice.getMembers()) {
                        event.getGuild().addRoleToMember(m, event.getGuild().getRolesByName("mute", true).get(0)).queue();
                    }
                    embed.setDescription("Die Lyrikecke ist er\u00F6ffnet!");
                    String[] arguments2 = {"events", "id = '" + event.getGuild().getId() + "'", "1", "narration"};
                    String[] answer2;
                    answer2 = core.databaseHandler.database("serversettings", "select", arguments2);
                    StringBuilder sb = new StringBuilder();
                    assert answer2 != null;
                    String[] split = answer2[0].split("###");
                    if (answer2[0].contains("ISSTARTED:")) {
                        for (String string : split) {
                            if (string.contains("ISSTARTED:")) {
                                sb.append("ISSTARTED:TRUE###");
                            } else {
                                sb.append(string).append("###");
                            }
                        }
                    } else {
                        sb.append("ISSTARTED:TRUE###");
                        for (String string : split) {
                            sb.append(string).append("###");
                        }
                    }
                    String[] arguments3 = {"events", "id = '" + event.getGuild().getId() + "'", "narration", "'" + sb.toString() + "'"};
                    try {
                        core.databaseHandler.database(event.getGuild().getId(), "update", arguments3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/
                if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Leser", true).get(0)) ||
                    event.getMember().getRoles().contains(event.getGuild().getRolesByName("Vala", true).get(0)))   {
                    STATIC.changeIsLyrikabend(true);
                    for (Member member : event.getGuild().getVoiceChannelById("469209414218285057").getMembers()) {
                        try {
                            if (!member.getUser().isBot())
                                member.mute(true).queue();
                        } catch (Exception ignored) {
                        }
                    }
                }
                break;
            case "stop":
                /*
                if (voice == null) {
                    embed.setDescription("Du musst dich in einem VoiceChannel befinden!");
                } else {
                    for (Member m : voice.getMembers()) {
                        event.getGuild().removeRoleFromMember(m, event.getGuild().getRolesByName("mute", true).get(0)).queue();
                    }
                    embed.setDescription("Die Lyrikecke ist vorbei! Vielen Dank f\u00FCr eure Teilnahme!");
                    String[] arguments2 = {"events", "id = '" + event.getGuild().getId() + "'", "1", "narration"};
                    String[] answer2;
                    answer2 = core.databaseHandler.database("serversettings", "select", arguments2);
                    StringBuilder sb = new StringBuilder();
                    assert answer2 != null;
                    String[] split = answer2[0].split("###");
                    if (answer2[0].contains("ISSTARTED:")) {
                        for (String string : split) {
                            if (string.contains("ISSTARTED:")) {
                                sb.append("ISSTARTED:FALSE###");
                            } else {
                                sb.append(string).append("###");
                            }
                        }
                    } else {
                        sb.append("ISSTARTED:FALSE###");
                        for (String string : split) {
                            sb.append(string).append("###");
                        }
                    }
                    String[] arguments3 = {"events", "id = '" + event.getGuild().getId() + "'", "narration", "'" + sb.toString() + "'"};
                    try {
                        core.databaseHandler.database(event.getGuild().getId(), "update", arguments3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String[] arguments5 = {"events", "id = '" + event.getGuild().getId() + "'", "1", "narration"};
                    String[] answer3;
                    answer3 = core.databaseHandler.database("serversettings", "select", arguments5);
                    StringBuilder sb2 = new StringBuilder();
                    assert answer3 != null;
                    String[] split2 = answer3[0].split("###");
                    if (answer3[0].contains("NARRATOR:")) {
                        for (String string : split2) {
                            if (string.contains("NARRATOR:")) {
                                sb2.append("NARRATOR:").append("###");
                            } else {
                                sb2.append(string).append("###");
                            }
                        }
                    } else {
                        sb2.append("NARRATOR:###");
                        for (String string : split) {
                            sb2.append(string).append("###");
                        }
                    }
                    String[] arguments4 = {"events", "id = '" + event.getGuild().getId() + "'", "narration", "'" + sb2.toString() + "'"};
                    try {
                        core.databaseHandler.database(event.getGuild().getId(), "update", arguments4);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/
                if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Leser", true).get(0)) ||
                        event.getMember().getRoles().contains(event.getGuild().getRolesByName("Vala", true).get(0)))   {
                    STATIC.changeIsLyrikabend(false);

                    for (Member member : event.getGuild().getVoiceChannelById("469209414218285057").getMembers()) {
                        try {
                            member.mute(false).queue();
                        } catch (Exception ignored) {}


                    }
                }
                break;
                /*
            case "setup":
                if (voice == null) {
                    embed.setDescription("Du musst dich in einem VoiceChannel befinden!");
                } else {
                    Permission[] expertpermissionsetup1 = new Permission[]{Permission.VOICE_SPEAK};
                    try {
                        voice.createPermissionOverride(event.getGuild().getRolesByName("mute", true).get(0))
                                .setDeny(expertpermissionsetup1).queue();
                    } catch (Exception e) {
                        Objects.requireNonNull(voice.getPermissionOverride(event.getGuild().getRolesByName("mute", true).get(0)))
                                .getManager().deny(expertpermissionsetup1).queue();
                    }

                    embed.setDescription("Setup completed!");
                }
                break;*/
            case "discussion":
            case "diskussion":
            case "d":
                /*String target = null;
                if (voice == null) {
                    embed.setDescription("Du musst dich in einem VoiceChannel befinden!");
                } else {
                    switch (args[2].toLowerCase()) {
                        case "experte":
                            target = "f\u00fcr alle Experten";
                            for (Member m : voice.getMembers()) {
                                if (m.getRoles().contains(event.getGuild().getRolesByName("Experte", true).get(0))) {
                                    event.getGuild().removeRoleFromMember(m, event.getGuild().getRolesByName("mute", true).get(0)).queue();
                                }
                            }
                            break;
                        case "everyone":
                            target = "f\u00fcr alle Nutzer";
                            for (Member m : voice.getMembers()) {
                                event.getGuild().removeRoleFromMember(m, event.getGuild().getRolesByName("mute", true).get(0)).queue();
                            }
                            break;

                        case "stop":
                            target = "nicht mehr";
                            for (Member m : voice.getMembers()) {
                                event.getGuild().addRoleToMember(m, event.getGuild().getRolesByName("mute", true).get(0)).queue();
                            }
                            String[] arguments6 = {"events", "id = '" + event.getGuild().getId() + "'", "1", "narration"};
                            String[] answer6;
                            answer6 = core.databaseHandler.database("serversettings", "select", arguments6);
                            assert answer6 != null;
                            String[] split6 = answer6[0].split("###");
                            if (answer6[0].contains("NARRATOR:")) {
                                for (String string : split6) {
                                    if (string.contains("NARRATOR:")) {
                                        for (String id : string.replace("NARRATOR:", "").split(",")) {
                                            Member m = event.getGuild().getMemberById(id);
                                            assert m != null;
                                            event.getGuild().removeRoleFromMember(m, event.getGuild().getRolesByName("mute", true).get(0)).queue();
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }

                EmbedBuilder diskussion = new EmbedBuilder();
                diskussion.setColor(Color.ORANGE);
                diskussion.setTitle("EVENT LYRIKECKE");
                diskussion.setDescription("Die Diskussionen sind " + target + " er\u00f6ffnet!");
                assert voice != null;
                event.getGuild().getTextChannelsByName(voice.getName(), true).get(0).sendMessage(diskussion.build()).queue();*/
                if (STATIC.getIsLyrikabend()) {
                    if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Leser", true).get(0)) ||
                            event.getMember().getRoles().contains(event.getGuild().getRolesByName("Vala", true).get(0)))   {
                        for (Member member : event.getGuild().getVoiceChannelById("469209414218285057").getMembers()) {
                            try {
                                member.mute(false).queue();
                            } catch (Exception ignored) {}
                        }
                    }
                }
                break;
            case "narrator":
            case "erz\u00e4hler":
            case "geschichtenerz\u00e4hler":
            case "storyteller":
                switch (args[2]) {
                    /*case "get":
                        String[] arguments6 = {"events", "id = '" + event.getGuild().getId() + "'", "1", "narration"};
                        String[] answer6;
                        answer6 = core.databaseHandler.database("serversettings", "select", arguments6);
                        StringBuilder name_storyteller = new StringBuilder();
                        assert answer6 != null;
                        String[] split6 = answer6[0].split("###");
                        if (answer6[0].contains("NARRATOR:")) {
                            for (String string : split6) {
                                if (string.contains("NARRATOR:")) {
                                    for (String id : string.replace("NARRATOR:", "").split(",")) {
                                        name_storyteller.append(Objects.requireNonNull(event.getGuild().getMemberById(id)).getUser().getAsTag()).append(", ");
                                    }
                                    name_storyteller.replace(name_storyteller.length() - 2, name_storyteller.length(), "");
                                }
                            }
                        } else {
                            name_storyteller.append("nicht festgelegt");
                        }
                        embed.setDescription("Der Erz\u00E4hler ist momentan " + name_storyteller + "!");
                        break;*/
                    case "set":
                        /*String[] arguments2 = {"events", "id = '" + event.getGuild().getId() + "'", "1", "narration"};
                        String[] answer2;
                        answer2 = core.databaseHandler.database("serversettings", "select", arguments2);
                        StringBuilder sb = new StringBuilder();
                        assert answer2 != null;
                        String[] split = answer2[0].split("###");
                        assert args[3] != null;
                        Member narrator = event.getGuild().getMembersByEffectiveName(args[3], true).get(0);
                        if (answer2[0].contains("NARRATOR:")) {
                            for (String string : split) {
                                if (string.contains("NARRATOR:")) {
                                    sb.append("NARRATOR:");
                                    for (String id : string.replace("NARRATOR:", "").split(",")) {
                                        sb.append(id).append(",");
                                    }
                                    sb.append(narrator.getUser().getId()).append("###"); //TODO: make this work with different inputs besides effective name
                                } else {
                                    sb.append(string).append("###");
                                }
                            }
                        } else {
                            sb.append("NARRATOR:###");
                            for (String string : split) {
                                sb.append(string).append("###");
                            }
                        }
                        String[] arguments3 = {"events", "id = '" + event.getGuild().getId() + "'", "narration", "'" + sb.toString() + "'"};
                        try {
                            core.databaseHandler.database(event.getGuild().getId(), "update", arguments3);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        embed.setDescription("'" + sb.toString() + "'" + " ist nun Erz\u00E4hler!");
                        event.getGuild().removeRoleFromMember(narrator, event.getGuild().getRolesByName("mute", true).get(0)).queue();*/
                        if (STATIC.getIsLyrikabend()) {
                            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Leser", true).get(0)) ||
                                    event.getMember().getRoles().contains(event.getGuild().getRolesByName("Vala", true).get(0)))   {
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    int i = 3;
                                    while (i < args.length) {
                                        sb.append(args[i]);
                                        sb.append(" ");
                                        i++;
                                    }
                                    Member member = util.getUser.getMemberFromInput(sb.toString().split(" "), event.getAuthor(), event.getGuild(), event.getTextChannel());
                                    member.mute(false).queue();
                                } catch (Exception ignored) {}
                            }
                        }
                        break;
                    /*case "clear":
                        String[] arguments5 = {"events", "id = '" + event.getGuild().getId() + "'", "1", "narration"};
                        String[] answer3;
                        answer3 = core.databaseHandler.database("serversettings", "select", arguments5);
                        StringBuilder sb2 = new StringBuilder();
                        assert answer3 != null;
                        String[] split2 = answer3[0].split("###");
                        if (answer3[0].contains("NARRATOR:")) {
                            for (String string : split2) {
                                if (string.contains("NARRATOR:")) {
                                    sb2.append("NARRATOR:").append("###");
                                    for (String id : string.replace("NARRATOR:", "").split(",")) {
                                        event.getGuild().addRoleToMember(Objects.requireNonNull(event.getGuild().getMemberById(id)), event.getGuild().getRolesByName("mute", true).get(0)).queue();
                                    }
                                } else {
                                    sb2.append(string).append("###");
                                }
                            }
                        } else {
                            sb2.append("NARRATOR:###");
                            for (String string : split2) {
                                sb2.append(string).append("###");
                            }
                        }
                        String[] arguments4 = {"events", "id = '" + event.getGuild().getId() + "'", "narration", "'" + sb2.toString() + "'"};
                        try {
                            core.databaseHandler.database(event.getGuild().getId(), "update", arguments4);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        embed.setDescription("Kein User ist mehr Erz\u00E4hler!");

                        break;*/



                }

                break;
        }
        event.getTextChannel().sendMessage(embed.build()).queue();
    }


}

