package commands;

import core.DatabaseHandler;
import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.CHANNEL;
import util.SET_CHANNEL;
import util.GetUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class CmdCoins {

    public static void coins(SlashCommandEvent event) throws SQLException {

        try {
            String[] coins_level = DatabaseHandler.database(event.getGuild().getId(), "select coins, level from users where id = '" + event.getUser().getId() + "'");
            assert coins_level != null;
            switch (event.getSubcommandName()) {
                case "gift":
                    if (Long.parseLong(coins_level[1]) >= 50) {
                        long amount;
                        Member member;
                        TextChannel channel = event.getTextChannel();
                        member = event.getOption("coins_gift_user").getAsMember();

                        amount = event.getOption("coins_gift_amount").getAsLong();

                        if (amount < 0) {
                            channel.sendMessage("Du kannst nicht weniger als 0 Coins verschenken!").queue();
                        } else {
                            try {
                                if (Long.parseLong(coins_level[0]) < amount) {
                                    channel.sendMessage("Du kannst nicht mehr Coins verschenken, als du besitzt!").queue();
                                } else {
                                    DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins - " + amount + " where id = '" + event.getUser().getId() + "'");

                                    assert member != null;
                                    DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins + " + amount + " where id = '" + member.getId() + "'");

                                    event.reply("**" + event.getUser().getAsTag() + "** hat an **" + member.getUser().getAsTag() + "** `" + amount + "` Coins verschenkt.").queue();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        event.getTextChannel().sendMessage("Du musst mindestens Level 50 erreicht haben!").queue();
                    }
                    break;
                case "give":
                    if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
                        SET_CHANNEL set_channel = CHANNEL.getSetChannel("modlog", event.getGuild().getId());
                        if (set_channel.getMsg()) {
                            MessageActions.neededChannel(event);
                        } else {
                            TextChannel modlog = event.getGuild().getTextChannelById(set_channel.getChannel());
                            long amount;
                            Member member;
                            TextChannel channel = event.getTextChannel();
                            member = event.getOption("coins_give_user").getAsMember();

                            amount = event.getOption("coins_give_amount").getAsLong();

                            try {
                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(Color.RED);
                                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                                embed.setDescription("**" + event.getUser().getAsTag() + "** hat dem Nutzer **" + member.getUser().getAsTag() + "**" +
                                        " `" + numberFormat.format(amount) + "` Coins hinzugef\u00fcgt.");
                                embed.setTimestamp(Instant.now());
                                assert modlog != null;
                                modlog.sendMessage(embed.build()).queue();

                                DatabaseHandler.database(event.getGuild().getId(), "update users set coins = coins + " + amount + " where id = '" + member.getId() + "'");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            event.reply("Hat dem Nutzer **" + member.getUser().getAsTag() + "** `" + amount + "` hinzugef\u00FCgt.").queue();
                        }
                    } else {
                        PermissionChecker.noPower(event);
                    }
                    break;
                case "get":
                default:
                    try {
                        coins(event, event.getOption("coins_user").getAsMember());
                    } catch (Exception e) {
                        coins(event, event.getMember());
                    }

                    break;
            }
        } catch (Exception e) {
            coins(event, event.getMember());
        }

    }


    private static void coins(SlashCommandEvent event, Member member) {

        long coins;
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        try {
            coins = Long.parseLong(Objects.requireNonNull(DatabaseHandler.database(event.getGuild().getId(), "select coins from users where id = '" + member.getId() + "'"))[0]);
        } catch (Exception e) {
            coins = 0L;
        }
        event.reply(MessageActions.getLocalizedString("coins_msg", "user", event.getUser().getId())
                .replace("[USER]", member.getUser().getAsTag()).replace("[COINS]", numberFormat.format(coins))).queue();

    }
}


