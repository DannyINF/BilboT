package listeners;

import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;


public class chatfilterListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //if (isReady.isReady(event.getGuild())) {
        String status = "deactivated";
        try {
            status = modulesChecker.moduleStatus("chatfilter", event.getGuild().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (status.equals("activated")) {
            if (!event.getAuthor().equals(event.getJDA().getSelfUser())) {
                SET_CHANNEL set_channel = CHANNEL.getSetChannel("modlog", event.getGuild().getId());
                if (set_channel.getMsg()) {
                    messageActions.neededChannel(event);
                } else {
                    TextChannel modlog = event.getGuild().getTextChannelById(set_channel.getChannel());
                    String[] ban = {"pisser", "pissa", "hure", "fick", "fotze", "brezelsalzabpopler", "inzest", "bastard", "spast",
                            "wichser", "wixxer", "\u5350", "npd", "nsdap", "hitler", "hodenkobold", "arschloch", "Milf", "slut",
                            "Hurensohn", "Huhrensohn", "Mongo", "gay", "Schwuchtel", "Neger", "Nigga"};
                    StringBuilder sb = new StringBuilder();
                    boolean writeReport = false;
                    for (String filter : ban) {
                        if (event.getMessage().getContentRaw().toLowerCase().replace(" ", "").replace("\n", "").contains(filter.toLowerCase())) {
                            sb.append(filter).append(", ");
                            writeReport = true;
                        }
                    }
                    if (writeReport) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.RED);
                        embed.setTitle(messageActions.getLocalizedString("report_auto_title", "server", event.getGuild().getId()));
                        URL jump = null;
                        try {
                            jump = new URL("https://discordapp.com/channels/" + event.getGuild().getId() + "/" + event.getTextChannel().getId() + "/" + event.getMessageId());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        embed.setDescription(messageActions.getLocalizedString("report_auto_msg", "server", event.getGuild().getId())
                                .replace("[CHANNEL]", event.getTextChannel().getAsMention())
                                .replace("[MESSAGE]", "`" + event.getMessage().getContentRaw() + "`")
                                .replace("[USER]", "**" + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + "**") +
                                "\n" + sb.toString().substring(0, sb.toString().length() - 2) +
                                "\n[jump](" + jump + ")");
                        assert modlog != null;
                        modlog.sendMessage(embed.build()).queue();
                    }
                }
            }
        }
    }
}

//}
