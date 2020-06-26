package listeners;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class modReactionListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getChannel().getId().equals("434007950852489216")) {
            if (event.getMember().getUser().isBot())
                return;
            int reactionCount;
            String mode;
            switch (event.getReactionEmote().getEmoji()) {
                case "\u21A9":
                    reactionCount = 1;
                    mode = "troll";
                    break;
                case "\u2705":
                    reactionCount = 2;
                    mode = "nothing";
                    break;
                case "\uD83C\uDFAD":
                    reactionCount = 1;
                    mode = "discussion";
                    break;
                case "\u2B55":
                    reactionCount = 1;
                    mode = "exil";
                    break;
                case "\u26D4":
                    reactionCount = 2;
                    mode = "kick";
                    break;
                case "\uD83D\uDD28":
                    reactionCount = 3;
                    mode = "ban";
                    break;
                default:
                    return;
            }
            event.getChannel().retrieveMessageById(event.getMessageId()).queue(msg -> msg.retrieveReactionUsers(event.getReactionEmote().getEmoji()).queue(users -> {
                if (users.size() > reactionCount) {
                    msg.clearReactions().queue();
                    msg.addReaction("\u2611").queue();
                    executeAction(event, mode);
                }
            }));

        }
    }

    private static void executeAction(GuildMessageReactionAddEvent event, String mode) {
        switch (mode) {
            case "troll":
                break;
            case "nothing":
                break;
            case "discussion":
                break;
            case "exil":
                break;
            case "kick":
                break;
            case "ban":
                break;
        }
    }
}
