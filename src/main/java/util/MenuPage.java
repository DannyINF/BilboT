package util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;

import java.util.ArrayList;

public class MenuPage {
    public int identification;
    public String icon;
    public String headline;
    public String description;
    public ArrayList<MenuPage> linked_pages;
    public MenuPage return_page;

    public MenuPage(int id, String i, String h, String d) {
        identification = id;
        icon = i;
        headline = h;
        description = d;
    }

    public int postMenuPage(PrivateMessageReactionAddEvent event) {
        String linked_menupages = "";
        ArrayList<String> icons = new ArrayList<>();

        for (MenuPage menuPage : linked_pages) {
            linked_menupages += "\n" + menuPage.icon + " " + menuPage.headline;
            icons.add(menuPage.icon);
        }
        if (return_page!=null) {
            linked_menupages += "\n\u21A9\uFE0F Zur\u00FCck";
            icons.add("\u21A9\uFE0F");
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(headline);
        embedBuilder.setDescription(description + "\n" + linked_menupages);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
            for (String str : icons) {
                message.addReaction(str).queue();
            }
        });
        return identification;
    }

    public int postMenuPage(GuildMessageReceivedEvent event) {
        String linked_menupages = "";
        ArrayList<String> icons = new ArrayList<>();

        for (MenuPage menuPage : linked_pages) {
            linked_menupages += "\n" + menuPage.icon + " " + menuPage.headline;
            icons.add(menuPage.icon);
        }
        if (return_page!=null) {
            linked_menupages += "\n\u21A9\uFE0F Zur\u00FCck";
            icons.add("\u21A9\uFE0F");
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(headline);
        embedBuilder.setDescription(description + "\n" + linked_menupages);
        event.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build()).queue(message -> {
            for (String str : icons) {
                message.addReaction(str).queue();
            }
        }));
        return identification;
    }

    public void linkPages(ArrayList<MenuPage> list) {
        linked_pages = list;
    }

    public void returnPage(MenuPage rp) {
        return_page = rp;
    }
}