package listeners;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class privateListener extends ListenerAdapter {
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().contains("BilboT deaktivieren!") && event.getAuthor().getId().equals("277746420281507841")) {
            event.getAuthor().openPrivateChannel().queue((PrivateChannel channel) -> {
                channel.sendMessage("BilboT wurde deaktiviert!").queue();
                event.getJDA().shutdownNow();
            });
        }
    }
}
