package util;

import core.messageActions;
import net.dv8tion.jda.api.entities.*;

import java.util.Timer;
import java.util.TimerTask;

public class getUser {
    public static Member getMemberFromInput(String[] args, User author, Guild guild, TextChannel tx) {
        Member member = null;
        try {
            if (args[0].contains("<") && args[0].contains(">") && args[0].contains("@")) {
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
                        member = guild.getMembersByEffectiveName(sb.toString(), true).get(0);
                        return member;
                    } catch (Exception e1) {
                        try {
                            member = guild.getMemberById(sb.toString());
                            return member;
                        } catch (Exception e2) {
                            try {
                                member = guild.getMembersByName(sb.toString(), true).get(0);
                                return member;
                            } catch (Exception ignored) {
                            }
                        }
                    }

                } catch (Exception ee) {
                    Message msg = tx.sendMessage(messageActions.getLocalizedString("user_not_found", "user", author.getId())
                            .replace("[ARGUMENT]", args[1])).complete();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 4000);
                }
            }
        } catch (Exception e) {
            Message msg = tx.sendMessage(messageActions.getLocalizedString("user_not_found", "user", author.getId())
                    .replace("[ARGUMENT]", args[1])).complete();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    msg.delete().queue();
                }
            }, 4000);
        }
        return member;
    }
}
