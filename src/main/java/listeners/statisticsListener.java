package listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class statisticsListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] arguments1 = {"users", "id = '" + event.getAuthor().getId() + "'", "3", "words", "msg", "chars"};
        String[] answer1 = null;
        try {
            answer1 = core.databaseHandler.database(event.getGuild().getId(), "select", arguments1);
        } catch (SQLException ignored) {
        }
        int words;
        int msg;
        int chars;

        try {
            assert answer1 != null;
            words = Integer.parseInt(answer1[0]);
        } catch (Exception e) {
            words = 0;
        }
        try {
            msg = Integer.parseInt(answer1[1]);
        } catch (Exception e) {
            msg = 0;
        }
        try {
            chars = Integer.parseInt(answer1[2]);
        } catch (Exception e) {
            chars = 0;
        }

        int newWords = words + event.getMessage().getContentRaw().split(" ").length;
        int newMsg = msg + 1;
        int newChars = chars + event.getMessage().getContentRaw().length();

        String[] arguments3 = {"users", "id = '" + event.getAuthor().getId() + "'", "words", String.valueOf(newWords), "msg", String.valueOf(newMsg), "chars", String.valueOf(newChars)};
        try {
            core.databaseHandler.database(event.getGuild().getId(), "update", arguments3);
        } catch (SQLException ignored) {
        }
    }
}