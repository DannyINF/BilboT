package commands;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CmdRole implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        String command;
        String role;
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.WHITE);

        String[] answer;
        answer = DatabaseHandler.database("serversettings", "select custom from ranks where id = '" + event.getGuild().getId() + "'");

        List<String> role_list;
        assert answer != null;
        if (answer[0] == null) {
            role_list = new ArrayList<>();
        } else {
            role_list = new ArrayList<>(Arrays.asList(answer[0].split("<#>")));
        }

        List<String> roles = new ArrayList<>();
        for (String str : role_list) {
            roles.add(str.split(":")[0]);
        }
        try {
            command = args[0];
        } catch (Exception e) {
            command = null;
        }
        try {
            role = args[1];
        } catch (Exception e) {
            role = null;
        }
        switch (command) {
            case "add":
                embed.setTitle("Rolle hinzuf\u00fcgen");
                embed.setDescription("Diese Rolle existiert nicht.");
                if (roles.contains(role.toLowerCase())) {
                    for (String str : role_list) {
                        if (str.split(":")[0].equals(role.toLowerCase())) {
                            Role add_role = event.getGuild().getRoleById(str.split(":")[1]);
                            assert add_role != null;
                            event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), add_role).queue();
                            embed.setDescription("Rolle '" + add_role.getName() + "' hinzugef\u00fcgt.");
                        }
                    }
                }
                break;

            case "remove":
                embed.setTitle("Rolle entfernen");
                embed.setDescription("Diese Rolle existiert nicht.");
                if (roles.contains(role.toLowerCase())) {
                    for (String str : role_list) {
                        if (str.split(":")[0].equals(role.toLowerCase())) {
                            Role remove_role = event.getGuild().getRoleById(str.split(":")[1]);
                            assert remove_role != null;
                            event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), remove_role).queue();
                            embed.setDescription("Rolle '" + remove_role.getName() + "' entfernt.");
                        }
                    }
                }
                break;

            case "create":
                embed.setTitle("Rolle erstellen");
                embed.setDescription("Diese Rolle existiert schon.");
                if (!roles.contains(role.toLowerCase())) {
                    if (event.getGuild().getRolesByName(role, true).size() > 0) {
                        Role create_role = event.getGuild().getRolesByName(role, true).get(0);
                        StringBuilder sb = new StringBuilder();
                        for (String str : role_list) {
                            sb.append(str).append("<#>");
                        }
                        sb.append(create_role.getName().toLowerCase()).append(":").append(create_role.getId());
                        try {
                            DatabaseHandler.database("serversettings", "update ranks set custom = '" + sb.toString() + "' where id = '" + event.getGuild().getId() + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        embed.setDescription("Rolle '" + create_role.getName() + "' erstellt.");
                    }
                }
                break;

            case "delete":
                embed.setTitle("Rolle l\u00f6schen");
                embed.setDescription("Diese Rolle existiert nicht.");
                if (roles.contains(role.toLowerCase())) {
                    if (event.getGuild().getRolesByName(role, true).size() > 0) {
                        Role delete_role = event.getGuild().getRolesByName(role, true).get(0);
                        role_list.remove(delete_role.getName().toLowerCase() + ":" + delete_role.getId());
                        StringBuilder sb = new StringBuilder();
                        for (String str : role_list) {
                            sb.append(str).append("<#>");
                        }
                        try {
                            DatabaseHandler.database("serversettings", "update ranks set custom = '" + sb.toString() + "' where id = '" + event.getGuild().getId() + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        embed.setDescription("Rolle '" + delete_role.getName() + "' gel\u00f6scht.");
                    }
                }
                break;

            case "list":
            default:
                embed.setTitle("Liste aller Rollen");
                StringBuilder sb = new StringBuilder();
                if (roles.isEmpty()) {
                    embed.setDescription("Momentan gibt es keine Rollen, die man sich selbst zuweisen kann.");
                } else {
                    for (String str : roles) {
                        sb.append(str).append(", ");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    embed.setDescription(sb.toString());
                }

                break;


        }
        event.getChannel().sendMessage(embed.build()).queue();
    }
}
