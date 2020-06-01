package listeners;

import core.commandHandler;
import core.commandParser;
import core.messageActions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

public class commandsListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith(STATIC.PREFIX)) {
            if (!event.getMessage().getContentRaw().startsWith(STATIC.PREFIX + "talk")) {
                messageActions.logCommand(event);
            }

            try {
                commandHandler.handleCommand(commandParser.parser(event.getMessage().getContentRaw(), event));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!event.getMessage().getContentRaw().startsWith(STATIC.PREFIX + "music")) {
                    event.getMessage().delete().queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
