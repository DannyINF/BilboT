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
        int i = STATIC.getAnnouncement();
        if (i >= 250) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTimestamp(Instant.now());
            embed.setColor(Color.decode("#4809bd"));
            int random = ThreadLocalRandom.current().nextInt(0, 8);
            switch (random) {
                case 0:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Wir veranstalten zusammen mit der Deutschen Tolkien Gesellschaft die Tolkien Reading Days! Informationen dazu in " + event.getGuild().getTextChannelById("407960159227215872").getAsMention() + ".");
                    break;
                case 1:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Am Samstag den 21.03. ab 19 Uhr, am Mittwoch den 25.03 und am Sonntag den 29.03. ab jeweils 18 Uhr, wird im Lyrikecke-Voicekanal aus Werken von Tolkien gelesen.");
                    break;
                case 2:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Lesen vereint! Und das nicht nur während des Tolkien Reading Day, sondern auch zum Beispiel im Lyrikabend, der hier wöchentlich stattfindet. \n" +
                            "Informationen hierzu: " + event.getGuild().getTextChannelById("470283820881543188").getAsMention());
                    break;
                case 3:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Wer waren nochmal Beren und Lúthien? Die Geschichte hierzu am 21.03. ab 19 Uhr.");
                    break;
                case 4:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Ein Rätsel in der Finsternis: Bilbo trifft das Geschöpf Gollum und ein Rätselspiel um Leben und Tod entsteht.\n" +
                            "Mittwoch 25.03. um 18 Uhr im Lyrikecke-Voicekanal.");
                    break;
                case 5:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Túrin Turambar der tragische Held: Mehr über ihn am Mittwoch den 25.03 ab 18 Uhr.");
                    break;
                case 6:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Die Abenteuer des Tom Bombadil noch nie selbst gelesen? Vorlesen lassen ist besser! \n" +
                            "Sonntag 29.03. ab 18 Uhr.");
                    break;
                case 7:
                    embed.setTitle("#TolkientrotztCorona");
                    embed.setDescription("Zwischen den Hauptevents des Tolkien Reading Days kann jeder Zuhörer selbst Gedichte oder Briefe von J.R.R. Tolkien vorschlagen oder sogar vorlesen!");
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
