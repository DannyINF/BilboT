package special;

import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

class weeklyTopicAnnouncements extends ListenerAdapter {
    private final Properties prop = new Properties();
    private final EmbedBuilder embed = new EmbedBuilder();
    private InputStream input = null;

    public void onMessageReceived(MessageReceivedEvent event) {
        String status = "deactivated";
        try {
            status = modulesChecker.moduleStatus("event", event.getGuild().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // checks activation
        if (status.equals("activated")) {
            String topic;
            String next_topic;
            try {
                input = new FileInputStream("Properties/Weekly-Topic/weeklyTopicCore.properties");
                prop.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int i = Integer.parseInt(prop.getProperty("announcement"));
            topic = prop.getProperty("current_topic");
            next_topic = prop.getProperty("new_topic");

            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
            if (i >= 125) {
                embed.setColor(Color.MAGENTA);
                int random = ThreadLocalRandom.current().nextInt(0, 10);
                switch (random) {
                    case 0:
                        embed.setTitle("Du bist ein HdR-Fan der ersten Stunde?");
                        embed.setDescription("Diskutiere im " + event.getGuild().getTextChannelsByName("\ud83d\udcc5-woechentliches-thema", true).get(0).getAsMention() + " mit deinesgleichen \u00FCber spannende Themen!");
                        break;
                    case 1:
                        embed.setTitle("Bock auf mehr XP?");
                        embed.setDescription("Beteilige dich beim " + event.getGuild().getTextChannelsByName("\ud83d\udcc5-woechentliches-thema", true).get(0).getAsMention() + " und erhalte einen 2x-Boost!");
                        break;
                    case 2:
                        embed.setTitle("Lust auf spannende Diskussionen?");
                        embed.setDescription("Das " + event.getGuild().getTextChannelsByName("\ud83d\udcc5-woechentliches-thema", true).get(0).getAsMention() + " ist genau richtig f\u00FCr dich!");
                        break;
                    case 3:
                        embed.setTitle("Schon das w\u00F6chentliche Thema besucht?");
                        embed.setDescription("Interessante Diskussionen, heftige Erkenntnisse!");
                        break;
                    case 4:
                        embed.setTitle("Magst du es zu diskutieren?");
                        embed.setDescription("Komm zum " + event.getGuild().getTextChannelsByName("\ud83d\udcc5-woechentliches-thema", true).get(0).getAsMention() + "!");
                        break;
                    case 5:
                        embed.setTitle("W\u00F6chentliches Thema n\u00E4chste Woche:");
                        embed.setDescription(next_topic);
                        break;
                    case 6:
                        embed.setTitle("W\u00F6chentliches Thema diese Woche:");
                        embed.setDescription(topic);
                        break;
                    case 7:
                        embed.setTitle("H\u00F6r' nur her und lass dir sagen:");
                        embed.setDescription("Komm zum " + event.getGuild().getTextChannelsByName("\ud83d\udcc5-woechentliches-thema", true).get(0).getAsMention() + " und beantworte unsere Fragen!");
                        break;
                    case 8:
                        embed.setTitle("Du wei\u00DFt etwas \u00FCber \"" + topic + "\"?");
                        embed.setDescription("Dann komm zum " + event.getGuild().getTextChannelsByName("\ud83d\udcc5-woechentliches-thema", true).get(0).getAsMention() + " und teile dein Wissen!");
                        break;
                }
                if (!event.getTextChannel().getName().toLowerCase().contains("chat") && !event.getTextChannel().getName().toLowerCase().contains("diskussion")) {
                    i++;
                } else {
                    i = 0;
                    messageActions.selfDestroyEmbedMSG(embed.build(), 20000, event);
                }

            }
            try {
                OutputStream output = new FileOutputStream("Properties/Weekly-Topic/weeklyTopicCore.properties");
                prop.setProperty("announcement", String.valueOf(i));
                prop.store(output, null);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
