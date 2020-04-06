package commands;

import core.permissionChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.getUser;

import java.util.ArrayList;
import java.util.List;

public class cmdExil implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (permissionChecker.checkRole(new Role[]{event.getGuild().getRolesByName("Vala", true).get(0)}, event.getMember())) {
            if (args.length > 0) {
                Member member = getUser.getMemberFromInput(args, event.getAuthor(), event.getGuild(), event.getTextChannel());
                Role exil = event.getGuild().getRolesByName("exil", true).get(0);
                List<Role> rolelist = new ArrayList<>();
                rolelist.add(event.getGuild().getRolesByName("dark-memes", true).get(0));
                rolelist.add(event.getGuild().getRolesByName("lyrikecke", true).get(0));
                rolelist.add(event.getGuild().getRolesByName("leser", true).get(0));
                rolelist.add(event.getGuild().getRolesByName("experte", true).get(0));
                rolelist.add(event.getGuild().getRolesByName("Lyrikabend - Verwalter", true).get(0));
                rolelist.add(event.getGuild().getRolesByName("verified", true).get(0));

                for (Role role : rolelist) {
                    try {
                        event.getGuild().removeRoleFromMember(member, role).queue();
                    } catch (Exception ignored) {
                    }
                }
                for (VoiceChannel vc : event.getGuild().getVoiceChannels()) {
                    try {
                        Role vcr = event.getGuild().getRolesByName(vc.getName(), true).get(0);
                        event.getGuild().removeRoleFromMember(member, vcr).queue();
                    } catch (Exception ignored) {
                    }
                }
                event.getGuild().addRoleToMember(member, exil).queue();

            } else {
                event.getTextChannel().sendMessage("Please provide an user.").queue();
            }
        } else {
            permissionChecker.noPower(event.getTextChannel());
        }

    }
}
