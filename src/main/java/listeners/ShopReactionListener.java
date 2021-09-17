package listeners;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.MenuPage;
import util.SHOPS;
import util.STATIC;

public class ShopReactionListener extends ListenerAdapter
{
    //TODO: comment
    @Override
    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event)
    {
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(msg -> msg.retrieveReactionUsers(event.getReactionEmote().getEmoji()).queue(users ->
        {
            if (users.size() > 1 && !event.getUser().isBot() && event.getUserId().equals("277746420281507841"))
            {
                MenuPage mp = SHOPS.getMenuPage(STATIC.getShopstep());
                if (event.getReactionEmote().getEmoji().equals("\u21A9\uFE0F")) {
                    STATIC.changeShopstep(mp.return_page.postMenuPage(event));
                    return;
                }
                for (MenuPage linked : mp.linked_pages) {
                    if (linked.icon.equals(event.getReactionEmote().getEmoji())) {
                        linked.returnPage(mp);
                        STATIC.changeShopstep(linked.postMenuPage(event));
                        break;
                    }
                }
                /*
                switch (STATIC.fakeDB.shopstep) {
                    case 0:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83C\uDFA7":
                                STATIC.fakeDB.setShopstep(1);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Voiceintros");
                                embedBuilder.setDescription("An welche Rarit\u00E4t hast du denn gedacht?\n" +
                                        "\uD83C\uDDE8 - Common\n" +
                                        "\uD83C\uDDF7 - Rare\n" +
                                        "\uD83C\uDDEA - Epic\n" +
                                        "\uD83C\uDDF1 - Legendary\n" +
                                        "\u21A9\uFE0F zur\u00FCck zum Shop");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDE8").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDEA").queue();
                                    message.addReaction("\uD83C\uDDF1").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83D\uDC8C":
                                if (STATIC.fakeDB.isActivated) {
                                    STATIC.fakeDB.setShopstep(3);
                                    embedBuilder = new EmbedBuilder();
                                    embedBuilder.setTitle("Shop - Willkommensbanner");
                                    embedBuilder.setDescription("An welche Rarit\u00E4t hast du denn gedacht?\n" +
                                            "\uD83C\uDDE8 - Common\n" +
                                            "\uD83C\uDDF7 - Rare\n" +
                                            "\uD83C\uDDEA - Epic\n" +
                                            "\uD83C\uDDF1 - Legendary\n" +
                                            "\u21A9\uFE0F zur\u00FCck zum Shop");
                                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                        message.addReaction("\uD83C\uDDE8").queue();
                                        message.addReaction("\uD83C\uDDF7").queue();
                                        message.addReaction("\uD83C\uDDEA").queue();
                                        message.addReaction("\uD83C\uDDF1").queue();
                                        message.addReaction("\u21A9\uFE0F").queue();
                                    });
                                } else {
                                    STATIC.fakeDB.setShopstep(3);
                                    embedBuilder = new EmbedBuilder();
                                    embedBuilder.setTitle("Shop - Aktivierung des Willkommensbanners");
                                    embedBuilder.setDescription("Zuerst musst du deinen Banner f\u00FCr `500` Coins aktivieren! M\u00F6chtest du dies jetzt tun?\n");
                                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                        message.addReaction("\u2705").queue();
                                        message.addReaction("\uD83D\uDEAB").queue();
                                    });
                                }
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(5);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Inventar");
                                embedBuilder.setDescription("Du besitzt im Moment " + ((STATIC.fakeDB.intros.split(",").length == 0 || STATIC.fakeDB.intros.split(",")[0].equals("")) ? "keine" : ("`" + STATIC.fakeDB.intros.split(",").length) + "`") + " " + ((STATIC.fakeDB.intros.split(",").length == 1 && STATIC.fakeDB.intros.split(",")[0].length()>0) ? "Intro" : "Intros") +
                                        " und hast " + ((STATIC.fakeDB.equipped_intro.length() == 0 || STATIC.fakeDB.equipped_intro.split(",")[0].equals("")) ? "kein Intro" : "das Intro **" + STATIC.fakeDB.equipped_intro + "**") + " ausger\u00FCstet.\n\n" +
                                        "Du besitzt im Moment " + ((STATIC.fakeDB.backgrounds.split(",").length == 0 || STATIC.fakeDB.backgrounds.split(",")[0].equals("")) ? "keine" : ("`" + STATIC.fakeDB.backgrounds.split(",").length) + "`") + " " + ((STATIC.fakeDB.backgrounds.split(",").length == 1 && STATIC.fakeDB.backgrounds.split(",")[0].length()>0) ? "Hintergrund," : "Hintergr\u00FCnde,") +
                                        " " + ((STATIC.fakeDB.borders.split(",").length == 0 || STATIC.fakeDB.borders.split(",")[0].equals("")) ? "keine" : ("`" + STATIC.fakeDB.borders.split(",").length) + "`") + " " + ((STATIC.fakeDB.borders.split(",").length == 1 && STATIC.fakeDB.borders.split(",")[0].length()>0) ? "Rahmen," : "Rahmen,") +
                                        " " + ((STATIC.fakeDB.texts.split(",").length == 0 || STATIC.fakeDB.texts.split(",")[0].equals("")) ? "keine" : ("`" + STATIC.fakeDB.texts.split(",").length) + "`") + " " + ((STATIC.fakeDB.texts.split(",").length == 1 && STATIC.fakeDB.texts.split(",")[0].length()>0) ? "Text" : "Texte") + " und " +
                                        " " + ((STATIC.fakeDB.symbols.split(",").length == 0 || STATIC.fakeDB.symbols.split(",")[0].equals("")) ? "keine" : ("`" + STATIC.fakeDB.symbols.split(",").length) + "`") + " " + ((STATIC.fakeDB.symbols.split(",").length == 1 && STATIC.fakeDB.symbols.split(",")[0].length()>0) ? "Symbol" : "Symbole") + ".\n" +
                                        "Du hast " + ((STATIC.fakeDB.equipped_background.length() == 0 || STATIC.fakeDB.equipped_background.split(",")[0].equals("")) ? "nichts" : ("den Hintergrund **" + STATIC.fakeDB.equipped_background + "**, den Rahmen **" + STATIC.fakeDB.equipped_border + "**, " +
                                        "den Text **" + STATIC.fakeDB.equipped_text) + "** und " + ((STATIC.fakeDB.equipped_symbol.length() == 0) ? "kein Symbol" : "das Symbol **" + STATIC.fakeDB.equipped_symbol + "**")) + " ausger\u00FCstet.\n\n" +
                                        (!(STATIC.fakeDB.intros.split(",").length == 0 || STATIC.fakeDB.intros.split(",")[0].equals("") )? "\uD83C\uDDEE Voiceintros inspizieren\n" : "") +
                                        (!(STATIC.fakeDB.backgrounds.split(",").length == 0 || STATIC.fakeDB.backgrounds.split(",")[0].equals("") )?"\uD83C\uDDED Hintergr\u00FCnde ansehen\n" : "") +
                                        (!(STATIC.fakeDB.borders.split(",").length == 0 || STATIC.fakeDB.borders.split(",")[0].equals("") )?"\uD83C\uDDF7 Rahmen n\u00E4her betrachten\n" : "") +
                                        (!(STATIC.fakeDB.texts.split(",").length == 0 || STATIC.fakeDB.texts.split(",")[0].equals("") )?"\uD83C\uDDF9 Texte angucken\n" : "") +
                                        (!(STATIC.fakeDB.symbols.split(",").length == 0 || STATIC.fakeDB.symbols.split(",")[0].equals("") )?"\uD83C\uDDF8 Symbole unter die Lupe nehmen\n" : "") +
                                        "\u21A9\uFE0F zur\u00FCck zum Shop");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    if (!(STATIC.fakeDB.intros.split(",").length == 0 || STATIC.fakeDB.intros.split(",")[0].equals("")))
                                        message.addReaction("\uD83C\uDDEE").queue();
                                    if (!(STATIC.fakeDB.backgrounds.split(",").length == 0 || STATIC.fakeDB.backgrounds.split(",")[0].equals("")))
                                        message.addReaction("\uD83C\uDDED").queue();
                                    if (!(STATIC.fakeDB.borders.split(",").length == 0 || STATIC.fakeDB.borders.split(",")[0].equals("")))
                                        message.addReaction("\uD83C\uDDF7").queue();
                                    if (!(STATIC.fakeDB.texts.split(",").length == 0 || STATIC.fakeDB.texts.split(",")[0].equals("")))
                                        message.addReaction("\uD83C\uDDF9").queue();
                                    if (!(STATIC.fakeDB.symbols.split(",").length == 0 || STATIC.fakeDB.symbols.split(",")[0].equals("")))
                                        message.addReaction("\uD83C\uDDF8").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;

                        }
                        break;
                    case 1:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83C\uDDE8":
                                STATIC.fakeDB.setShopstep(2);
                                break;
                            case "\uD83C\uDDF7":
                                STATIC.fakeDB.setShopstep(2);
                                break;
                            case "\uD83C\uDDEA":
                                STATIC.fakeDB.setShopstep(2);
                                break;
                            case "\uD83C\uDDF1":
                                STATIC.fakeDB.setShopstep(2);
                                break;
                            case "\uD83D\uDEAB":
                            case "\u21A9\uFE0F":
                                STATIC.fakeDB.setShopstep(0);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop");
                                embedBuilder.setDescription("Willkommen im Shop des BilboTs. Hier kannst du dir verschiedene kosmetische Artikel für Coins kaufen.\n" +
                                        "Wonach suchst du denn?\n" +
                                        "\uD83C\uDFA7 Voiceintros\n" +
                                        "\uD83D\uDC8C Willkommensbanner\n" +
                                        "\uD83D\uDCBC Inventar");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDFA7").queue();
                                    message.addReaction("\uD83D\uDC8C").queue();
                                    message.addReaction("\uD83D\uDCBC").queue();
                                });
                                break;
                        }
                        break;
                    //Banner Bestandteil
                    case 3:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83C\uDDE8":
                                STATIC.fakeDB.setShopstep(4);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common'");
                                embedBuilder.setDescription("Welchen Teil des Banners willst du kaufen?\n\n" +
                                        "\uD83C\uDDED - Hintergrund\n" +
                                        "\uD83C\uDDF7 - Rahmen\n" +
                                        "\uD83C\uDDF9 - Text\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDED").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDF9").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF7":
                                STATIC.fakeDB.setShopstep(5);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare'");
                                embedBuilder.setDescription("Welchen Teil des Banners willst du kaufen?\n\n" +
                                        "\uD83C\uDDED - Hintergrund\n" +
                                        "\uD83C\uDDF7 - Rahmen\n" +
                                        "\uD83C\uDDF9 - Text\n" +
                                        "\uD83C\uDDF8 - Symbol\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDED").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDF9").queue();
                                    message.addReaction("\uD83C\uDDF8").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDEA":
                                STATIC.fakeDB.setShopstep(6);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic'");
                                embedBuilder.setDescription("Welchen Teil des Banners willst du kaufen?\n\n" +
                                        "\uD83C\uDDED - Hintergrund\n" +
                                        "\uD83C\uDDF7 - Rahmen\n" +
                                        "\uD83C\uDDF9 - Text\n" +
                                        "\uD83C\uDDF8 - Symbol\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDED").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDF9").queue();
                                    message.addReaction("\uD83C\uDDF8").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF1":
                                STATIC.fakeDB.setShopstep(7);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary'");
                                embedBuilder.setDescription("Welchen Teil des Banners willst du kaufen?\n\n" +
                                        "\uD83C\uDDED - Hintergrund\n" +
                                        "\uD83C\uDDF7 - Rahmen\n" +
                                        "\uD83C\uDDF9 - Text\n" +
                                        "\uD83C\uDDF8 - Symbol\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDED").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDF9").queue();
                                    message.addReaction("\uD83C\uDDF8").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            //nach Freischaltung
                            case "\u2705":
                                STATIC.fakeDB.setShopstep(3);
                                STATIC.fakeDB.setActivated(true);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner");
                                embedBuilder.setDescription("Gl\u00FCckwunsch! Du hast deinen Willkommenbanner erfolgreich freigeschaltet und zwei gew\u00F6hnliche Hintergr\u00FCnde," +
                                        " einen gew\u00F6hnlichen Text und einen gew\u00F6hnlichen Rahmen erhalten!\n\nAn welche Rarit\u00E4t hast du denn gedacht?\n" +
                                        "\uD83C\uDDE8 - Common\n" +
                                        "\uD83C\uDDF7 - Rare\n" +
                                        "\uD83C\uDDEA - Epic\n" +
                                        "\uD83C\uDDF1 - Legendary\n" +
                                        "\u21A9\uFE0F zur\u00FCck zum Shop");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDE8").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDEA").queue();
                                    message.addReaction("\uD83C\uDDF1").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83D\uDEAB":
                            case "\u21A9\uFE0F":
                                STATIC.fakeDB.setShopstep(0);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop");
                                embedBuilder.setDescription("Willkommen im Shop des BilboTs. Hier kannst du dir verschiedene kosmetische Artikel für Coins kaufen.\n" +
                                        "Wonach suchst du denn?\n" +
                                        "\uD83C\uDFA7 Voiceintros\n" +
                                        "\uD83D\uDC8C Willkommensbanner\n" +
                                        "\uD83D\uDCBC Inventar");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDFA7").queue();
                                    message.addReaction("\uD83D\uDC8C").queue();
                                    message.addReaction("\uD83D\uDCBC").queue();
                                });
                                break;
                        }
                        break;
                    //Banner Random/Auswahl Common
                    case 4:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83C\uDDED":
                                STATIC.fakeDB.setShopstep(8);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' - Hintergrund");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Hintergrund im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Hintergrund kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Hintergrund ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Hintergrund kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF7":
                                STATIC.fakeDB.setShopstep(9);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' - Rahmen");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Rahmen im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Rahmen kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Rahmen ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Rahmen kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF9":
                                STATIC.fakeDB.setShopstep(10);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' - Text");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Text im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Text kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Text ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Text kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            //zurück zur Startseite
                            case "\u21A9\uFE0F":
                                STATIC.fakeDB.setShopstep(3);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner");
                                embedBuilder.setDescription("An welche Rarit\u00E4t hast du denn gedacht?\n" +
                                        "\uD83C\uDDE8 - Common\n" +
                                        "\uD83C\uDDF7 - Rare\n" +
                                        "\uD83C\uDDEA - Epic\n" +
                                        "\uD83C\uDDF1 - Legendary\n" +
                                        "\u21A9\uFE0F zur\u00FCck zum Shop");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDE8").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDEA").queue();
                                    message.addReaction("\uD83C\uDDF1").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                        }
                        break;
                    //Banner Random/Auswahl Rare
                    case 5:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83C\uDDED":
                                STATIC.fakeDB.setShopstep(11);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' - Hintergrund");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Hintergrund im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Hintergrund kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Hintergrund ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Hintergrund kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF7":
                                STATIC.fakeDB.setShopstep(12);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' - Rahmen");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Rahmen im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Rahmen kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Rahmen ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Rahmen kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF9":
                                STATIC.fakeDB.setShopstep(13);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' - Text");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Text im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Text kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Text ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Text kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF8":
                                STATIC.fakeDB.setShopstep(14);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' - Symbol");
                                embedBuilder.setDescription("Hast du schon einen bestimmtes Symbol im Sinn oder m\u00F6chstest du ein zuf\u00E4lliges Symbol kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmtes Symbol ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lliges Symbol kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\u21A9\uFE0F":
                                STATIC.fakeDB.setShopstep(3);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner");
                                embedBuilder.setDescription("An welche Rarit\u00E4t hast du denn gedacht?\n" +
                                        "\uD83C\uDDE8 - Common\n" +
                                        "\uD83C\uDDF7 - Rare\n" +
                                        "\uD83C\uDDEA - Epic\n" +
                                        "\uD83C\uDDF1 - Legendary\n" +
                                        "\u21A9\uFE0F zur\u00FCck zum Shop");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDE8").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDEA").queue();
                                    message.addReaction("\uD83C\uDDF1").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                        }
                        break;
                    //Banner Random/Auswahl Epic
                    case 6:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83C\uDDED":
                                STATIC.fakeDB.setShopstep(15);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' - Hintergrund");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Hintergrund im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Hintergrund kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Hintergrund ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Hintergrund kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF7":
                                STATIC.fakeDB.setShopstep(16);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' - Rahmen");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Rahmen im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Rahmen kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Rahmen ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Rahmen kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF9":
                                STATIC.fakeDB.setShopstep(17);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' - Text");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Text im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Text kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Text ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Text kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF8":
                                STATIC.fakeDB.setShopstep(18);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' - Symbol");
                                embedBuilder.setDescription("Hast du schon einen bestimmtes Symbol im Sinn oder m\u00F6chstest du ein zuf\u00E4lliges Symbol kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmtes Symbol ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lliges Symbol kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\u21A9\uFE0F":
                                STATIC.fakeDB.setShopstep(3);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner");
                                embedBuilder.setDescription("An welche Rarit\u00E4t hast du denn gedacht?\n" +
                                        "\uD83C\uDDE8 - Common\n" +
                                        "\uD83C\uDDF7 - Rare\n" +
                                        "\uD83C\uDDEA - Epic\n" +
                                        "\uD83C\uDDF1 - Legendary\n" +
                                        "\u21A9\uFE0F zur\u00FCck zum Shop");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDE8").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDEA").queue();
                                    message.addReaction("\uD83C\uDDF1").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                        }
                        break;
                    //Banner Random/Auswahl Legendary
                    case 7:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83C\uDDED":
                                STATIC.fakeDB.setShopstep(19);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' - Hintergrund");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Hintergrund im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Hintergrund kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Hintergrund ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Hintergrund kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF7":
                                STATIC.fakeDB.setShopstep(20);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' - Rahmen");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Rahmen im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Rahmen kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Rahmen ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Rahmen kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF9":
                                STATIC.fakeDB.setShopstep(21);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' - Text");
                                embedBuilder.setDescription("Hast du schon einen bestimmten Text im Sinn oder m\u00F6chstest du einen zuf\u00E4lligen Text kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmten Text ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lligen Text kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\uD83C\uDDF8":
                                STATIC.fakeDB.setShopstep(22);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' - Symbol");
                                embedBuilder.setDescription("Hast du schon einen bestimmtes Symbol im Sinn oder m\u00F6chstest du ein zuf\u00E4lliges Symbol kaufen?\n" +
                                        "\uD83D\uDD0E  - bestimmtes Symbol ausw\u00E4hlen\n" +
                                        "\uD83C\uDFB2 - zuf\u00E4lliges Symbol kaufen\n" +
                                        "\u21A9\uFE0F zur\u00FCck zur Auswahl");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDD0E").queue();
                                    message.addReaction("\uD83C\uDFB2").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                            case "\u21A9\uFE0F":
                                STATIC.fakeDB.setShopstep(3);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner");
                                embedBuilder.setDescription("An welche Rarit\u00E4t hast du denn gedacht?\n" +
                                        "\uD83C\uDDE8 - Common\n" +
                                        "\uD83C\uDDF7 - Rare\n" +
                                        "\uD83C\uDDEA - Epic\n" +
                                        "\uD83C\uDDF1 - Legendary\n" +
                                        "\u21A9\uFE0F zur\u00FCck zum Shop");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83C\uDDE8").queue();
                                    message.addReaction("\uD83C\uDDF7").queue();
                                    message.addReaction("\uD83C\uDDEA").queue();
                                    message.addReaction("\uD83C\uDDF1").queue();
                                    message.addReaction("\u21A9\uFE0F").queue();
                                });
                                break;
                        }
                        break;
                    case 8:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(23);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' Hintergrund - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Hintergrundes ein, den du f\u00FCr `150` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(24);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' Hintergrund - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Hintergrund f\u00FCr `75` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(4);
                                break;
                        }
                        break;
                    case 9:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(25);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' Rahmen - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Rahmens ein, den du f\u00FCr `150` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(26);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' Rahmen - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Rahmen f\u00FCr `75` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(4);
                                break;
                        }
                        break;
                    case 10:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(27);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' Text - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Textes ein, den du f\u00FCr `200` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(28);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Common' Text - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Text f\u00FCr `100` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(4);
                                break;
                        }
                        break;
                    case 11:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(29);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Hintergrund - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Hintergrundes ein, den du f\u00FCr `300` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(30);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Hintergrund - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Hintergrund f\u00FCr `150` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(5);
                                break;
                        }
                        break;
                    case 12:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(31);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Rahmen - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Rahmens ein, den du f\u00FCr `300` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(32);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Rahmen - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Rahmen f\u00FCr `150` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(5);
                                break;
                        }
                        break;
                    case 13:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(33);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Text - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Textes ein, den du f\u00FCr `400` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(34);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Text - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Text f\u00FCr `200` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(5);
                                break;
                        }
                        break;
                    case 14:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(35);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Symbol - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Symbols ein, das du f\u00FCr `600` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(36);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Rare' Symbol - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du ein zuf\u00E4lliges Symbol f\u00FCr `300` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(5);
                                break;
                        }
                        break;
                    case 15:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(37);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Hintergrund - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Hintergrundes ein, den du f\u00FCr `700` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(38);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Hintergrund - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Hintergrund f\u00FCr `350` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(6);
                                break;
                        }
                        break;
                    case 16:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(39);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Rahmen - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Rahmens ein, den du f\u00FCr `700` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(40);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Rahmen - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Rahmen f\u00FCr `350` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(6);
                                break;
                        }
                        break;
                    case 17:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(41);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Text - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Textes ein, den du f\u00FCr `1.000` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(42);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Text - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Text f\u00FCr `500` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(6);
                                break;
                        }
                        break;
                    case 18:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(43);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Symbol - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Symbols ein, das du f\u00FCr `1.600` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(44);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Epic' Symbol - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du ein zuf\u00E4lliges Symbol f\u00FCr `800` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(6);
                                break;
                        }
                        break;
                    case 19:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(45);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Hintergrund - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Hintergrundes ein, den du f\u00FCr `1.500` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(46);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Hintergrund - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Hintergrund f\u00FCr `750` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(7);
                                break;
                        }
                        break;
                    case 20:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(47);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Rahmen - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Rahmens ein, den du f\u00FCr `1.500` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(48);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Rahmen - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Rahmen f\u00FCr `750` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(7);
                                break;
                        }
                        break;
                    case 21:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(49);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Text - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Textes ein, den du f\u00FCr `3.000` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(50);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Text - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du einen zuf\u00E4lligen Text f\u00FCr `1.500` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(7);
                                break;
                        }
                        break;
                    case 22:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\uD83D\uDD0E":
                                STATIC.fakeDB.setShopstep(51);
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Symbol - Kaufoption: Auswahl");
                                embedBuilder.setDescription("Bitte gib die Nummer des Symbols ein, das du f\u00FCr `4.000` Coins kaufen m\u00F6chtest.\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83C\uDFB2":
                                STATIC.fakeDB.setShopstep(52);
                                embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle("Shop - Willkommensbanner 'Legendary' Symbol - Kaufoption: Zufall");
                                embedBuilder.setDescription("M\u00F6chtest du ein zuf\u00E4lliges Symbol f\u00FCr `2.000` Coins kaufen?\n");
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                                    message.addReaction("\u2705").queue();
                                    message.addReaction("\uD83D\uDEAB").queue();
                                });
                                break;
                            case "\uD83D\uDCBC":
                                STATIC.fakeDB.setShopstep(7);
                                break;
                        }
                        break;
                    case 23:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(8);
                                break;
                        }
                        break;
                    case 24:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(8);
                                break;
                        }
                        break;
                    case 25:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(9);
                                break;
                        }
                        break;
                    case 26:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(9);
                                break;
                        }
                        break;
                    case 27:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(10);
                                break;
                        }
                        break;
                    case 28:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(10);
                                break;
                        }
                        break;
                    case 29:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(11);
                                break;
                        }
                        break;
                    case 30:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(11);
                                break;
                        }
                        break;
                    case 31:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(12);
                                break;
                        }
                        break;
                    case 32:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(12);
                                break;
                        }
                        break;
                    case 33:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(13);
                                break;
                        }
                        break;
                    case 34:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(13);
                                break;
                        }
                        break;
                    case 35:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(14);
                                break;
                        }
                        break;
                    case 36:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(14);
                                break;
                        }
                        break;
                    case 37:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(15);
                                break;
                        }
                        break;
                    case 38:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(15);
                                break;
                        }
                        break;
                    case 39:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(16);
                                break;
                        }
                        break;
                    case 40:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(16);
                                break;
                        }
                        break;
                    case 41:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(17);
                                break;
                        }
                        break;
                    case 42:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(17);
                                break;
                        }
                        break;
                    case 43:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(18);
                                break;
                        }
                        break;
                    case 44:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(18);
                                break;
                        }
                        break;
                    case 45:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(19);
                                break;
                        }
                        break;
                    case 46:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(19);
                                break;
                        }
                        break;
                    case 47:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(20);
                                break;
                        }
                        break;
                    case 48:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(20);
                                break;
                        }
                        break;
                    case 49:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(21);
                                break;
                        }
                        break;
                    case 50:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(21);
                                break;
                        }
                        break;
                    case 51:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(22);
                                break;
                        }
                        break;
                    case 52:
                        switch (event.getReactionEmote().getEmoji()) {
                            case "\u2705":
                            case "\uD83D\uDEAB":
                                STATIC.fakeDB.setShopstep(22);
                                break;
                        }
                        break;
                }

                if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDEE") && !event.getUser().isBot() && STATIC.fakeDB.shopstep == 5)
                {
                    STATIC.fakeDB.setShopstep(6);
                }
                else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDED") && !event.getUser().isBot() && STATIC.fakeDB.shopstep == 5)
                {
                    STATIC.fakeDB.setShopstep(6);
                }
                else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDF7") && !event.getUser().isBot() && STATIC.fakeDB.shopstep == 5)
                {
                    STATIC.fakeDB.setShopstep(6);
                }
                else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDF9") && !event.getUser().isBot() && STATIC.fakeDB.shopstep == 5)
                {
                    STATIC.fakeDB.setShopstep(6);
                }
                else if (event.getReactionEmote().getEmoji().equals("\uD83C\uDDF8") && !event.getUser().isBot() && STATIC.fakeDB.shopstep == 5)
                {
                    STATIC.fakeDB.setShopstep(6);
                }*/
            }
        }));
    }
}
