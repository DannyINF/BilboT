package commands;

import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.CHANNEL;
import util.SET_CHANNEL;
import util.GetUser;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class CmdBan {

    public static void ban(SlashCommandEvent event, User user, int del_days, String reason) {
        event.deferReply(true).queue();
        // only members with the ban permission are able to ban using this command
        if (PermissionChecker.checkPermission(new Permission[]{Permission.BAN_MEMBERS}, event.getMember())) {
            SET_CHANNEL set_channel = CHANNEL.getSetChannel("modlog", event.getGuild().getId());
            if (set_channel.getMsg()) {
                MessageActions.neededChannel(event);
            } else {
                TextChannel modlog = event.getGuild().getTextChannelById(set_channel.getChannel());

                // setting up and sending the msg for the user
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.red);
                embed.setTitle(MessageActions.getLocalizedString("banned_msg_title", "user", event.getUser().getId()));
                embed.setDescription("**" + MessageActions.getLocalizedString("banned_msg_content", "user", event.getUser().getId()) + "**\n" + reason);
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessage(embed.build()).queue()
                );
                // setting up and sending the msg for the #modlog
                EmbedBuilder embed1 = new EmbedBuilder();
                embed1.setColor(Color.red);
                embed1.setTitle(MessageActions.getLocalizedString("banned_msg_title", "user", event.getUser().getId()));
                embed1.setDescription(MessageActions.getLocalizedString("log_mod_ban", "server", event.getGuild().getId())
                        .replace("[USER]", user.getAsTag()).replace("[REASON]", reason));
                assert modlog != null;
                modlog.sendMessage(embed1.build()).queue(msg -> event.getGuild().ban(user, del_days, reason).queue());

            }
        } else {
            PermissionChecker.noPower(event);
        }
    }
}
