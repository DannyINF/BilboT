package commands;

import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
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
            Triplet answerStart = STATIC.getExperienceUser(event.getAuthor().getId(), event.getGuild().getId());
            switch (argument) {
                case "gift":
                    if ((Long) answerStart.getValue1() >= 50) {
                        long amount;
                        Member member = null;
                        MessageChannel channel = event.getChannel();
                        if (args[1].contains("<") && args[1].contains(">") && args[1].contains("@")) {
                            try {
                                member = event.getGuild().getMemberById(args[1].replace("@", "").replace("<", "").replace(">", "").replace("!", ""));
                            } catch (Exception e) {
                                channel.sendMessage("Gib bitte einen Nutzer an.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                break;
                            }
                        } else {
                            try {
                                int i = 1;
                                StringBuilder sb = new StringBuilder();
                                while (i < args.length - 2) {
                                    sb.append(args[i]);
                                    sb.append(" ");
                                    i++;
                                }
                                sb.append(args[i]);
                                member = event.getGuild().getMembersByEffectiveName(sb.toString(), true).get(0);
                            } catch (Exception e) {
                                channel.sendMessage("Nutzer nicht gefunden.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                            }
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
                                if ((Long)answerStart.getValue2() < amount) {
                                    channel.sendMessage("Du kannst nicht mehr Coins verschenken, als du besitzt!").queue();
                                } else {
                                    long newcoinsAuthor = (Long) answerStart.getValue2() - amount;

                                    STATIC.updateExperienceUser(event.getAuthor().getId(), event.getGuild().getId(), 0L, 0L, -amount);

                                    assert member != null;
                                    Triplet answerSelectMember = STATIC.getExperienceUser(member.getId(), event.getGuild().getId());

                                    long newcoinsMember = (Long) answerSelectMember.getValue2() + amount;

                                    STATIC.updateExperienceUser(member.getId(), event.getGuild().getId(), 0L, 0L, amount);

                                    event.getChannel().sendMessage("**" + event.getAuthor().getAsTag() + "** hat an **" + member.getUser().getAsTag() + "** `" + amount + "` Coins verschenkt.").queue();

                                    String[] argumentsUpdateAuthor = {"users", "id = '" + event.getAuthor().getId() + "'", "coins", String.valueOf(newcoinsAuthor)};
                                    core.databaseHandler.database(event.getGuild().getId(), "update users set coins = " + newcoinsAuthor + " where id = '" + event.getAuthor().getId() + "'");

                                    String[] argumentsUpdateMember = {"users", "id = '" + member.getUser().getId() + "'", "coins", String.valueOf(newcoinsMember)};
                                    core.databaseHandler.database(event.getGuild().getId(), "update users set coins = " + newcoinsMember + " where id = '" + member.getId() + "'");
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
                            Member member = null;
                            MessageChannel channel = event.getChannel();
                            if (args[1].contains("<") && args[1].contains(">") && args[1].contains("@")) {
                                try {
                                    member = event.getGuild().getMemberById(args[1].replace("@", "").replace("<", "").replace(">", ""));
                                } catch (Exception e) {
                                    channel.sendMessage("Gib bitte einen Nutzer an.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                    break;
                                }
                            } else {
                                try {
                                    int i = 1;
                                    StringBuilder sb = new StringBuilder();
                                    while (i < args.length - 2) {
                                        sb.append(args[i]);
                                        sb.append(" ");
                                        i++;
                                    }
                                    sb.append(args[i]);
                                    member = event.getGuild().getMembersByEffectiveName(sb.toString(), true).get(0);
                                } catch (Exception e) {
                                    channel.sendMessage("Nutzer nicht gefunden.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                }
                            }

                            try {
                                amount = Long.parseLong(args[args.length - 1]);
                            } catch (Exception e) {
                                channel.sendMessage("Gib bitte die Anzahl an Coins an, die du hinzuf\u00fcgen willst.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                break;
                            }

                            try {
                                assert member != null;
                                Triplet answer = STATIC.getExperienceUser(member.getId(), event.getGuild().getId());

                                long newcoins = (Long) answer.getValue2() + amount;

                                STATIC.updateExperienceUser(member.getId(), event.getGuild().getId(), 0L, 0L, amount);

                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(Color.RED);
                                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                                embed.setDescription("**" + event.getAuthor().getAsTag() + "** hat dem Nutzer **" + member.getUser().getAsTag() + "**" +
                                        " `" + numberFormat.format(amount) + "` Coins hinzugef\u00fcgt.");
                                embed.setTimestamp(Instant.now());
                                assert modlog != null;
                                modlog.sendMessage(embed.build()).queue();

                                String[] arguments3 = {"users", "id = '" + member.getUser().getId() + "'", "coins", String.valueOf(newcoins)};
                                core.databaseHandler.database(event.getGuild().getId(), "update users set coins = " + newcoins + " where id = '" + member.getId() + "'");
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
                        coins(event, member);
                    } catch (Exception e) {
                        coins(event, Objects.requireNonNull(event.getMember()));
                    }

                    break;
            }

    }


    private void coins(GuildMessageReceivedEvent event, Member member) throws SQLException {

        String coins;
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        // sending msg with number of coins
        Triplet answer = STATIC.getExperienceUser(member.getId(), event.getGuild().getId());
        try {
            coins = String.valueOf(answer.getValue2());
        } catch (Exception e) {
            coins = "0";
        }
        event.getChannel().sendMessage(messageActions.getLocalizedString("coins_msg", "user", event.getAuthor().getId())
                .replace("[USER]", member.getUser().getAsTag()).replace("[COINS]", numberFormat.format(Integer.parseInt(coins)))).queue();

    }
}


