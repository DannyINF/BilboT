package util;

import java.util.ArrayList;

public class SHOPS {
    public static PageMenu shop;
    public static MenuPage starting;
    public static MenuPage intro, banner, inventory;
    public static MenuPage iCommon, iRare, iEpic, iLegendary, bCommon, bRare, bEpic, bLegendary, invIntros, invBackgrounds, invBorders, invTexts, invSymbols;
    public static MenuPage icRandom, irRandom, ieRandom, ilRandom;
    public static MenuPage icSelect, irSelect, ieSelect, ilSelect;
    public static void initShop() {
        //menupage = new MenuPage(id, "icon", "headline", "description", linkedFromMenuPage, returnMenuPage);

        ArrayList<MenuPage> linkedFromICRandom = new ArrayList<>();
        ArrayList<MenuPage> linkedFromICSelect = new ArrayList<>();

        ArrayList<MenuPage> linkedFromIRRandom = new ArrayList<>();
        ArrayList<MenuPage> linkedFromIRSelect = new ArrayList<>();

        ArrayList<MenuPage> linkedFromIERandom = new ArrayList<>();
        ArrayList<MenuPage> linkedFromIESelect = new ArrayList<>();

        ArrayList<MenuPage> linkedFromILRandom = new ArrayList<>();
        ArrayList<MenuPage> linkedFromILSelect = new ArrayList<>();

        icRandom = new MenuPage(19, "\uD83C\uDFB2", "headline", "description");
        icSelect = new MenuPage(20, "\uD83D\uDD0E", "headline", "description");

        irRandom = new MenuPage(21, "\uD83C\uDFB2", "headline", "description");
        irSelect = new MenuPage(22, "\uD83D\uDD0E", "headline", "description");

        ieRandom = new MenuPage(23, "\uD83C\uDFB2", "headline", "description");
        ieSelect = new MenuPage(24, "\uD83D\uDD0E", "headline", "description");

        ilRandom = new MenuPage(25, "\uD83C\uDFB2", "headline", "description");
        ilSelect = new MenuPage(26, "\uD83D\uDD0E", "headline", "description");

        ArrayList<MenuPage> linkedFromICommon = new ArrayList<>();
        ArrayList<MenuPage> linkedFromIRare = new ArrayList<>();
        ArrayList<MenuPage> linkedFromIEpic = new ArrayList<>();
        ArrayList<MenuPage> linkedFromILegendary = new ArrayList<>();

        ArrayList<MenuPage> linkedFromBCommon = new ArrayList<>();
        ArrayList<MenuPage> linkedFromBRare = new ArrayList<>();
        ArrayList<MenuPage> linkedFromBEpic = new ArrayList<>();
        ArrayList<MenuPage> linkedFromBLegendary = new ArrayList<>();

        ArrayList<MenuPage> linkedFromInvIntros = new ArrayList<>();
        ArrayList<MenuPage> linkedFromInvBackgrounds = new ArrayList<>();
        ArrayList<MenuPage> linkedFromInvBorders = new ArrayList<>();
        ArrayList<MenuPage> linkedFromInvTexts = new ArrayList<>();
        ArrayList<MenuPage> linkedFromInvSymbols = new ArrayList<>();

        iCommon = new MenuPage(4, "\uD83C\uDDE8", "Intro - Common", "Willst du f\u00FCr den Preis von `50` Coins ein zuf\u00E4lliges Intro kaufen oder willst du dir f\u00FCr `150` Coins ein bestimmtes aussuchen?");
        iRare = new MenuPage(5, "\uD83C\uDDF7", "Intro - Rare", "Willst du f\u00FCr den Preis von `100` Coins ein zuf\u00E4lliges Intro kaufen oder willst du dir f\u00FCr `300` Coins ein bestimmtes aussuchen?");
        iEpic = new MenuPage(6, "\uD83C\uDDEA", "Intro - Epic", "Willst du f\u00FCr den Preis von `500` Coins ein zuf\u00E4lliges Intro kaufen oder willst du dir f\u00FCr `1.500` Coins ein bestimmtes aussuchen?");
        iLegendary = new MenuPage(7, "\uD83C\uDDF1", "Intro - Legendary", "Willst du f\u00FCr den Preis von `1.000` Coins ein zuf\u00E4lliges Intro kaufen oder willst du dir f\u00FCr `3.000` Coins ein bestimmtes aussuchen?");

        bCommon = new MenuPage(9, "\uD83C\uDDE8", "Willkommensbanner - Common", "Welchen Teil des Banners willst du kaufen?");
        bRare = new MenuPage(10, "\uD83C\uDDF7", "Willkommensbanner - Rare", "Welchen Teil des Banners willst du kaufen?");
        bEpic = new MenuPage(11, "\uD83C\uDDEA", "Willkommensbanner - Epic", "Welchen Teil des Banners willst du kaufen?");
        bLegendary = new MenuPage(12, "\uD83C\uDDF1", "Willkommensbanner - Legendary", "Welchen Teil des Banners willst du kaufen?");

        invIntros = new MenuPage(14, "\uD83C\uDDEE", "headline", "description");
        invBackgrounds = new MenuPage(15, "\uD83C\uDDED", "headline", "description");
        invBorders = new MenuPage(16, "\uD83C\uDDF7", "headline", "description");
        invTexts = new MenuPage(17, "\uD83C\uDDF9", "headline", "description");
        invSymbols = new MenuPage(18, "\uD83C\uDDF8", "headline", "description");

        ArrayList<MenuPage> linkedFromIntro = new ArrayList<>();
        linkedFromIntro.add(iCommon);
        linkedFromIntro.add(iRare);
        linkedFromIntro.add(iEpic);
        linkedFromIntro.add(iLegendary);

        ArrayList<MenuPage> linkedFromBanner = new ArrayList<>();
        linkedFromBanner.add(bCommon);
        linkedFromBanner.add(bRare);
        linkedFromBanner.add(bEpic);
        linkedFromBanner.add(bLegendary);

        ArrayList<MenuPage> linkedFromInventory = new ArrayList<>();
        linkedFromInventory.add(invIntros);
        linkedFromInventory.add(invBackgrounds);
        linkedFromInventory.add(invBorders);
        linkedFromInventory.add(invTexts);
        linkedFromInventory.add(invSymbols);

        intro = new MenuPage(    1, "\uD83C\uDFA7", "Shop - Voiceintros", "An welche Rarit\u00E4t hast du denn gedacht?");
        banner = new MenuPage(   2, "\uD83D\uDC8C", "Shop - Willkommensbanner", "An welche Rarit\u00E4t hast du denn gedacht?");
        inventory = new MenuPage(3, "\uD83D\uDCBC", "Shop - Inventar", "Platzhalter");
        ArrayList<MenuPage> linkedFromStarting = new ArrayList<>();
        linkedFromStarting.add(intro);
        linkedFromStarting.add(banner);
        linkedFromStarting.add(inventory);

        starting = new MenuPage(0, null, "Shop", "Willkommen im Shop des BilboTs. Hier kannst du dir verschiedene kosmetische Artikel für Coins kaufen.\nWonach suchst du denn?");

        icRandom.linkPages(linkedFromICRandom);
        icSelect.linkPages(linkedFromICSelect);
        irRandom.linkPages(linkedFromIRRandom);
        irSelect.linkPages(linkedFromIRSelect);
        ieRandom.linkPages(linkedFromIERandom);
        ieSelect.linkPages(linkedFromIESelect);
        ilRandom.linkPages(linkedFromILRandom);
        ilSelect.linkPages(linkedFromILSelect);

        iCommon.linkPages(linkedFromICommon);
        iRare.linkPages(linkedFromIRare);
        iEpic.linkPages(linkedFromIEpic);
        iLegendary.linkPages(linkedFromILegendary);
        bCommon.linkPages(linkedFromBCommon);
        bRare.linkPages(linkedFromBRare);
        bEpic.linkPages(linkedFromBEpic);
        bLegendary.linkPages(linkedFromBLegendary);
        invIntros.linkPages(linkedFromInvIntros);
        invBackgrounds.linkPages(linkedFromInvBackgrounds);
        invBorders.linkPages(linkedFromInvBorders);
        invTexts.linkPages(linkedFromInvTexts);
        invSymbols.linkPages(linkedFromInvSymbols);

        intro.linkPages(linkedFromIntro);
        banner.linkPages(linkedFromBanner);
        inventory.linkPages(linkedFromInventory);
        starting.linkPages(linkedFromStarting);

        shop = new PageMenu(starting, 0);
        System.out.println("initialized shop");
    }

    public static MenuPage getMenuPage(int id) {
        MenuPage[] menuPages = {starting, intro, banner, inventory, iCommon, iRare, iEpic, iLegendary, bCommon, bRare,
                bEpic, bLegendary, invIntros, invBackgrounds, invBorders, invTexts, invSymbols, icRandom, irRandom,
                ieRandom, ilRandom, icSelect, irSelect, ieSelect, ilSelect};
        for (MenuPage mp : menuPages) {
            if (mp.identification == id) {
                return mp;
            }
        }
        return null;
    }
}
