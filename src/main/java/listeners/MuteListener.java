package listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MuteListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        Role mute;
        try {
            mute = event.getGuild().getRolesByName("mute", true).get(0);
        } catch (Exception e) {
            Permission[] perm = Permission.EMPTY_PERMISSIONS;
            event.getGuild().createRole().setName("mute").setPermissions(perm).queue();
            mute = event.getGuild().getRolesByName("mute", true).get(0);
        }
        if (event.getRoles().contains(mute) && !Objects.requireNonNull(event.getMember().getVoiceState()).isGuildMuted()) {
            event.getMember().mute(true).queue();
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        Role mute;
        try {
            mute = event.getGuild().getRolesByName("mute", true).get(0);
        } catch (Exception e) {
            Permission[] perm = Permission.EMPTY_PERMISSIONS;
            event.getGuild().createRole().setName("mute").setPermissions(perm).queue();
            mute = event.getGuild().getRolesByName("mute", true).get(0);
        }
        if (event.getRoles().contains(mute) && Objects.requireNonNull(event.getMember().getVoiceState()).isGuildMuted()) {
            event.getMember().mute(false).queue();
        }
    }
}

