package commands;

import core.permissionChecker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
//TODO: Update

public class cmdRoles implements Command {
    private static void createRank(MessageReceivedEvent event, Color color, String rang, Boolean mentionable, Boolean hoisted) {
        try {
            event.getGuild().getRolesByName(rang, true).get(0);
        } catch (Exception e) {
            event.getGuild().createRole().setColor(color).setName(rang)
                    .setMentionable(mentionable).setHoisted(hoisted)
                    .setPermissions(Permission.CREATE_INSTANT_INVITE).setPermissions(Permission.MESSAGE_READ)
                    .setPermissions(Permission.MESSAGE_HISTORY).setPermissions(Permission.MESSAGE_ADD_REACTION)
                    .setPermissions(Permission.NICKNAME_CHANGE).setPermissions(Permission.VIEW_CHANNEL)
                    .setPermissions(Permission.VOICE_SPEAK).setPermissions(Permission.VOICE_CONNECT)
                    .setPermissions(Permission.MESSAGE_ATTACH_FILES).setPermissions(Permission.MESSAGE_EMBED_LINKS)
                    .complete();
        }
    }

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
            String[] raenge = {
                    "Level 500 bis \u221E", "Level 490 bis 499", "Level 480 bis 489", "Level 470 bis 479", "Level 460 bis 469",
                    "Level 450 bis 459", "Level 440 bis 449", "Level 430 bis 439", "Level 420 bis 429", "Level 410 bis 419",
                    "Level 400 bis 409", "Level 390 bis 399", "Level 380 bis 389", "Level 370 bis 379", "Level 360 bis 369",
                    "Level 350 bis 359", "Level 340 bis 349", "Level 330 bis 339", "Level 320 bis 329", "Level 310 bis 319",
                    "Level 300 bis 309", "Level 290 bis 299", "Level 280 bis 289", "Level 270 bis 279", "Level 260 bis 269",
                    "Level 250 bis 259", "Level 240 bis 249", "Level 230 bis 239", "Level 220 bis 229", "Level 210 bis 219",
                    "Level 200 bis 209", "Level 190 bis 199", "Level 180 bis 189", "Level 170 bis 179", "Level 160 bis 169",
                    "Level 150 bis 159", "Level 140 bis 149", "Level 130 bis 139", "Level 120 bis 129", "Level 110 bis 119",
                    "Level 100 bis 109", "Level 90 bis 99", "Level 80 bis 89", "Level 70 bis 79", "Level 60 bis 69",
                    "Level 50 bis 59", "Level 40 bis 49", "Level 30 bis 39", "Level 20 bis 29", "Level 10 bis 19",
                    "Level 0 bis 9"};
            String[] loreraenge = {
                    "Maia", "Calaquende", "Moriquende", "Dunadan", "Adan"
            };
            Color adan = Color.decode("#ffcc2b");
            Color dunedan = Color.decode("#ffaf2a");
            Color moriquende = Color.decode("#ff9c26");
            Color calaquende = Color.decode("#ff721e");
            Color maia = Color.decode("#ff4b27");
            int j = 0;
            String lorerang;
            while (j < loreraenge.length) {
                lorerang = loreraenge[j];
                if (j >= 4) {
                    createRank(event, adan, lorerang, true, true);
                } else if (j >= 3) {
                    createRank(event, dunedan, lorerang, true, true);
                } else if (j >= 2) {
                    createRank(event, moriquende, lorerang, true, true);
                } else if (j >= 1) {
                    createRank(event, calaquende, lorerang, true, true);
                } else {
                    createRank(event, maia, lorerang, true, true);
                }
                j++;
            }

            int i = 0;
            String rang;
            while (i < raenge.length) {
                rang = raenge[i];
                if (i >= 46) {
                    createRank(event, adan, rang, false, false);
                } else if (i >= 36) {
                    createRank(event, dunedan, rang, false, false);
                } else if (i >= 21) {
                    createRank(event, moriquende, rang, false, false);
                } else if (i >= 1) {
                    createRank(event, calaquende, rang, false, false);
                } else {
                    createRank(event, maia, rang, false, false);
                }
                i++;
            }
        }

    }
}
