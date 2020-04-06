package commands;

import core.messageActions;
import core.modulesChecker;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.CHANNEL;
import util.SET_CHANNEL;
import util.getUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class cmdCoins implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws SQLException {
        String status;

        // checking for activation
        status = modulesChecker.moduleStatus("xp", event.getGuild().getId());
        if (status.equals("activated")) {

            String argument;
            try {
                argument = args[0];
            } catch (Exception e) {
                argument = "-1-1-";
            }

            switch (argument) {
                case "gift":
                    String[] argumentsStart = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "level"};
                    String[] answerStart;
                    answerStart = core.databaseHandler.database(event.getGuild().getId(), "select", argumentsStart);
                    assert answerStart != null;
                    if (Integer.parseInt(answerStart[0]) >= 50) {
                        long amount;
                        Member member = null;
                        MessageChannel channel = event.getChannel();
                        if (args[1].contains("<") && args[1].contains(">") && args[1].contains("@")) {
                            try {
                                member = event.getGuild().getMemberById(args[1].replace("@", "").replace("<", "").replace(">", "").replace("!", ""));
                            } catch (Exception e) {
                                Message msg = channel.sendMessage("Gib bitte einen Nutzer an.").complete();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        msg.delete().queue();
                                    }
                                }, 5000);
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
                                Message msg = channel.sendMessage("Nutzer nicht gefunden.").complete();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        msg.delete().queue();
                                    }
                                }, 5000);
                            }
                        }

                        try {
                            amount = Long.parseLong(args[args.length - 1]);
                        } catch (Exception e) {
                            Message msg = channel.sendMessage("Gib bitte die Anzahl an Coins an, die du hinzuf\u00fcgen willst.").complete();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    msg.delete().queue();
                                }
                            }, 5000);
                            break;
                        }

                        if (amount < 0) {
                            channel.sendMessage("Du kannst nicht weniger als 0 Coins verschenken!").complete();
                        } else {
                            try {
                                String[] argumentsSelectAuthor = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "coins"};
                                String[] answerSelectAuthor;
                                answerSelectAuthor = core.databaseHandler.database(event.getGuild().getId(), "select", argumentsSelectAuthor);

                                assert answerSelectAuthor != null;
                                if (Long.parseLong(answerSelectAuthor[0]) < amount) {
                                    channel.sendMessage("Du kannst nicht mehr Coins verschenken, als du besitzt!").complete();
                                } else {
                                    long newcoinsAuthor = Long.parseLong(answerSelectAuthor[0]) - amount;

                                    String[] argumentsUpdateAuthor = {"users", "id = '" + event.getAuthor().getId() + "'", "coins", String.valueOf(newcoinsAuthor)};
                                    core.databaseHandler.database(event.getGuild().getId(), "update", argumentsUpdateAuthor);


                                    assert member != null;
                                    String[] argumentsSelectMember = {"users", "id = '" + member.getUser().getId() + "'", "1", "coins"};
                                    String[] answerSelectMember;
                                    answerSelectMember = core.databaseHandler.database(event.getGuild().getId(), "select", argumentsSelectMember);

                                    assert answerSelectMember != null;
                                    long newcoinsMember = Long.parseLong(answerSelectMember[0]) + amount;

                                    String[] argumentsUpdateMember = {"users", "id = '" + member.getUser().getId() + "'", "coins", String.valueOf(newcoinsMember)};
                                    core.databaseHandler.database(event.getGuild().getId(), "update", argumentsUpdateMember);
                                    event.getChannel().sendMessage(event.getAuthor().getAsTag() + " hat an " + member.getUser().getAsTag() + " " + amount + " Coins verschenkt.").complete();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        event.getChannel().sendMessage("Du musst mindestens Level 50 erreicht haben!").complete();
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
                                    Message msg = channel.sendMessage("Gib bitte einen Nutzer an.").complete();
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            msg.delete().queue();
                                        }
                                    }, 5000);
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
                                    Message msg = channel.sendMessage("Nutzer nicht gefunden.").complete();
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            msg.delete().queue();
                                        }
                                    }, 5000);
                                }
                            }

                            try {
                                amount = Long.parseLong(args[args.length - 1]);
                            } catch (Exception e) {
                                Message msg = channel.sendMessage("Gib bitte die Anzahl an Coins an, die du hinzuf\u00fcgen willst.").complete();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        msg.delete().queue();
                                    }
                                }, 5000);
                                break;
                            }

                            try {
                                assert member != null;
                                String[] arguments = {"users", "id = '" + member.getUser().getId() + "'", "1", "coins"};
                                String[] answer;
                                answer = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);

                                assert answer != null;
                                long newcoins = Long.parseLong(answer[0]) + amount;

                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(Color.RED);
                                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                                embed.setDescription("**" + event.getAuthor().getAsTag() + "** hat dem Nutzer **" + member.getUser().getAsTag() + "**" +
                                        " *" + numberFormat.format(amount) + "* Coins hinzugef\u00fcgt.");
                                embed.setTimestamp(Instant.now());
                                assert modlog != null;
                                modlog.sendMessage(embed.build()).queue();

                                String[] arguments3 = {"users", "id = '" + member.getUser().getId() + "'", "coins", String.valueOf(newcoins)};
                                core.databaseHandler.database(event.getGuild().getId(), "update", arguments3);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        permissionChecker.noPower(event.getTextChannel());
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
                        Member member = getUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getTextChannel());
                        coins(event, member);
                    } catch (Exception e) {
                        coins(event, Objects.requireNonNull(event.getMember()));
                    }

                    break;
            }
        } else {
            messageActions.moduleIsDeactivated(event, "xp");
        }
    }


    private void coins(MessageReceivedEvent event, Member member) throws SQLException {

        String coins;
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        // sending msg with number of coins
        String[] arguments = {"users", "id = '" + member.getUser().getId() + "'", "1", "coins"};
        String[] answer;
        answer = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
        try {
            assert answer != null;
            coins = answer[0];
        } catch (Exception e) {
            coins = "0";
        }
        event.getTextChannel().sendMessage(messageActions.getLocalizedString("coins_msg", "user", event.getAuthor().getId())
                .replace("[USER]", member.getUser().getAsTag()).replace("[COINS]", numberFormat.format(Integer.parseInt(coins)))).queue();

    }
}


