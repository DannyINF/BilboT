package commands;

import core.DatabaseHandler;
import core.PermissionChecker;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.GetUser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CmdExil {

    public static void exil(SlashCommandEvent event, Member member) throws SQLException {

        if (PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember()) || PermissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName(".", true).get(0)}, event.getMember())) {
            exileMember(event.getGuild(), member);
            event.reply("Done!").queue();
        } else {
            PermissionChecker.noPower(event);
        }
    }
    public static void exileMember(Guild guild, Member member) throws SQLException {
        String[] answer = DatabaseHandler.database(guild.getId(), "select id from exil");
        
        Role exil = guild.getRolesByName("exil", true).get(0);
        assert member != null;
        assert answer != null;
        boolean isExil = false;
        for (String str : answer) {
            if (str.equals(member.getId()))
                isExil = true;
        }
        if (isExil) {
            String[] rolesFromDB = DatabaseHandler.database(guild.getId(), "select roles from exil where id = '" + member.getId() + "'");
            guild.removeRoleFromMember(member, exil).queue();
            assert rolesFromDB != null;
            for (String id : rolesFromDB[0].split(",")) {
                try {
                    guild.addRoleToMember(member, guild.getRoleById(id)).queue();
                } catch (Exception ignored) {}
            }
            DatabaseHandler.database(guild.getId(), "delete from exil where id = '" + member.getId() + "'");
        } else {
            //TODO: implement duration
            int duration = -1;
            StringBuilder sb = new StringBuilder();
            List<String> voiceroles = new ArrayList<>();
            for (VoiceChannel vc : guild.getVoiceChannels()) {
                voiceroles.add(vc.getName().toLowerCase());
            }
            for (Role role : member.getRoles()) {
                if (!voiceroles.contains(role.getName().toLowerCase())) {
                    sb.append(role.getId());
                    sb.append(",");
                }
                guild.removeRoleFromMember(member, role).queue();
            }
            sb.deleteCharAt(sb.length()-1);

            DatabaseHandler.database(guild.getId(), "insert into exil (id, roles, duration) values ('" + member.getId() + "', '" + sb.toString() + "', " + duration + ")");

            guild.addRoleToMember(member, exil).queue();
            try {
                guild.kickVoiceMember(member).queue();
            } catch (Exception ignored) {}

        }
    }
}
