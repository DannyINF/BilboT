package listeners;

import core.commandHandler;
import core.commandParser;
import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class commandsListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith(STATIC.PREFIX)) {
            if (permissionChecker.checkRole(new Role[] {event.getGuild().getRolesByName("verified", true).get(0)}, event.getMember())) {
                if (!event.getMessage().getContentRaw().startsWith(STATIC.PREFIX + "talk")) {
                    messageActions.logCommand(event);
                }

                try {
                    commandHandler.handleCommand(commandParser.parser(event.getMessage().getContentRaw(), event));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", du musst dich erst verifizieren, um Befehle nutzen zu können.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
            }

            if (!event.getChannel().getName().toLowerCase().contains("spam") && !event.getAuthor().equals(event.getJDA().getSelfUser())) {
                if (STATIC.addCommandSpammer(event.getAuthor().getId()) == 0)
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", bitte f\u00fchre Befehle nur im Channel " + Objects.requireNonNull(event.getGuild().getTextChannelById("409055450802159616")).getAsMention() + " aus!").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
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
