package commands;

import core.messageActions;
import core.modulesChecker;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class cmdClear implements Command {


    private final EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);

    private int getInt(String string) {
        if (string.equals("channel")) {
            string = "100";
        }

        try {
            return Integer.parseInt(string) + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws SQLException {
        String status;

        // checking for activation
        status = modulesChecker.moduleStatus("clear", event.getGuild().getId());
        if (status.equals("activated")) {
            // only members with the permission "ADMINISTRATOR" are able to perform this command
            if (core.permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
                //getting number of msgs that shall be deleted
                int num = getInt(args[0]);
                if (num > 1 && num <= 100) {
                    try {
                        // getting all msgs of the textchannel
                        MessageHistory history = new MessageHistory(event.getTextChannel());
                        List<Message> msgs;

                        event.getMessage().delete().queue();

                        // storing the last x messages in the list msgs (x equals the number from args[0])
                        msgs = history.retrievePast(num).complete();

                        // deleting all messages from "msgs"
                        event.getTextChannel().deleteMessages(msgs).queue();

                        // preparing and sending msg (information: how many msgs were deleted)
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setTitle(messageActions.getLocalizedString("clear_deleted_title", "user", event.getAuthor().getId()));
                        embed.setColor(Color.red);
                        embed.setDescription(messageActions.getLocalizedString("clear_deleted_msg", "user", event.getAuthor().getId())
                                .replace("[COUNT]", args[0]));
                        Message msg = event.getTextChannel().sendMessage(embed.build()).complete();

                        // destroying itself after 3 seconds
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                msg.delete().queue();
                            }
                        }, 3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (num < 1) {
                        event.getTextChannel().sendMessage(error.setDescription(messageActions.getLocalizedString("clear_error_no_number", "user", event.getAuthor().getId())).build()).queue();
                    } else {
                        event.getTextChannel().sendMessage(error.setDescription(messageActions.getLocalizedString("clear_error_wrong_number", "user", event.getAuthor().getId())).build()).queue();
                    }

                }
            } else {
                permissionChecker.noPower(event.getTextChannel());
            }

        } else {
            messageActions.moduleIsDeactivated(event, "clear");
        }

    }


}
