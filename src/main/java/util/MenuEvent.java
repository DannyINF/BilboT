package util;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class MenuEvent {
    public String t;
    public String[] arguments;
    public MenuEvent(String type, String[] args) {
        t = type;
        arguments = args;
    }

    public void callEvent(PrivateMessageReactionAddEvent event) throws SQLException, IOException {
        switch (t) {
            case "CONDITION":
                MenuPage success = null;
                MenuPage failure = null;
                User user = event.getUser();
                boolean foundSuccess = false;
                boolean foundFailure = false;
                /*for (MenuPage mp : SHOPS.shop) {
                    if (!foundSuccess && mp.identification == Integer.parseInt(arguments[0])) {
                        foundSuccess = true;
                        success = mp;
                    } else if (!foundFailure && mp.identification == Integer.parseInt(arguments[1])) {
                        foundFailure = true;
                        failure = mp;
                    } else if (foundFailure && foundSuccess) {
                        break;
                    }
                }*/

                int userValue = Integer.parseInt(DatabaseHandler.database("388969412889411585", "select " + arguments[2] + " from users where id = '" + user.getId() + "'")[0]);
                int neededValue = Integer.parseInt(arguments[4]);
                boolean check = false;

                switch (arguments[3]) {
                    case "EQUAL":
                        check = userValue == neededValue;
                        break;
                    case "LEQ":
                        check = userValue <= neededValue;
                        break;
                    case "LESS":
                        check = userValue < neededValue;
                        break;
                    case "GREATER":
                        check = userValue > neededValue;
                        break;
                    case "GEQ":
                        check = userValue >= neededValue;
                        break;
                    case "NEQ":
                        check = userValue != neededValue;
                        break;
                }
                if (check)
                    success.postMenuPage(event);
                else
                    failure.postMenuPage(event);
                break;
            case "TRANSACTION":
                success = null;
                failure = null;
                user = event.getUser();
                foundSuccess = false;
                foundFailure = false;
                /*for (MenuPage mp : new SHOPS().shop.p) {
                    if (!foundSuccess && mp.identification == Integer.parseInt(arguments[0])) {
                        foundSuccess = true;
                        success = mp;
                    } else if (!foundFailure && mp.identification == Integer.parseInt(arguments[1])) {
                        foundFailure = true;
                        failure = mp;
                    } else if (foundFailure && foundSuccess) {
                        break;
                    }
                }*/

                userValue = Integer.parseInt(DatabaseHandler.database("388969412889411585", "select coins from users where id = '" + user.getId() + "'")[0]);
                neededValue = Integer.parseInt(arguments[2]);

                check = userValue >= neededValue;

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Transaktion");

                if (check) {
                    DatabaseHandler.database("388969412889411585", "update users set coins = coins - " + neededValue + " where id = '" + user.getId() + "'");
                    buyArticle(arguments[3], arguments[4], user.getId());
                    embedBuilder.setDescription("Die Transaktion \u00FCber `" + neededValue + "` Coins war erfolgreich!");
                    embedBuilder.setColor(Color.GREEN);
                    event.getChannel().sendMessageEmbeds(embedBuilder.build());
                    success.postMenuPage(event);
                } else {
                    embedBuilder.setDescription("Die Transaktion \u00FCber `" + neededValue + "` Coins war nicht erfolgreich!\nDu besitzt nicht gen\u00FCgend Coins.");
                    embedBuilder.setColor(Color.RED);
                    event.getChannel().sendMessageEmbeds(embedBuilder.build());
                    failure.postMenuPage(event);
                }

                break;

        }
    }

    private void buyArticle(String article, String id, String userId) throws IOException {
        switch (article) {
            case "RANDOM_INTRO":
                article = "INTRO";
                String pl = "";
                switch (id) {
                    case "common":
                        pl = "PL_epOfFugDagfFqqNqvykuieqyxngEISt";
                        break;
                    case "rare":
                        pl = "PL_epOfFugDagG-R8IjY42YW2Qwy2S45jK";
                        break;
                    case "epic":
                        pl = "PL_epOfFugDagi0OcxJvJ3ZdDvRUOOuGQJ";
                        break;
                    case "legendary":
                        pl = "PL_epOfFugDaivDeYCaiEJXZklYvpUyv3-";
                        break;
                }
                int lenght = Integer.parseInt(PlaylistChecker.check(pl).get(0).toString());
                int win_int;
                while (id == null) { //as long as id is in intros
                    win_int = ThreadLocalRandom.current().nextInt(1, lenght);
                    id = id + "-" + win_int;
                }
                break;
            case "RANDOM_BACKGROUND":
                break;
            case "RANDOM_BORDER":
                break;
            case "RANDOM_TEXT":
                break;
            case "RANDOM_SYMBOL":
                break;
        }
        //DatabaseHandler.database("388969412889411585", "update users set coins = coins - " + neededValue + " where id = '" + userId + "'");
    }
}
