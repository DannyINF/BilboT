package commands;

import core.permissionChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class cmdCreateRoles implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            ArrayList<String> raenge = new ArrayList<>();
            int rangzahl = 0;
            int partner;
            while (rangzahl <= 1040) {
                partner = rangzahl + 9;
                raenge.add("Level " + rangzahl + " bis Level " + partner);
                System.out.println("Added Level " + rangzahl + " bis Level " + partner);
                rangzahl += 10;
            }
            raenge.add("Level 1050 bis \u221e");
            System.out.println("Added Level 1050 bis \u221e");

            ArrayList<String> loreraenge = new ArrayList<>();
            loreraenge.add("Drúadan");
            loreraenge.add("Hobbit");
            loreraenge.add("Adan");
            loreraenge.add("Dúnadan");
            loreraenge.add("Peredhel");
            loreraenge.add("Moriquende");
            loreraenge.add("Calaquende");
            loreraenge.add("Maia");

            Color[] lorecolor = {
                    Color.decode("#fff9ba"), Color.decode("#fff53d"), Color.decode("#f7bf16"), Color.decode("#e68f0a"), Color.decode("#f3730d"), Color.decode("#f25511"), Color.decode("#bf3636"), Color.decode("#ca2a1a")
            };

            int i = 0;
            String rang;
            while (i<raenge.size()) {
                rang = raenge.get(i);
                if (i>=105) {
                    createRank(event, lorecolor[7], rang, false, false);
                } else if (i>=75) {
                    createRank(event, lorecolor[6], rang, false, false);
                } else if (i>=50) {
                    createRank(event, lorecolor[5], rang, false, false);
                } else if (i>=30) {
                    createRank(event, lorecolor[4], rang, false, false);
                } else if (i>=15) {
                    createRank(event, lorecolor[3], rang, false, false);
                } else if (i>=5) {
                    createRank(event, lorecolor[2], rang, false, false);
                } else if (i>=1) {
                    createRank(event, lorecolor[1], rang, false, false);
                } else {
                    createRank(event, lorecolor[0], rang, false, false);
                }
                i++;
            }


            int j = 0;
            for (String str : loreraenge) {
                createRank(event, lorecolor[j], str, true, true);
                j++;
            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            raenge.addAll(loreraenge);

            RoleOrderAction action = event.getGuild().modifyRolePositions();
            for (String str : raenge) {
                System.out.println(str);
                Role role = event.getGuild().getRolesByName(str, true).get(0);
                action.selectPosition(role.getPosition()).moveTo(role.getGuild().getRoles().size()-8);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            action.queue();
        }
    }

    private static void createRank(GuildMessageReceivedEvent event, Color color, String rang, Boolean mentionable, Boolean hoisted) {
        try {
            event.getGuild().getRolesByName(rang, true).get(0);
            System.out.println("Skipped " + rang);
        } catch (Exception e){
            event.getGuild().createRole().setColor(color).setName(rang)
                    .setMentionable(mentionable).setHoisted(hoisted)
                    .queue();

            System.out.println("Created " + rang);
        }
    }
}