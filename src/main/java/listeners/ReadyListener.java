package listeners;

import audio.InitScreamBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.SECRETS;
import util.STATIC;
import util.STREAM;

public class ReadyListener extends ListenerAdapter {

    private long members;

    public void onReady(@NotNull ReadyEvent event) {

        StringBuilder out = new StringBuilder("\nThis bot is running on following servers: \n");
        for (Guild g : event.getJDA().getGuilds()) {
            if(!g.getId().equals(STATIC.GUILD_ID)) {
                g.leave().queue();
            } else {
                members += g.getMembers().size();
                out.append(g.getName()).append(" (").append(g.getId()).append(")  ").append("Nutzeranzahl: ").append(g.getMembers().size()).append("\n");
            }
        }

        System.out.println(out);
        System.out.println("\nInsgesamte Nutzeranzahl: " + members);
        try {
            STREAM.startStream(event.getJDA().getGuilds().get(0));
            STREAM.isStarted = true;
            for (VoiceChannel vc : event.getJDA().getGuilds().get(0).getVoiceChannels()) {
                if (!vc.getMembers().isEmpty()) {
                    InitScreamBot.main(SECRETS.TOKENSCREAM, event, event.getJDA().getGuilds().get(0), vc, null);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}

