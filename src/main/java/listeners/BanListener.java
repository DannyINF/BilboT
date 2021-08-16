package listeners;

import core.DatabaseHandler;
import core.MessageActions;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.sql.SQLException;


public class BanListener extends ListenerAdapter {


    public void onGuildBan(@NotNull GuildBanEvent event) {
        try {
            DatabaseHandler.database(event.getGuild().getId(), "update users set verifystatus = FALSE where id = '" + event.getUser().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //TODO: log ban

        //TODO: write message in welcome
    }

    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        // log unban
    }


}
