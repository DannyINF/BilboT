package special;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class announcements extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getChannel().getName().toLowerCase().contains("chat") && !event.getChannel().getName().toLowerCase().contains("diskussion")) {
            int i = STATIC.getAnnouncement();
            if (i >= 250) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTimestamp(Instant.now());
                embed.setColor(Color.decode("#4809bd"));
                int random = ThreadLocalRandom.current().nextInt(0, 7);
                switch (random) {
                    case 0:
                        embed.setTitle("\uD83D\uDCDA Der Lyrikabend:");
                        embed.setDescription("Jede Woche findet auf dem Server der Lyrikabend statt. Dabei werden ein oder mehrere Kapitel vorgelesen und danach diskutiert." +
                                "Um teilnehmen zu k\u00f6nnen, muss man seine Buchausgabe angeben. Mehr Infos in " + Objects.requireNonNull(event.getGuild().getTextChannelById("470283820881543188")).getAsMention());
                        break;
                    case 1:
                        embed.setTitle("\uD83C\uDFA8 Der Zeichenwettbewerb");
                        embed.setDescription("Einmal im Monat findet der Zeichenwettbewerb statt. Aus drei Themen wird eines gew\u00e4hlt, welches dann gezeichnet wird. Dabei soll kreativ gearbeitet werden und es gibt auch was zu gewinnen. " +
                                "Alle Informationen in " + Objects.requireNonNull(event.getGuild().getTextChannelById("557328633606307892")).getAsMention());
                        break;
                    case 2:
                        embed.setTitle("\uD83D\uDCDD Der Schreibwettbewerb");
                        embed.setDescription("Alle zwei Monate findet der Schreibwettbewerb statt. Aus drei Themen wird eines gew\u00e4hlt, zu welchem dann ein Text geschrieben wird. Dabei soll kreativ gearbeitet werden und es gibt auch was zu gewinnen. " +
                                "Alle Informationen in " + Objects.requireNonNull(event.getGuild().getTextChannelById("596263709765271552")).getAsMention());
                        break;
                    case 3:
                        embed.setTitle("\u2753 Das Tolkien-Quick-Quiz");
                        embed.setDescription("Jede Woche am Dienstag gibt es 10 Fragen zu Tolkiens Werken. Jeder darf teilnehmen (Au\u00dfer Experten und das Adminteam)\n" +
                                "In " + Objects.requireNonNull(event.getGuild().getTextChannelById("631157118942052362")).getAsMention() + " findet es statt.");
                        break;
                    case 4:
                        embed.setTitle("\uD83D\uDCC5 W\u00f6chentliches Thema");
                        embed.setDescription("(Fast) jede Woche kommt hier ein neues Thema, \u00fcber das diskutiert werden kann. Dabei muss man nicht die tiefste Einsicht in das Thema haben und kann auch einfach Fragen zu dem Thema stellen. " +
                                Objects.requireNonNull(event.getGuild().getTextChannelById("473261177397444620")).getAsMention());
                        break;
                    case 5:
                        embed.setTitle("\u2754 Fragen zu Tolkiens Werken");
                        embed.setDescription("Du willst wissen, wer Tom Bombadil war oder was die Schattenarmee gemacht hat?\n" +
                                "Einfach in " + Objects.requireNonNull(event.getGuild().getTextChannelById("453541314706014228")).getAsMention() + " jede erdenkliche Frage zu Tolkien und/oder seinen Werken stellen!");
                        break;
                    case 6:
                        embed.setTitle("\u23ef\ufe0f YouTuber-Kategorien");
                        embed.setDescription("F\u00fcr alle hier vertretenen YouTuber gibt es einen Subbereich, wo ihr mit ihnen schreiben k\u00f6nnt und das Neuste erf\u00e4hrt.\n" +
                                "Um beizutreten m\u00fcsst ihr nur einen einfachen Befehl eingeben:\n" +
                                "Ardko: `/role add sub2Ardko`\n" +
                                "Mythen aus Mittelerde: `/role add sub2MaM`\n" +
                                "Tolkien erkl\u00e4rt: `/role add sub2TE`");
                        break;

                }
                if (!event.getChannel().getName().toLowerCase().contains("\uD83D\uDCAC-chat") && !event.getChannel().getName().toLowerCase().contains("diskussion")) {
                    STATIC.changeAnnouncement(1);
                } else {
                    STATIC.changeAnnouncement(-i);
                    event.getChannel().sendMessage(embed.build()).queue();
                }

            } else {
                STATIC.changeAnnouncement(1);
            }
        }

    }
}
