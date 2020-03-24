package special;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.STATIC;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class announcements extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getTextChannel().getName().toLowerCase().contains("chat") && !event.getTextChannel().getName().toLowerCase().contains("diskussion")) {
            int i = STATIC.getAnnouncement();
            if (i >= 250) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTimestamp(Instant.now());
                embed.setColor(Color.decode("#4809bd"));
                int random = ThreadLocalRandom.current().nextInt(0, 7);
                switch (random) {
                    case 0:
                        embed.setTitle("#TolkienTrotztCorona");
                        embed.setDescription("Wir veranstalten zusammen mit der Deutschen Tolkien Gesellschaft die Tolkien Reading Days! Informationen dazu in " + event.getGuild().getTextChannelById("407960159227215872").getAsMention() + ".");
                        break;
                    case 1:
                        embed.setTitle("#TolkienTrotztCorona");
                        embed.setDescription("Am Mittwoch den 25.03. und am Sonntag den 29.03. ab jeweils 18 Uhr, wird im Lyrikecke-Voicekanal aus Werken von Tolkien gelesen.");
                        break;
                    case 2:
                        embed.setTitle("#TolkienTrotztCorona");
                        embed.setDescription("Lesen vereint! Und das nicht nur w\u00e4hrend des Tolkien Reading Day, sondern auch zum Beispiel im Lyrikabend, der hier w\u00f6chentlich stattfindet. \n" +
                                "Informationen hierzu: " + event.getGuild().getTextChannelById("470283820881543188").getAsMention());
                        break;
                    case 3:
                        embed.setTitle("#TolkienTrotztCorona");
                        embed.setDescription("Zwischen den Hauptevents des Tolkien Reading Days kann jeder Zuh\u00f6rer selbst Gedichte oder Briefe von J.R.R. Tolkien vorschlagen oder sogar vorlesen!");
                        break;
                    case 4:
                        embed.setTitle("#TolkienTrotztCorona");
                        embed.setDescription("Ein R\u00e4tsel in der Finsternis: Bilbo trifft das Gesch\u00f6pf Gollum und ein R\u00e4tselspiel um Leben und Tod entsteht.\n" +
                                "Mittwoch 25.03. um 18 Uhr im Lyrikecke-Voicekanal.");
                        break;
                    case 5:
                        embed.setTitle("#TolkienTrotztCorona");
                        embed.setDescription("T\u00farin Turambar der tragische Held: Mehr \u00fcber ihn am Mittwoch den 25.03. ab 18 Uhr.");
                        break;
                    case 6:
                        embed.setTitle("#TolkienTrotztCorona");
                        embed.setDescription("Die Abenteuer des Tom Bombadil noch nie selbst gelesen? Vorlesen lassen ist besser! \n" +
                                "Sonntag 29.03. ab 18 Uhr.");
                        break;

                }
                if (!event.getTextChannel().getName().toLowerCase().contains("\uD83D\uDCAC-chat") && !event.getTextChannel().getName().toLowerCase().contains("diskussion")) {
                    STATIC.changeAnnouncement(1);
                } else {
                    STATIC.changeAnnouncement(-i);
                    event.getTextChannel().sendMessage(embed.build()).queue();
                }

            } else {
                STATIC.changeAnnouncement(1);
            }
        }

    }
}
