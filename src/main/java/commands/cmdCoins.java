package commands;

import core.databaseHandler;
import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.javatuples.Triplet;
import util.CHANNEL;
import util.SET_CHANNEL;
import util.STATIC;
import util.getUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class cmdCoins implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {


            String argument;
            try {
                argument = args[0];
            } catch (Exception e) {
                argument = "-1-1-";
            }
            String[] coins_level = databaseHandler.database(event.getGuild().getId(), "select coins, level from users where id = '" + event.getAuthor().getId() + "'");
            assert coins_level != null;
            switch (argument) {
                case "gift":
                    if (Long.parseLong(coins_level[1]) >= 50) {
                        long amount;
                        Member member;
                        TextChannel channel = event.getChannel();
                        try {
                            ArrayList<String> list = new ArrayList<>(Arrays.asList(args).subList(1, args.length - 1));
                            member = util.getUser.getMemberFromInput(list.toArray(new String[0]), event.getAuthor(), event.getGuild(), channel);
                        } catch (Exception e) {
                            e.printStackTrace();
                            channel.sendMessage("Gib bitte einen Nutzer an.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                            break;
                        }

                        try {
                            amount = Long.parseLong(args[args.length - 1]);
                        } catch (Exception e) {
                            channel.sendMessage("Gib bitte die Anzahl an Coins an, die du hinzuf\u00fcgen willst.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                            break;
                        }

                        if (amount < 0) {
                            channel.sendMessage("Du kannst nicht weniger als 0 Coins verschenken!").queue();
                        } else {
                            try {
                                if (Long.parseLong(coins_level[0]) < amount) {
                                    channel.sendMessage("Du kannst nicht mehr Coins verschenken, als du besitzt!").queue();
                                } else {
                                    core.databaseHandler.database(event.getGuild().getId(), "update users set coins = coins - " + amount + " where id = '" + event.getAuthor().getId() + "'");

                                    assert member != null;
                                    core.databaseHandler.database(event.getGuild().getId(), "update users set coins = coins + " + amount + " where id = '" + member.getId() + "'");

                                    event.getChannel().sendMessage("**" + event.getAuthor().getAsTag() + "** hat an **" + member.getUser().getAsTag() + "** `" + amount + "` Coins verschenkt.").queue();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        event.getChannel().sendMessage("Du musst mindestens Level 50 erreicht haben!").queue();
                    }
                    break;
                case "give":
                    if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
                        SET_CHANNEL set_channel = CHANNEL.getSetChannel("modlog", event.getGuild().getId());
                        if (set_channel.getMsg()) {
                            messageActions.neededChannel(event);
                        } else {
                            TextChannel modlog = event.getGuild().getTextChannelById(set_channel.getChannel());
                            long amount;
                            Member member;
                            TextChannel channel = event.getChannel();
                            try {
                                ArrayList<String> list = new ArrayList<>(Arrays.asList(args).subList(1, args.length - 1));
                                member = util.getUser.getMemberFromInput(list.toArray(new String[0]), event.getAuthor(), event.getGuild(), channel);
                            } catch (Exception e) {
                                channel.sendMessage("Gib bitte einen Nutzer an.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                break;
                            }

                            try {
                                amount = Long.parseLong(args[args.length - 1]);
                            } catch (Exception e) {
                                channel.sendMessage("Gib bitte die Anzahl an Coins an, die du hinzuf\u00fcgen willst.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                break;
                            }

                            try {
                                assert member != null;

                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(Color.RED);
                                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                                embed.setDescription("**" + event.getAuthor().getAsTag() + "** hat dem Nutzer **" + member.getUser().getAsTag() + "**" +
                                        " `" + numberFormat.format(amount) + "` Coins hinzugef\u00fcgt.");
                                embed.setTimestamp(Instant.now());
                                assert modlog != null;
                                modlog.sendMessage(embed.build()).queue();

                                core.databaseHandler.database(event.getGuild().getId(), "update users set coins = coins + " + amount + " where id = '" + member.getId() + "'");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        permissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
                    }
                    break;
                default:
                    try {
                        ArrayList<String> args2 = new ArrayList<>();
                        int i = 0;
                        while (i < args.length - 2) {
                            args2.add(args[i]);
                            i++;
                        }
                        args2.add(args[i]);
                        String[] args3 = new String[args2.size()];
                        args3 = args2.toArray(args3);
                        Member member = getUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getChannel());
                        assert member != null;
                        coins(event, member);
                    } catch (Exception e) {
                        coins(event, Objects.requireNonNull(event.getMember()));
                    }

                    break;
            }

    }


    private void coins(GuildMessageReceivedEvent event, Member member) {

        long coins;
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        try {
            coins = Long.parseLong(Objects.requireNonNull(databaseHandler.database(event.getGuild().getId(), "select coins from users where id = '" + member.getId() + "'"))[0]);
        } catch (Exception e) {
            coins = 0L;
        }
        event.getChannel().sendMessage(messageActions.getLocalizedString("coins_msg", "user", event.getAuthor().getId())
                .replace("[USER]", member.getUser().getAsTag()).replace("[COINS]", numberFormat.format(coins))).queue();

    }
}


