package listeners;

import core.CommandHandler;
import core.CommandParser;
import core.MessageActions;
import core.PermissionChecker;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandsListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.startsWith(STATIC.PREFIX)) {
            if (PermissionChecker.checkRole(STATIC.getVerified(), event.getMember())) {

                try {
                    CommandHandler.handleCommand(CommandParser.parser(message, event));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!message.startsWith(STATIC.PREFIX + "talk"))
                    MessageActions.logCommand(event);

            } else {
                event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", du musst dich erst verifizieren, um Befehle nutzen zu können.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
            }

            if (!event.getChannel().getName().toLowerCase().contains("spam") && !event.getAuthor().equals(event.getJDA().getSelfUser())) {
                if (STATIC.addCommandSpammer(event.getAuthor().getId()) == 0)
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", bitte f\u00fchre Befehle nur im Channel " + Objects.requireNonNull(event.getGuild().getTextChannelById("409055450802159616")).getAsMention() + " aus!").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
            }
        }
    }
}