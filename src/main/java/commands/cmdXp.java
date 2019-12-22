package commands;

import core.messageActions;
import core.modulesChecker;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.LevelChecker;
import util.getUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class cmdXp implements Command {

    private static void xp(MessageReceivedEvent event, Member xpmember) throws SQLException {

        String strmember;
        strmember = xpmember.getUser().getAsTag();

        String xp;
        String level;
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        String[] arguments = {"users", "id = '" + xpmember.getUser().getId() + "'", "2", "xp", "level"};
        String[] data = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
        xp = data[0];
        level = data[1];
        event.getTextChannel().sendMessage(messageActions.getLocalizedString("xp_msg", "user", event.getAuthor().getId())
                .replace("[USER]", strmember).replace("[LEVEL]", numberFormat.format(Integer.parseInt(level)))
                .replace("[XP]", numberFormat.format(Integer.parseInt(xp)))).queue();

    }

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws SQLException {
        String status;
        status = modulesChecker.moduleStatus("xp", event.getGuild().getId());
        if (status.equals("activated")) {

            try {
                switch (args[0]) {
                    case "ranking":
                        cmdXpRanking.action(args, event);
                        break;
                    case "give":
                        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
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
                                Message msg = channel.sendMessage("Gib bitte die Anzahl an XP an, die du hinzuf\u00fcgen willst.").complete();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        msg.delete().queue();
                                    }
                                }, 5000);
                                break;
                            }

                            try {
                                String[] arguments = {"users", "id = '" + member.getUser().getId() + "'", "1", "xp"};
                                String[] answer;
                                answer = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);

                                long newxp = Long.parseLong(answer[0]) + amount;

                                String[] arguments3 = {"users", "id = '" + member.getUser().getId() + "'", "xp", String.valueOf(newxp)};
                                core.databaseHandler.database(event.getGuild().getId(), "update", arguments3);
                                long level = LevelChecker.checker(member, event.getGuild(), newxp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case "next":
                        String[] arguments = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "xp"};
                        String[] answer;
                        answer = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);

                        long newxp = Long.parseLong(answer[0]);
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.ORANGE);
                        embed.setTitle("Next level / next rank for " + event.getAuthor().getAsTag());
                        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                        embed.setDescription(
                                "Next level: " + numberFormat.format(LevelChecker.nextLevel(newxp)) + " XP remaining\n" +
                                        "Next rank: " + LevelChecker.nextRank(newxp)[1] + " (" + numberFormat.format(Long.parseLong(LevelChecker.nextRank(newxp)[0])) + " XP remaining)"
                        );
                        event.getTextChannel().sendMessage(embed.build()).queue();
                        break;
                    default:
                        ArrayList<String> args2 = new ArrayList<>();
                        int i = 0;
                        while (i < args.length - 1) {
                            args2.add(args[i]);
                            args2.add(" ");
                            i++;
                        }
                        args2.add(args[i]);
                        String[] args3 = new String[args2.size()];
                        args3 = args2.toArray(args3);
                        Member member = getUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getTextChannel());
                        xp(event, member);

                        break;
                }
            } catch (Exception e) {
                xp(event, event.getMember());
            }


        } else {
            messageActions.moduleIsDeactivated(event, "xp");
        }
    }
}

//TODO: Update message