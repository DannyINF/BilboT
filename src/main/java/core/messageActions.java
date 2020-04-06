package core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;

public class messageActions {
    public static void selfDestroyMSG(Message msg, int time_in_millis, MessageReceivedEvent event) {
        Message send = event.getTextChannel().sendMessage(msg).complete();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                send.delete().queue();
            }
        }, time_in_millis);
    }

    public static void selfDestroyEmbedMSG(MessageEmbed msg, int time_in_millis, MessageReceivedEvent event) {
        Message send = event.getTextChannel().sendMessage(msg).complete();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                send.delete().queue();
            }
        }, time_in_millis);
    }

    public static void selfDestroyEmbedMSG(MessageEmbed msg, int time_in_millis, GuildVoiceJoinEvent event) {
        Message send = event.getGuild().getTextChannelsByName(event.getChannelJoined().getName(), true).get(0).sendMessage(msg).complete();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                send.delete().queue();
            }
        }, time_in_millis);
    }

    public static void logCommand(MessageReceivedEvent event) {
        StringBuilder command = new StringBuilder();
        for (String s : event.getMessage().getContentRaw().split(" ")) {
            command.append(s);
            command.append(" ");
        }
        channelActions.getChannel(event, "cmdlog").sendMessage(
                messageActions.getLocalizedString("log_command", "server", event.getGuild().getId())
                        .replace("[COMMAND]", command).replace("[USER]", event.getAuthor().getAsTag()).replace("[CHAT]", event.getTextChannel().getAsMention())
        ).queue();
    }

    /**
     * @param string   string you want to insert (e.g. test_test)
     * @param kind_msg where does the string belong to (serversettings [type "server"] or usersettings [type "user])
     * @param id       id of server or user
     */
    public static String getLocalizedString(String string, String kind_msg, String id) {
        String localizedString;
        String lang;
        String country;
        String table_name = null;
        String db_name = null;
        String[] answer = null;


        switch (kind_msg) {
            case "server":
                db_name = "serversettings";
                table_name = "msgs";
                break;
            case "user":
                db_name = "usersettings";
                table_name = "users";
                break;
        }
        String[] arguments = {table_name, "id = '" + id + "'", "2", "language", "country"};

        try {
            answer = databaseHandler.database(db_name, "select", arguments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        lang = answer[0];
        country = answer[1];

        Locale currentLocale = new Locale(lang, country);
        localizedString = ResourceBundle.getBundle("MessageBundle", currentLocale).getString(string);
        localizedString = localizedString.replace("ö", "\u00f6");
        localizedString = localizedString.replace("Ö", "\u00d6");
        localizedString = localizedString.replace("ä", "\u00e4");
        localizedString = localizedString.replace("Ä", "\u00c4");
        localizedString = localizedString.replace("ü", "\u00fc");
        localizedString = localizedString.replace("Ü", "\u00dc");
        localizedString = localizedString.replace("ß", "\u00df");
        localizedString = localizedString.replace("ẞ", "\u1e9e");
        return localizedString;
    }

    public static void neededChannel(MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())).queue();
    }

    public static void neededChannel(GuildBanEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void neededChannel(TextChannelCreateEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void neededChannel(TextChannelDeleteEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void neededChannel(net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void neededChannel(net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void neededChannel(GuildVoiceJoinEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();

    }

    public static void neededChannel(GuildMemberRemoveEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void neededChannel(UserUpdateOnlineStatusEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void neededChannel(GuildMemberJoinEvent event, String channel) {
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(getLocalizedString("channel_need", "server", event.getGuild().getId())
                .replace("[CHANNEL]", channel)).queue();
    }

    public static void moduleIsDeactivated(MessageReceivedEvent event, String module) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(getLocalizedString("modules_title", "server", event.getGuild().getId()));
        embed.setColor(Color.RED);
        embed.setDescription(getLocalizedString("modules_is_deactivated", "server", event.getGuild().getId())
                .replace("[MODUL]", module));
        event.getTextChannel().sendMessage(embed.build()).queue();
    }


}
