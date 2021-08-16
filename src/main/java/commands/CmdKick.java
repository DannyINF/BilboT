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

public class CmdKick {

    public static void kick(SlashCommandEvent event) {

        if (PermissionChecker.checkPermission(new Permission[]{Permission.KICK_MEMBERS}, event.getMember())) {
            SET_CHANNEL set_channel = CHANNEL.getSetChannel("modlog", event.getGuild().getId());
            if (set_channel.getMsg()) {
                event.getChannel()
                        .sendMessage("Du musst noch den Channel \"modlog\" festlegen! \n `/channel set modlog #channel`").queue();
            } else {
                TextChannel modlog = event.getGuild().getTextChannelById(set_channel.getChannel());
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red);
                    embed.setTitle(MessageActions.getLocalizedString("kick_title", "user", event.getUser().getId()));
                    embed.setDescription("**" + MessageActions.getLocalizedString("kick_msg_priv", "user", event.getUser().getId()) + "**\n" + event.getOption("kick_reason").getAsString());
                    event.getOption("kick_user").getAsUser().openPrivateChannel().queue(channel ->
                            channel.sendMessage(embed.build()).queue()
                    );
                    EmbedBuilder embed1 = new EmbedBuilder();
                    embed1.setColor(Color.RED);
                    embed1.setTitle(MessageActions.getLocalizedString("kick_title", "user", event.getUser().getId()));
                    embed1.setDescription(MessageActions.getLocalizedString("kick_msg", "server", event.getGuild().getId())
                            .replace("[USER]", event.getOption("kick_user").getAsUser().getAsMention()).replace("[REASON]", event.getOption("kick_reason").getAsString()));
                    assert modlog != null;
                    modlog.sendMessage(embed1.build()).queue(msg -> event.getGuild().kick(event.getOption("kick_user").getAsUser().getId(), event.getOption("kick_reason").getAsString()).queue());
                }
            }
        } else {
            PermissionChecker.noPower(event);
        }
    }
}