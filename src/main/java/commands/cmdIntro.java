package commands;

import core.databaseHandler;
import core.messageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.PlaylistChecker;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class cmdIntro implements Command {

    private Integer lenght = 0;

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws Exception {

        // preparing msg
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);
        embed.setTitle(messageActions.getLocalizedString("intro_title", "user", event.getAuthor().getId()));
        switch (args[0]) {
            // setting the text to a explanation, where you can find the intros
            case "l":
            case "list":
                embed.setDescription(messageActions.getLocalizedString("intro_list", "user", event.getAuthor().getId()));
                break;
            case "s":
            case "set":
                if (args[1].toLowerCase().equals("nothing")) {
                    String[] arguments = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "intro"};
                    String[] answerIntrolist = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
                    String[] introlist;
                    try {
                        assert answerIntrolist != null;
                        introlist = answerIntrolist[0].split("#")[1].split("&");
                    } catch (Exception e) {
                        introlist = null;
                    }

                    StringBuilder updatedIntros = new StringBuilder();

                    updatedIntros.append(args[1]).append("#");
                    for (String str : introlist) {
                        updatedIntros.append(str).append("&");
                    }

                    String[] arguments4 = {"users", "id = '" + event.getAuthor().getId() + "'", "intro", "'" + updatedIntros.toString() + "'"};
                    core.databaseHandler.database(event.getGuild().getId(), "update", arguments4);

                    embed.setDescription(messageActions.getLocalizedString("intro_equiped", "user", event.getAuthor().getId())
                            .replace("[USER]", event.getAuthor().getAsMention()).replace("[INTRO]", args[1]));
                } else {
                    String[] arguments = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "intro"};
                    String[] answerIntrolist = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
                    String[] introlist;
                    try {
                        assert answerIntrolist != null;
                        introlist = answerIntrolist[0].split("#")[1].split("&");
                    } catch (Exception e) {
                        introlist = null;
                    }

                    boolean inChache = false;
                    if (introlist != null) {
                        for (String str : introlist) {
                            if (str.equals(args[1])) {
                                inChache = true;
                            }
                        }
                    }
                    // checking if the user has the intro he wants to equip
                    if (inChache) {
                        StringBuilder updatedIntros = new StringBuilder();

                        updatedIntros.append(args[1]).append("#");
                        for (String str : introlist) {
                            updatedIntros.append(str).append("&");
                        }

                        String[] arguments4 = {"users", "id = '" + event.getAuthor().getId() + "'", "intro", "'" + updatedIntros.toString() + "'"};
                        core.databaseHandler.database(event.getGuild().getId(), "update", arguments4);

                        embed.setDescription(messageActions.getLocalizedString("intro_equiped", "user", event.getAuthor().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()).replace("[INTRO]", args[1]));
                    } else {
                        embed.setDescription(messageActions.getLocalizedString("intro_not_in_inv", "user", event.getAuthor().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()).replace("[INTRO]", args[1]));
                    }
                }
                break;
            // preparing a help msg
            case "h":
            case "help":
                embed.setDescription(messageActions.getLocalizedString("intro_help", "user", event.getAuthor().getId()));
                break;
            case "i":
            case "inventory":
            case "c":
            case "cache":
                String[] arguments1 = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "intro"};
                String[] introlist1;
                try {
                    introlist1 = Objects.requireNonNull(databaseHandler.database(event.getGuild().getId(), "select", arguments1))[0].split("#")[1].split("&");
                } catch (Exception e) {
                    introlist1 = null;
                }
                // triggers if you have no voice intros
                if (introlist1 == null) {
                    embed.setDescription(messageActions.getLocalizedString("intro_no_intros", "user", event.getAuthor().getId())
                            .replace("[USER]", event.getAuthor().getAsMention()));
                } else {
                    // preparing a list of all voice intros, the user has
                    StringBuilder sb = new StringBuilder();
                    int i = 0;
                    while (i < introlist1.length - 1) {
                        sb.append(introlist1[i]);
                        sb.append(", ");
                        i++;
                    }
                    sb.append(introlist1[i]);
                    embed.setDescription(messageActions.getLocalizedString("intro_cache", "user", event.getAuthor().getId())
                            .replace("[USER]", event.getAuthor().getAsTag()) + sb.toString());
                }

                break;
            // preparing a msg with all price-categories
            case "p":
            case "prize":
            case "price":
                embed.setDescription(messageActions.getLocalizedString("intro_price", "user", event.getAuthor().getId()));
                break;
            case "g":
            case "b":
            case "get":
            case "buy":
                if (args[1].contains("common") || args[1].contains("rare") || args[1].contains("epic") || args[1].contains("legendary") || args[1].contains("custom")) {
                    // getting the user intros
                    String[] arguments2 = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "intro"};
                    String[] answer = core.databaseHandler.database(event.getGuild().getId(), "select", arguments2);
                    String[] introlist2;
                    try {
                        assert answer != null;
                        introlist2 = answer[0].split("#")[1].split("&");
                    } catch (Exception e) {
                        introlist2 = null;
                    }
                    // getting the coins of the user
                    String[] arguments3 = {"users", "id = '" + event.getAuthor().getId() + "'", "1", "coins"};
                    String[] answer3;
                    answer3 = core.databaseHandler.database(event.getGuild().getId(), "select", arguments3);
                    int coins;
                    try {
                        assert answer3 != null;
                        coins = Integer.parseInt(answer3[0]);
                    } catch (Exception e) {
                        coins = 0;
                    }
                    boolean inChache = false;
                    int j = 0;
                    if (introlist2 != null) {
                        while (j < introlist2.length) {
                            if (introlist2[j].equals(args[1])) {
                                inChache = true;
                            }
                            j++;
                        }
                    }

                    if (inChache) {
                        embed.setDescription(messageActions.getLocalizedString("intro_already_in_inv", "user", event.getAuthor().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()).replace("[INTRO]", "'" + args[1] + "'"));
                    } else {
                        String pl;
                        if (args[1].contains("-")) {
                            int prize = 0;
                            boolean all_right = true;
                            try {
                                switch (args[1].split("-")[0]) {
                                    case "common":
                                        prize = 150;
                                        pl = "PL_epOfFugDagfFqqNqvykuieqyxngEISt";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(args[1].split("-")[1]) < 1 || Integer.parseInt(args[1].split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "rare":
                                        prize = 300;
                                        pl = "PL_epOfFugDagG-R8IjY42YW2Qwy2S45jK";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(args[1].split("-")[1]) < 1 || Integer.parseInt(args[1].split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "epic":
                                        prize = 1500;
                                        pl = "PL_epOfFugDagi0OcxJvJ3ZdDvRUOOuGQJ";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(args[1].split("-")[1]) < 1 || Integer.parseInt(args[1].split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "legendary":
                                        prize = 3000;
                                        pl = "PL_epOfFugDaivDeYCaiEJXZklYvpUyv3-";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(args[1].split("-")[1]) < 1 || Integer.parseInt(args[1].split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "custom":
                                        prize = 5000;
                                        pl = "PL_epOfFugDag4MhC046KsXgG4eg9mVesk";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(args[1].split("-")[1]) < 1 || Integer.parseInt(args[1].split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                }
                            } catch (Exception e) {
                                embed.setDescription(messageActions.getLocalizedString("error_title", "user", event.getAuthor().getId()));
                                break;
                            }

                            if (coins < prize) {
                                embed.setDescription(messageActions.getLocalizedString("intro_no_coins", "user", event.getAuthor().getId())
                                        .replace("[USER]", event.getAuthor().getAsMention()));
                            } else if (!all_right) {
                                embed.setDescription(messageActions.getLocalizedString("intro_no_exist", "user", event.getAuthor().getId())
                                        .replace("[USER]", event.getAuthor().getAsMention()));
                            } else {
                                StringBuilder newIntros = new StringBuilder();
                                newIntros.append(answer[0].split("#")[0]).append("#");
                                int k = 0;
                                if (introlist2 != null) {
                                    while (k < introlist2.length) {
                                        newIntros.append(introlist2[k]);
                                        newIntros.append("&");
                                        k++;
                                    }
                                }
                                newIntros.append(args[1]);
                                String[] arguments4 = {"users", "id = '" + event.getAuthor().getId() + "'", "intro", "'" + newIntros.toString() + "'"};
                                String[] argumentsi4 = {"users", "intro", "'" + newIntros.toString() + "'"};
                                try {
                                    core.databaseHandler.database(event.getGuild().getId(), "update", arguments4);
                                } catch (Exception e) {
                                    core.databaseHandler.database(event.getGuild().getId(), "insert", argumentsi4);
                                }
                                String[] arguments5 = {"users", "id = '" + event.getAuthor().getId() + "'", "coins", String.valueOf(coins - prize)};
                                core.databaseHandler.database(event.getGuild().getId(), "update", arguments5);
                                embed.setDescription(messageActions.getLocalizedString("intro_bought_success", "user", event.getAuthor().getId())
                                        .replace("[USER]", event.getAuthor().getAsMention()).replace("[INTRO]", "'" + args[1] + "'"));
                            }
                        } else {
                            boolean finish = false;
                            int prize = 0;
                            String award = null;
                            while (!finish) {
                                switch (args[1]) {
                                    case "common":
                                        pl = "PL_epOfFugDagfFqqNqvykuieqyxngEISt";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        int win_int = ThreadLocalRandom.current().nextInt(1, lenght);
                                        award = "common-" + win_int;
                                        prize = 50;
                                        break;
                                    case "rare":
                                        pl = "PL_epOfFugDagG-R8IjY42YW2Qwy2S45jK";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        win_int = ThreadLocalRandom.current().nextInt(1, lenght);
                                        award = "rare-" + win_int;
                                        prize = 100;
                                        break;
                                    case "epic":
                                        pl = "PL_epOfFugDagi0OcxJvJ3ZdDvRUOOuGQJ";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        win_int = ThreadLocalRandom.current().nextInt(1, lenght);
                                        award = "epic-" + win_int;
                                        prize = 500;
                                        break;
                                    case "legendary":
                                        pl = "PL_epOfFugDaivDeYCaiEJXZklYvpUyv3-";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        win_int = ThreadLocalRandom.current().nextInt(1, lenght);
                                        award = "legendary-" + win_int;
                                        prize = 1000;
                                        break;


                                }
                                j = 0;
                                finish = true;
                                if (introlist2 != null) {
                                    while (j < introlist2.length) {
                                        if (introlist2[j].equals(award)) {
                                            finish = false;
                                        }
                                        j++;
                                    }
                                }


                            }
                            if (coins < prize) {
                                embed.setDescription(messageActions.getLocalizedString("intro_no_coins", "user", event.getAuthor().getId())
                                        .replace("[USER]", event.getAuthor().getAsMention()));
                            } else {
                                StringBuilder newIntros = new StringBuilder();
                                newIntros.append(answer[0].split("#")[0]).append("#");
                                int k = 0;
                                if (introlist2 != null) {
                                    while (k < introlist2.length) {
                                        newIntros.append(introlist2[k]);
                                        newIntros.append("&");
                                        k++;
                                    }
                                }
                                newIntros.append(award);
                                String[] arguments4 = {"users", "id = '" + event.getAuthor().getId() + "'", "intro", "'" + newIntros.toString() + "'"};
                                core.databaseHandler.database(event.getGuild().getId(), "update", arguments4);
                                String[] arguments5 = {"users", "id = '" + event.getAuthor().getId() + "'", "coins", String.valueOf(coins - prize)};
                                core.databaseHandler.database(event.getGuild().getId(), "update", arguments5);
                                assert award != null;
                                embed.setDescription(messageActions.getLocalizedString("intro_bought_success", "user", event.getAuthor().getId())
                                        .replace("[USER]", event.getAuthor().getAsMention()).replace("[INTRO]", award));

                            }
                        }
                    }
                } else {
                    embed.setDescription(messageActions.getLocalizedString("intro_no_exist", "user", event.getAuthor().getId())
                            .replace("[USER]", event.getAuthor().getAsMention()));
                }
                break;
        }
        event.getTextChannel().sendMessage(embed.build()).queue();
    }


}
