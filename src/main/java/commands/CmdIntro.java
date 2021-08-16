package commands;

import core.DatabaseHandler;
import core.MessageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.PlaylistChecker;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class CmdIntro {
    //TODO: comment and rework
    public static void intro(SlashCommandEvent event) throws Exception {
        // preparing msg
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);
        embed.setTitle(MessageActions.getLocalizedString("intro_title", "user", event.getUser().getId()));
        switch (event.getSubcommandName()) {
            // setting the text to a explanation, where you can find the intros
            case "l":
            case "list":
                embed.setDescription(MessageActions.getLocalizedString("intro_list", "user", event.getUser().getId()));
                break;
            case "s":
            case "set":
                
                if (event.getOption("intro_set_intro").getAsString().equalsIgnoreCase("nothing")) {
                    String[] answerIntrolist = DatabaseHandler.database(event.getGuild().getId(), "select intro from users where id = '" + event.getUser().getId() + "'");
                    String[] introlist;
                    try {
                        assert answerIntrolist != null;
                        introlist = answerIntrolist[0].split("#")[1].split("&");
                    } catch (Exception e) {
                        introlist = null;
                    }

                    StringBuilder updatedIntros = new StringBuilder();

                    updatedIntros.append(event.getOption("intro_set_intro").getAsString()).append("#");
                    assert introlist != null;
                    for (String str : introlist) {
                        updatedIntros.append(str).append("&");
                    }

                    DatabaseHandler.database(event.getGuild().getId(), "update users set intro = '" + updatedIntros.toString() + "' where id = '" + event.getUser().getId() + "'");

                    embed.setDescription(MessageActions.getLocalizedString("intro_equiped", "user", event.getUser().getId())
                            .replace("[USER]", event.getUser().getAsMention()).replace("[INTRO]", event.getOption("intro_set_intro").getAsString()));
                } else {
                    String[] answerIntrolist = DatabaseHandler.database(event.getGuild().getId(), "select intro from users where id = '" + event.getUser().getId() + "'");
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
                            if (str.equals(event.getOption("intro_set_intro").getAsString())) {
                                inChache = true;
                                break;
                            }
                        }
                    }
                    // checking if the user has the intro he wants to equip
                    if (inChache) {
                        StringBuilder updatedIntros = new StringBuilder();

                        updatedIntros.append(event.getOption("intro_set_intro").getAsString()).append("#");
                        for (String str : introlist) {
                            updatedIntros.append(str).append("&");
                        }

                        DatabaseHandler.database(event.getGuild().getId(), "update users set intro = '" + updatedIntros.toString() + "' where id = '" + event.getUser().getId() + "'");

                        embed.setDescription(MessageActions.getLocalizedString("intro_equiped", "user", event.getUser().getId())
                                .replace("[USER]", event.getUser().getAsMention()).replace("[INTRO]", event.getOption("intro_set_intro").getAsString()));
                    } else {
                        embed.setDescription(MessageActions.getLocalizedString("intro_not_in_inv", "user", event.getUser().getId())
                                .replace("[USER]", event.getUser().getAsMention()).replace("[INTRO]", event.getOption("intro_set_intro").getAsString()));
                    }
                }
                break;
            case "i":
            case "inventory":
            case "c":
            case "cache":
                String[] introlist1;
                try {
                    introlist1 = Objects.requireNonNull(DatabaseHandler.database(event.getGuild().getId(), "select intro from users where id = '" + event.getUser().getId() + "'"))[0]
                            .split("#")[1].split("&");
                } catch (Exception e) {
                    introlist1 = null;
                }
                // triggers if you have no voice intros
                if (introlist1 == null) {
                    embed.setDescription(MessageActions.getLocalizedString("intro_no_intros", "user", event.getUser().getId())
                            .replace("[USER]", event.getUser().getAsMention()));
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
                    embed.setDescription(MessageActions.getLocalizedString("intro_cache", "user", event.getUser().getId())
                            .replace("[USER]", event.getUser().getAsTag()) + sb.toString());
                }

                break;
            // preparing a msg with all price-categories
            case "p":
            case "prize":
            case "price":
                embed.setDescription(MessageActions.getLocalizedString("intro_price", "user", event.getUser().getId()));
                break;
            case "g":
            case "b":
            case "get":
            case "buy":
                if (event.getOption("intro_buy_intro").getAsString().contains("common") || event.getOption("intro_buy_intro").getAsString().contains("rare") || event.getOption("intro_buy_intro").getAsString().contains("epic") || event.getOption("intro_buy_intro").getAsString().contains("legendary") || event.getOption("intro_buy_intro").getAsString().contains("custom")) {
                    // getting the user intros
                    String[] answer = DatabaseHandler.database(event.getGuild().getId(), "select intro from users where id = '" + event.getUser().getId() + "'");
                    String[] introlist2;
                    try {
                        assert answer != null;
                        introlist2 = answer[0].split("#")[1].split("&");
                    } catch (Exception e) {
                        introlist2 = null;
                    }
                    // getting the coins of the user
                    String[] answer3;
                    answer3 = DatabaseHandler.database(event.getGuild().getId(), "select coins from users where id = '" + event.getUser().getId() + "'");
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
                            if (introlist2[j].equals(event.getOption("intro_buy_intro").getAsString())) {
                                inChache = true;
                                break;
                            }
                            j++;
                        }
                    }

                    if (inChache) {
                        embed.setDescription(MessageActions.getLocalizedString("intro_already_in_inv", "user", event.getUser().getId())
                                .replace("[USER]", event.getUser().getAsMention()).replace("[INTRO]", "'" + event.getOption("intro_buy_intro").getAsString() + "'"));
                    } else {
                        String pl;
                        int lenght;
                        if (event.getOption("intro_buy_intro").getAsString().contains("-")) {
                            int prize = 0;
                            boolean all_right = true;
                            try {
                                switch (event.getOption("intro_buy_intro").getAsString().split("-")[0]) {
                                    case "common":
                                        prize = 150;
                                        pl = "PL_epOfFugDagfFqqNqvykuieqyxngEISt";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) < 1 || Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "rare":
                                        prize = 300;
                                        pl = "PL_epOfFugDagG-R8IjY42YW2Qwy2S45jK";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) < 1 || Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "epic":
                                        prize = 1500;
                                        pl = "PL_epOfFugDagi0OcxJvJ3ZdDvRUOOuGQJ";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) < 1 || Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "legendary":
                                        prize = 3000;
                                        pl = "PL_epOfFugDaivDeYCaiEJXZklYvpUyv3-";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) < 1 || Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                    case "custom":
                                        prize = 5000;
                                        pl = "PL_epOfFugDag4MhC046KsXgG4eg9mVesk";
                                        lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                                        if (Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) < 1 || Integer.parseInt(event.getOption("intro_buy_intro").getAsString().split("-")[1]) > lenght) {
                                            all_right = false;
                                        }
                                        break;
                                }
                            } catch (Exception e) {
                                embed.setDescription(MessageActions.getLocalizedString("error_title", "user", event.getUser().getId()));
                                break;
                            }

                            if (coins < prize) {
                                embed.setDescription(MessageActions.getLocalizedString("intro_no_coins", "user", event.getUser().getId())
                                        .replace("[USER]", event.getUser().getAsMention()));
                            } else if (!all_right) {
                                embed.setDescription(MessageActions.getLocalizedString("intro_no_exist", "user", event.getUser().getId())
                                        .replace("[USER]", event.getUser().getAsMention()));
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
                                newIntros.append(event.getOption("intro_buy_intro").getAsString());
                                try {
                                    DatabaseHandler.database(event.getGuild().getId(), "update users set intro = '" + newIntros.toString() + "' where id = '" + event.getUser().getId() + "'");
                                } catch (Exception e) {
                                    DatabaseHandler.database(event.getGuild().getId(), "insert into users (intro) values ('" + newIntros.toString() + "')");
                                }
                                DatabaseHandler.database(event.getGuild().getId(), "update users set coins = " + (coins - prize) + " where id = '" + event.getUser().getId() + "'");
                                embed.setDescription(MessageActions.getLocalizedString("intro_bought_success", "user", event.getUser().getId())
                                        .replace("[USER]", event.getUser().getAsMention()).replace("[INTRO]", "'" + event.getOption("intro_buy_intro").getAsString() + "'"));
                            }
                        } else {
                            boolean finish = false;
                            int prize = 0;
                            String award = null;
                            while (!finish) {
                                switch (event.getOption("intro_buy_intro").getAsString()) {
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
                                            break;
                                        }
                                        j++;
                                    }
                                }


                            }
                            if (coins < prize) {
                                embed.setDescription(MessageActions.getLocalizedString("intro_no_coins", "user", event.getUser().getId())
                                        .replace("[USER]", event.getUser().getAsMention()));
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
                                DatabaseHandler.database(event.getGuild().getId(), "update users set intro = '" + newIntros.toString() + "' where id = '" + event.getUser().getId() + "'");
                                DatabaseHandler.database(event.getGuild().getId(), "update users set coins = " + (coins - prize) + " where id = '" + event.getUser().getId() + "'");
                                assert award != null;
                                embed.setDescription(MessageActions.getLocalizedString("intro_bought_success", "user", event.getUser().getId())
                                        .replace("[USER]", event.getUser().getAsMention()).replace("[INTRO]", award));

                            }
                        }
                    }
                } else {
                    embed.setDescription(MessageActions.getLocalizedString("intro_no_exist", "user", event.getUser().getId())
                            .replace("[USER]", event.getUser().getAsMention()));
                }
                break;
        }
        event.replyEmbeds(embed.build()).queue();
    }
}