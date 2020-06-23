package listeners;

import core.messageActions;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class spamListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        try {
            List list = null;
            int k = 100;
            while (list == null && k > 0) {
                try {
                    list = event.getChannel().getHistory().retrievePast(k).complete();
                } catch (Exception ignored) {
                }
                k--;
            }
            if (list == null)
                return;

            int i = 0;
            int counter = 0;
            int maxcounter = 0;
            int size = 0;
            String string3 = "M:" + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + ":";
            String string1 = list.get(i).toString().replace(string3, "")
                    .substring(0, list.get(i).toString().replace(string3, "")
                            .length() - 20);
            i++;
            String string2;
            if (string1.length() > 0) {
                while (i < list.size() && size < 5) {
                    if (list.get(i).toString().contains(string3)) {
                        string2 = list.get(i).toString().replace(string3, "")
                                .substring(0, list.get(i).toString().replace(string3, "")
                                        .length() - 20);
                        if (!string1.equals(string2))
                            counter = 0;
                        else
                            counter++;
                        size++;
                    }
                    if (counter > maxcounter) {
                        maxcounter = counter;
                    }
                    i++;
                    if (i == list.size()) {
                        size = 5;
                    }
                }
                if (!event.getAuthor().isBot() && !event.getChannel().getName().contains("spam")) {
                    if (maxcounter > 3) {
                        new commands.cmdBan().action(new String[]{event.getAuthor().getAsMention(), "1",
                                messageActions.getLocalizedString("spam_reason", "user", event.getAuthor().getId()).replace("[MESSAGE]", string1)}, event);
                    } else if (maxcounter > 2) {
                        MessageBuilder builder = new MessageBuilder();
                        builder.setContent(messageActions.getLocalizedString("spam_warn2", "user", event.getAuthor().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()));
                        messageActions.selfDestroyMSG(builder.build(), 15000, event);
                    } else if (maxcounter > 1) {
                        MessageBuilder builder = new MessageBuilder();
                        builder.setContent(messageActions.getLocalizedString("spam_warn1", "user", event.getAuthor().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()));
                        messageActions.selfDestroyMSG(builder.build(), 15000, event);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
