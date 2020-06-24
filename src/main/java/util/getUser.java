package util;

import core.messageActions;
import net.dv8tion.jda.api.entities.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class getUser {
    public static Member getMemberFromInput(String[] args, User author, Guild guild, TextChannel tx) {
        Member member = null;

        try {
            if (args[0].startsWith("<") && args[0].contains(">") && args[0].contains("@")) {
                member = guild.getMemberById(args[0].replace("@", "").replace("<", "")
                        .replace(">", "").replace("!", ""));
                return member;
            } else {
                try {
                    int i = 0;
                    StringBuilder sb = new StringBuilder();
                    while (i < args.length) {
                        sb.append(args[i]);
                        i++;
                    }
                    try {
                        member = guild.getMemberById(sb.toString());
                        return member;
                    } catch (Exception e1) {
                        try {
                            member = guild.getMembersByEffectiveName(sb.toString(), true).get(0);
                            return member;
                        } catch (Exception e2) {
                            member = guild.getMembersByName(sb.toString(), true).get(0);
                            return member;

                        }
                    }

                } catch (Exception ee) {
                    tx.sendMessage(messageActions.getLocalizedString("user_not_found", "user", author.getId())
                            .replace("[ARGUMENT]", args[1])).queue(msg -> msg.delete().queueAfter(4, TimeUnit.SECONDS));
                }
            }
        } catch (Exception e) {
            tx.sendMessage(messageActions.getLocalizedString("user_not_found", "user", author.getId())
                    .replace("[ARGUMENT]", args[1])).queue(msg -> msg.delete().queueAfter(4, TimeUnit.SECONDS));
        }
        return member;
    }
}
