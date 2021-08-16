package listeners;

import core.ChannelActions;
import core.DatabaseHandler;
import core.MessageActions;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.FALSE;


public class JoinListener extends ListenerAdapter {
    //TODO: comment
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
            SET_CHANNEL set_channel = CHANNEL.getSetChannel("log", event.getGuild().getId());
            if (set_channel.getMsg()) {
                MessageActions.neededChannel(event, "log");
            } else {
                TextChannel welcome1 = event.getGuild().getDefaultChannel();

                ChannelActions.getChannel(event, "log").sendMessage(MessageActions.getLocalizedString("log_user_join", "server", event.getGuild().getId())
                        .replace("[USER]", event.getUser().getAsTag())).queue();
                if (event.getMember().getUser().isBot() == FALSE) {
                    String[] exil = null;
                    try {
                        exil = DatabaseHandler.database(event.getGuild().getId(), "select id from exil");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    assert exil != null;
                    boolean isExil = false;
                    for (String str : exil) {
                        if (str.equals(event.getMember().getId()))
                            isExil = true;
                    }
                    Role exil_role = event.getGuild().getRolesByName("exil", true).get(0);
                    if (isExil)
                        event.getGuild().addRoleToMember(event.getMember(), exil_role).queue();
                    else {
                        String[] answer2 = null;
                        try {
                            answer2 = DatabaseHandler.database("usersettings", "select verified from users where id = '" + event.getMember().getId() + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            DatabaseHandler.database(event.getGuild().getId(), "update users set ticket = 0 where id = '" + event.getMember().getId() + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        boolean isVerifiedUser = false;
                        try {
                            assert answer2 != null;
                            if (answer2[0].equalsIgnoreCase("true")) {
                                isVerifiedUser = true;
                            }
                        } catch (Exception ignored) {
                        }

                        String[] answer3 = null;
                        try {
                            answer3 = DatabaseHandler.database(event.getGuild().getId(), "select verifystatus from users where id = '" + event.getMember().getId() + "'");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        boolean isVerifiedServer = false;
                        try {
                            assert answer3 != null;
                            if (answer3[0].equalsIgnoreCase("true")) {
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
                                    "Klicke daf\u00fcr auf das :white_check_mark:-Emoji unter dieser Nachricht.\n\n" +
                                    ":flag_gb: Mae govannen " + event.getMember().getAsMention() + "! In order to access the server, you have to verify yourself first. " +
                                    "Therefore, you have to click the :white_check_mark: emoji below this message.").queueAfter(3, TimeUnit.SECONDS, msg -> msg.addReaction("\u2705").queue());
                        }
                    }
                }

        }
    }
}
