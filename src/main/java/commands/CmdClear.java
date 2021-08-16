package commands;

import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CmdClear extends ListenerAdapter {

    private final EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);

    public static void clear(SlashCommandEvent event) {

        // only members with the permission "ADMINISTRATOR" are able to perform this command
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            //getting number of msgs that shall be deleted

            OptionMapping amountOption = event.getOption("clear_amount"); // This is configured to be optional so check for null
            int amount = amountOption == null
                    ? 100 // default 100
                    : (int) Math.min(200, Math.max(2, amountOption.getAsLong())); // enforcement: must be between 2-200
            event.getChannel().getIterableHistory()
                    .takeAsync(amount)
                    .thenAccept(event.getChannel()::purgeMessages);
            event.reply("Cleared " + amount + " messages!").queue(msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        } else {
            PermissionChecker.noPower(event);
        }
    }
}
