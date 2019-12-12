package listeners;

import core.channelActions;
import core.databaseHandler;
import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.awt.*;
import java.sql.SQLException;

import static java.lang.Boolean.FALSE;


public class joinListener extends ListenerAdapter {
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String status1 = null;
        try {
            status1 = modulesChecker.moduleStatus("joining", event.getGuild().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert status1 != null;
        if (status1.equals("activated")) {
            SET_CHANNEL set_channel = CHANNEL.getSetChannel("log", event.getGuild().getId());
            if (set_channel.getMsg()) {
                messageActions.neededChannel(event, "log");
            } else {
                TextChannel welcome1 = event.getGuild().getDefaultChannel();

                channelActions.getChannel(event, "log").sendMessage(messageActions.getLocalizedString("log_user_join", "server", event.getGuild().getId())
                        .replace("[USER]", event.getUser().getAsTag())).queue();
                if (event.getMember().getUser().isBot() == FALSE) {
                    String[] arguments2 = {"users", "id = '" + event.getUser().getId() + "'", "1", "verified"};
                    String[] answer2 = null;
                    try {
                        answer2 = databaseHandler.database("usersettings", "select", arguments2);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    boolean isVerifiedUser = false;
                    try {
                        assert answer2 != null;
                        if (answer2[0].toLowerCase().equals("true")) {
                            isVerifiedUser = true;
                        }
                    } catch (Exception ignored) {
                    }

                    String[] arguments3 = {"users", "id = '" + event.getUser().getId() + "'", "1", "verifystatus"};
                    String[] answer3 = null;
                    try {
                        answer3 = databaseHandler.database(event.getGuild().getId(), "select", arguments3);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    boolean isVerifiedServer = false;
                    try {
                        assert answer3 != null;
                        if (answer3[0].toLowerCase().equals("true")) {
                            isVerifiedServer = true;
                        }
                    } catch (Exception ignored) {
                    }

                    if (isVerifiedUser && isVerifiedServer) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("verified", true).get(0)).queue();
                        assert welcome1 != null;
                        welcome1.sendMessage("Mae govannen " + event.getMember().getAsMention() + "!").queue();
                    } else {
                        assert welcome1 != null;
                        welcome1.sendMessage(":flag_de: Mae govannen " + event.getMember().getAsMention() + "! Um auf den Server zugreifen zu k\u00f6nnen, musst du dich erst verifizieren. " +
                                "Klicke dafür auf das :white_check_mark:-Emoji unter dieser Nachricht.\n\n" +
                                ":flag_gb: Mae govannen " + event.getMember().getAsMention() + "! In order to acces the server, you have to verify yourself first. " +
                                "Therefore, you have to click the :white_check_mark: emoji below this message.").queue(msg -> msg.addReaction("✅").queue());
                    }

                }
            }
        }
    }
}
