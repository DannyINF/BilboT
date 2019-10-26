package listeners;

import core.messageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class addonCommunicationListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            if (event.getAuthor().isBot() && event.getTextChannel().getName().equals("bilbot-communication-channel")) {
                String id = event.getAuthor().getId();
                if (id.equals("486085339530788894") || id.equals("486089278019993611") || id.equals("486089728437780480")) {
                    //args: 5: NAME; 6: URL; 7: SIZE; 8: TITLE; 9: OLDVOLUME; 10: NEWVOLUME; 11: VOLUME; 12: NUMBER; 13: isPlaying
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("Music");
                    embed.setColor(new Color(33, 237, 90));
                    String[] args = event.getMessage().getContentRaw().trim().split("-###-");
                    TextChannel channel = event.getGuild().getTextChannelById(args[3]);
                    Member member = event.getGuild().getMemberById(args[2]);
                    assert member != null;
                    String msg = messageActions.getLocalizedString(args[0], "user", member.getUser().getId())
                            .replace("[NAME]", args[4]).replace("[URL]", args[5]).replace("[SIZE]", args[6]).replace("[TITLE]", args[7])
                            .replace("[OLDVOLUME]", args[8]).replace("[NEWVOLUME]", args[9]).replace("[VOLUME]", args[10]).replace("[NUMBER]", args[11]);

                    switch (event.getAuthor().getId()) {
                        case "486085339530788894":
                            embed.setFooter("Player: Thorin", null);
                            embed.setDescription(msg);
                            assert channel != null;
                            channel.sendMessage(embed.build()).queue();
                            break;
                        case "486089278019993611":
                            embed.setFooter("Player: Balin", null);
                            embed.setDescription(msg);
                            assert channel != null;
                            channel.sendMessage(embed.build()).queue();
                            break;
                        case "486089728437780480":
                            embed.setFooter("Player: Bombur", null);
                            embed.setDescription(msg);
                            assert channel != null;
                            channel.sendMessage(embed.build()).queue();
                            break;
                    }
                    event.getMessage().delete().queue();
                }
            }
        } catch (Exception ignored) {
        }
    }
}
