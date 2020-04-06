package listeners;

import core.commandHandlerMusic;
import core.commandParser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

public class commandsMusicListener extends ListenerAdapter {

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith(STATIC.PREFIX)) {

            try {
                commandHandlerMusic.handleCommand(commandParser.parser(event.getMessage().getContentRaw(), event));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
