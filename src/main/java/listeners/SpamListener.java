package listeners;

import commands.CmdBan;
import core.MessageActions;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpamListener extends ListenerAdapter {
    //TODO: filter spam according to different rules (user- and channel-dynamic)
}
