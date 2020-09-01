package commands;

import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.CHANNEL;
import util.SET_CHANNEL;
import util.GetUser;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class CmdKick implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.KICK_MEMBERS}, event.getMember())) {
            SET_CHANNEL set_channel = CHANNEL.getSetChannel("modlog", event.getGuild().getId());
            if (set_channel.getMsg()) {
                event.getChannel()
                        .sendMessage("Du musst noch den Channel \"modlog\" festlegen! \n `/channel set modlog #channel`").queue();
            } else {
                TextChannel modlog = event.getGuild().getTextChannelById(set_channel.getChannel());
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                    ArrayList<String> args2 = new ArrayList<>();
                    args2.add(args[0]);
                    String[] args3 = new String[args2.size()];
                    args3 = args2.toArray(args3);
                    Member member = GetUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getChannel());
                    User user = member.getUser();
                    StringBuilder sb = new StringBuilder();
                    int i = 1;
                    while (i < args.length) {
                        sb.append(args[i]);
                        sb.append(" ");
                        i++;
                    }
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red);
                    embed.setTitle(MessageActions.getLocalizedString("kick_title", "user", event.getAuthor().getId()));
                    embed.setDescription("**" + MessageActions.getLocalizedString("kick_msg_priv", "user", event.getAuthor().getId()) + "**\n" + sb.toString());
                    user.openPrivateChannel().queue(channel ->
                            channel.sendMessage(embed.build()).queue()
                    );
                    EmbedBuilder embed1 = new EmbedBuilder();
                    embed1.setColor(Color.RED);
                    embed1.setTitle(MessageActions.getLocalizedString("kick_title", "user", event.getAuthor().getId()));
                    embed1.setDescription(MessageActions.getLocalizedString("kick_msg", "server", event.getGuild().getId())
                            .replace("[USER]", user.getAsMention()).replace("[REASON]", sb.toString()));
                    assert modlog != null;
                    modlog.sendMessage(embed1.build()).queue(msg -> event.getGuild().kick(user.getId(), sb.toString()).queue());

                }
            }
        } else {
            PermissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }
    }


}
