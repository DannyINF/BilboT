package listeners;

import core.DatabaseHandler;
import core.MessageActions;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.CHANNEL;
import util.SET_CHANNEL;

import java.sql.SQLException;
import java.util.Objects;


public class LeaveListener extends ListenerAdapter {
    //TODO: log leaving members (+msg in welcome)
}
