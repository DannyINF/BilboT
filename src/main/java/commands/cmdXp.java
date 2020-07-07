package commands;

import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.*;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class cmdXp implements Command {

    private static void xp(GuildMessageReceivedEvent event, Member xpmember) throws SQLException {

        String strmember;
        strmember = xpmember.getUser().getAsTag();

        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        String[] xp_level = core.databaseHandler.database(event.getGuild().getId(), "select xp, level from users where id = '" + xpmember.getId() + "'");

        assert xp_level != null;
        event.getChannel().sendMessage(messageActions.getLocalizedString("xp_msg", "user", event.getAuthor().getId())
                .replace("[USER]", strmember).replace("[LEVEL]", numberFormat.format(Long.parseLong(xp_level[1])))
                .replace("[XP]", numberFormat.format(Long.parseLong(xp_level[0])))).queue();

    }

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        try {
            switch (args[0]) {
                case "ranking":
                    cmdXpRanking.action(args, event);
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
                            MessageChannel channel = event.getChannel();
                            try {
                                ArrayList<String> list = new ArrayList<>(Arrays.asList(args).subList(1, args.length - 1));
                                member = util.getUser.getMemberFromInput(list.toArray(new String[0]), event.getAuthor(), event.getGuild(), event.getChannel());
                            } catch (Exception e) {
                                channel.sendMessage("Gib bitte einen Nutzer an.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                break;
                            }
                            assert member != null;
                            try {
                                amount = Long.parseLong(args[args.length - 1]);
                            } catch (Exception e) {
                                channel.sendMessage("Gib bitte die Anzahl an XP an, die du hinzuf\u00fcgen willst.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                                break;
                            }

                            try {
                                core.databaseHandler.database(event.getGuild().getId(), "update users set xp = xp + " + amount + " where id = '" + member.getId() + "'");

                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(Color.RED);
                                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                                embed.setDescription("**" + event.getAuthor().getAsTag() + "** hat dem Nutzer **" + member.getUser().getAsTag() + "**" +
                                        " `" + numberFormat.format(amount) + "` XP hinzugef\u00fcgt.");
                                embed.setTimestamp(Instant.now());
                                assert modlog != null;
                                modlog.sendMessage(embed.build()).queue();

                                LevelChecker.checker(member, event.getGuild());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        permissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
                    }

                    break;
                case "next":
                    try {
                        String[] xp_level = core.databaseHandler.database(event.getGuild().getId(), "select xp, level from users where id = '" + event.getAuthor().getId() + "'");

                        assert xp_level != null;
                        long newxp = Long.parseLong(xp_level[0]);
                        long level = Long.parseLong(xp_level[1]);

                        Color color;
                        if (level > 1049) {
                            color = Color.decode("#ca2a1a");
                        } else if (level > 749)
                            color = Color.decode("#bf3636");
                        else if (level > 499)
                            color = Color.decode("#f25511");
                        else if (level > 299)
                            color = Color.decode("#f3730d");
                        else if (level > 149)
                            color = Color.decode("#e68f0a");
                        else if (level > 49)
                            color = Color.decode("#f7bf16");
                        else if (level > 9)
                            color = Color.decode("#fff53d");
                        else
                            color = Color.decode("#fff9ba");

                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(color);
                        embed.setTitle("Next level / next rank for " + event.getAuthor().getAsTag());
                        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                        embed.setDescription(
                                "Next level: " + numberFormat.format(LevelChecker.nextLevel(newxp)) + " XP remaining\n" +
                                        "Next rank: " + LevelChecker.nextRank(newxp)[1] + " (" + numberFormat.format(Long.parseLong(LevelChecker.nextRank(newxp)[0])) + " XP remaining)"
                        );
                        event.getChannel().sendMessage(embed.build()).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    Member member = getUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getChannel());
                    assert member != null;
                    xp(event, member);

                    break;
            }
        } catch (Exception e) {
            xp(event, Objects.requireNonNull(event.getMember()));
        }
    }
}

//TODO: Update message