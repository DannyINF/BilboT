package commands;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.derby.impl.sql.execute.CurrentDatetime;
import util.GetUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.ArrayList;

public class CmdStats {

    public static void stats(SlashCommandEvent event) {

        Member member = event.getMember();
        try {
            member = event.getOption("stats_user").getAsMember();
        } catch (Exception ignored) { }

        String[] answer1 = null;
        try {
            answer1 = DatabaseHandler.database(event.getGuild().getId(), "select words, msg, chars, voicetime, xp, level, coins, first_join from users where id = '" + member.getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long words;
        long msg;
        long chars;
        long voicetime;
        long xp;
        long level;
        long coins;
        String date;

        try {
            assert answer1 != null;
            words = Long.parseLong(answer1[0]);
        } catch (Exception e) {
            words = 0;
        }
        try {
            msg = Long.parseLong(answer1[1]);
        } catch (Exception e) {
            msg = 0;
        }
        try {
            chars = Long.parseLong(answer1[2]);
        } catch (Exception e) {
            chars = 0;
        }
        try {
            voicetime = Long.parseLong(answer1[3]);
        } catch (Exception e) {
            voicetime = 0;
        }
        try {
            xp = Long.parseLong(answer1[4]);
        } catch (Exception e) {
            xp = 0;
        }
        try {
            level = Long.parseLong(answer1[5]);
        } catch (Exception e) {
            level = 0;
        }
        try {
            coins = Long.parseLong(answer1[6]);
        } catch (Exception e) {
            coins = 0;
        }

        try {
            date = answer1[7].split("-")[2] + "." + answer1[7].split("-")[1] + "." + answer1[7].split("-")[0]; //reformat date
        } catch (Exception e) {
            date = "10.06.2019"; //date of launch of feature
        }

        long hours = voicetime / 60;
        long minutes = voicetime % 60;
        long days = hours / 24;
        hours = hours % 24;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(191, 255, 178));
        embed.setTitle("Statistiken f\u00fcr " + member.getUser().getAsTag());
        embed.setFooter("seit dem " + date, null);
        embed.setTimestamp(new CurrentDatetime().getCurrentTimestamp().toLocalDateTime().atZone(ZoneId.of("Europe/Berlin")));
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
        embed.setDescription(
                        "XP: " + numberFormat.format(xp) +
                        "\nLevel: " + numberFormat.format(level) +
                        "\nCoins: " + numberFormat.format(coins) +
                        "\n\nWords: " + numberFormat.format(words) +
                        "\nMessages: " + numberFormat.format(msg) +
                        "\nCharacters: " + numberFormat.format(chars) +
                        "\nVoicetime: " + days + " days, " + hours + " hours, " + minutes + " minutes");
        event.replyEmbeds(embed.build()).queue();
    }
}
