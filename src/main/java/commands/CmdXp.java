package commands;

import core.DatabaseHandler;
import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import util.*;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CmdXp {

    private static void xp(SlashCommandEvent event, Member xpmember) throws SQLException {

        String strmember;
        strmember = xpmember.getUser().getAsTag();

        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        String[] xp_level = DatabaseHandler.database(event.getGuild().getId(), "select xp, level from users where id = '" + xpmember.getId() + "'");

        assert xp_level != null;
        event.reply(MessageActions.getLocalizedString("xp_msg", "user", event.getUser().getId())
                .replace("[USER]", strmember).replace("[LEVEL]", numberFormat.format(Long.parseLong(xp_level[1])))
                .replace("[XP]", numberFormat.format(Long.parseLong(xp_level[0])))).queue();

    }

    public static void xp(SlashCommandEvent event) throws SQLException {

        try {
            switch (event.getSubcommandName()) {
                case "ranking":
                    event.deferReply(false).queue();
                    InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
                    hook.setEphemeral(false); // All messages here will now be ephemeral implicitly

                    int int_start = 0;
                    try {
                        int_start = (int) event.getOption("xp_rank").getAsLong();
                    } catch (Exception ignored) {
                    }
                    int start;
                    if (int_start != 0) {
                        start = int_start;
                    } else {
                        start = Integer.parseInt(Objects.requireNonNull(DatabaseHandler.database(event.getGuild().getId(), "select * from (select row_number() over (), id from (select id, xp, level, coins from users where ticket = 0 and not profile like 'bot' order by xp desc) as tmp) as temp where id = '" + event.getUser().getId() + "'"))[0]) - 5;
                    }
                    if (start <= 0)
                        start = 1;
                    int k;
                    String name;
                    String level;
                    String xp;
                    String coins;

                    String[] answer = DatabaseHandler.database(event.getGuild().getId(), "select id, xp, level, coins from users where ticket = 0 and not profile like 'bot' order by xp desc offset " + (start - 1) + " rows fetch next 10 rows only");

                    StringBuilder sb = new StringBuilder();

                    for (int j = 0; j < 10; j++) {
                        try {
                            name = Objects.requireNonNull(event.getJDA().getUserById(answer[j * 4])).getAsTag();
                        } catch (Exception e) {
                            name = Objects.requireNonNull(answer)[j * 4];
                        }
                        xp = answer[j*4+1];
                        level = answer[j*4+2];
                        coins = answer[j*4+3];

                        if (name.equals(event.getUser().getAsTag()) && j == 0) {
                            sb.append("```css\n");
                        } else if (!name.equals(event.getUser().getAsTag()) && j == 0) {
                            sb.append("```");
                        } else if (name.equals(event.getUser().getAsTag())) {
                            sb.append("\n``````css\n");
                        }

                        sb.append(start + j);
                        sb.append(". ");
                        sb.append(name);
                        k = name.length();
                        while (k < 35) {
                            sb.append(" ");
                            k++;
                        }
                        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                        sb.append(MessageActions.getLocalizedString("xp_ranking_level", "user", event.getUser().getId()));
                        sb.append(numberFormat.format(Long.parseLong(level)));
                        k = level.length();
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append(MessageActions.getLocalizedString("xp_ranking_xp", "user", event.getUser().getId()));
                        sb.append(numberFormat.format(Long.parseLong(xp)));
                        k = xp.length();
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append(MessageActions.getLocalizedString("xp_ranking_coins", "user", event.getUser().getId()));
                        sb.append(numberFormat.format(Long.parseLong(coins)));
                        k = coins.length();
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append("\n");
                        if (name.equals(event.getUser().getAsTag()) && !(j == 9)) {
                            sb.append("\n``````");
                        }

                    }
                    sb.append("```");

                    hook.editOriginal(sb.toString()).queue();
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
                            member = event.getOption("xp_give_user").getAsMember();

                            amount = event.getOption("xp_give_amount").getAsLong();

                            try {
                                DatabaseHandler.database(event.getGuild().getId(), "update users set xp = xp + " + amount + " where id = '" + member.getId() + "'");

                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(Color.RED);
                                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                                embed.setDescription("**" + event.getUser().getAsTag() + "** hat dem Nutzer **" + member.getUser().getAsTag() + "**" +
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
                        PermissionChecker.noPower(event);
                    }

                    break;
                case "next":
                    try {
                        event.deferReply(false).queue();
                        hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
                        hook.setEphemeral(false); // All messages here will now be ephemeral implicitly

                        String[] xp_level = DatabaseHandler.database(event.getGuild().getId(), "select xp, level from users where id = '" + event.getUser().getId() + "'");

                        assert xp_level != null;
                        long newxp = Long.parseLong(xp_level[0]);
                        long long_level = Long.parseLong(xp_level[1]);

                        Color color;
                        if (long_level > 1049) {
                            color = Color.decode("#ca2a1a");
                        } else if (long_level > 749)
                            color = Color.decode("#bf3636");
                        else if (long_level > 499)
                            color = Color.decode("#f25511");
                        else if (long_level > 299)
                            color = Color.decode("#f3730d");
                        else if (long_level > 149)
                            color = Color.decode("#e68f0a");
                        else if (long_level > 49)
                            color = Color.decode("#f7bf16");
                        else if (long_level > 9)
                            color = Color.decode("#fff53d");
                        else
                            color = Color.decode("#fff9ba");

                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(color);
                        embed.setTitle("Next level / next rank for " + event.getUser().getAsTag());
                        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                        embed.setDescription(
                                "Next level: " + numberFormat.format(LevelChecker.nextLevel(newxp)) + " XP remaining\n" +
                                        "Next rank: " + LevelChecker.nextRank(newxp)[1] + " (" + numberFormat.format(Long.parseLong(LevelChecker.nextRank(newxp)[0])) + " XP remaining)"
                        );
                        hook.editOriginalEmbeds(embed.build()).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "get":
                default:
                    Member member = event.getOption("xp_user").getAsMember();
                    xp(event, member);

                    break;
            }
        } catch (Exception e) {
            xp(event, Objects.requireNonNull(event.getMember()));
        }
    }
}

//TODO: Update message