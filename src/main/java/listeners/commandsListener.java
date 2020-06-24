package listeners;

import com.google.common.base.Stopwatch;
import core.commandHandler;
import core.commandParser;
import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalField;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Stopwatch.*;

public class commandsListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
            String message = event.getMessage().getContentRaw();

            if (message.startsWith(STATIC.PREFIX)) {
                if (permissionChecker.checkRole(STATIC.getVerified(), event.getMember())) {

                    try {
                        commandHandler.handleCommand(commandParser.parser(message, event));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!message.startsWith(STATIC.PREFIX + "talk"))
                        messageActions.logCommand(event);

                } else {
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", du musst dich erst verifizieren, um Befehle nutzen zu können.").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                }

                if (!event.getChannel().getName().toLowerCase().contains("spam") && !event.getAuthor().equals(event.getJDA().getSelfUser())) {
                    if (STATIC.addCommandSpammer(event.getAuthor().getId()) == 0)
                        event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", bitte f\u00fchre Befehle nur im Channel " + Objects.requireNonNull(event.getGuild().getTextChannelById("409055450802159616")).getAsMention() + " aus!").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                }

                try {
                    if (!message.startsWith(STATIC.PREFIX + "music"))
                        event.getMessage().delete().queue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}