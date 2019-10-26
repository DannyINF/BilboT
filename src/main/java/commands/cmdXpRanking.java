package commands;

import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

class cmdXpRanking {

    public static void action(String[] args, MessageReceivedEvent event) throws SQLException {
        String status = modulesChecker.moduleStatus("xp", event.getGuild().getId());
        if (status.equals("activated")) {
            int start;
            try {
                start = Integer.parseInt(args[1]);
            } catch (Exception e) {
                start = 1;
            }
            if (start == 0) {
                start++;
            }
            int i = 0;
            int k;
            String user;
            String name;
            int level;
            int xp;
            int coins;

            // <String, Integer> Map
            HashMap<String, Integer> map = new HashMap<>();
            while (i < event.getGuild().getMembers().size()) {
                String[] arguments = {"users", "id = '" + event.getGuild().getMembers().get(i).getUser().getId() + "'", "1", "xp"};
                String[] answer = {};
                try {
                    answer = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    assert answer != null;
                    xp = Integer.parseInt(answer[0]);
                } catch (Exception e) {
                    xp = 0;
                }

                if (!event.getGuild().getMembers().get(i).getUser().isBot()) {
                    map.put(event.getGuild().getMembers().get(i).getUser().getId(), xp);
                }
                i++;

            }

            Comparator<String> comparator = new ValueComparator<>(map);
            TreeMap<String, Integer> result = new TreeMap<>(comparator);
            result.putAll(map);


            StringBuilder sb = new StringBuilder();

            if (start < result.size()) {
                i = start - 1;
            } else {
                i = 0;
            }
            while (i < result.size() && i < (start + 9)) {
                user = result.keySet().toString().replace("[", "").replace("]", "")
                        .split(", ")[i];
                String[] arguments = {"users", "id = '" + user + "'", "3", "xp", "level", "coins"};
                String[] answer = {};
                try {
                    answer = core.databaseHandler.database(event.getGuild().getId(), "select", arguments);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    assert answer != null;
                    xp = Integer.parseInt(answer[0]);
                } catch (Exception e) {
                    xp = 0;
                }
                try {
                    level = Integer.parseInt(answer[1]);
                } catch (Exception e) {
                    level = 0;
                }
                try {
                    coins = Integer.parseInt(answer[2]);
                } catch (Exception e) {
                    coins = 0;
                }


                name = Objects.requireNonNull(event.getGuild().getMemberById(user)).getUser().getAsTag();

                if (name.equals(event.getAuthor().getAsTag()) && (i == start - 1 || i == 0)) {
                    sb.append("```css\n");
                } else if (!name.equals(event.getAuthor().getAsTag()) && (i == start - 1 || i == 0)) {
                    sb.append("```");
                } else if (name.equals(event.getAuthor().getAsTag())) {
                    sb.append("\n``````css\n");
                }

                sb.append(i + 1);
                sb.append(". ");
                sb.append(name);
                k = name.length();
                while (k < 35) {
                    sb.append(" ");
                    k++;
                }
                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                sb.append(messageActions.getLocalizedString("xp_ranking_level", "user", event.getAuthor().getId()));
                sb.append(numberFormat.format(level));
                k = String.valueOf(level).length();
                while (k < 10) {
                    sb.append(" ");
                    k++;
                }
                sb.append(messageActions.getLocalizedString("xp_ranking_xp", "user", event.getAuthor().getId()));
                sb.append(numberFormat.format(xp));
                k = String.valueOf(xp).length();
                while (k < 10) {
                    sb.append(" ");
                    k++;
                }
                sb.append(messageActions.getLocalizedString("xp_ranking_coins", "user", event.getAuthor().getId()));
                sb.append(numberFormat.format(coins));
                k = String.valueOf(coins).length();
                while (k < 10) {
                    sb.append(" ");
                    k++;
                }
                sb.append("\n");
                if (name.equals(event.getAuthor().getAsTag()) && !(i == start + 8)) {
                    sb.append("\n``````");
                }
                i++;
            }
            sb.append("```");

            event.getTextChannel().sendMessage(sb.toString()).queue();

        } else {
            messageActions.moduleIsDeactivated(event, "xp");
        }
    }

}

class ValueComparator<K, V extends Comparable<V>> implements Comparator<K> {

    private final HashMap<K, V> map = new HashMap<>();

    ValueComparator(HashMap<K, V> map) {
        this.map.putAll(map);
    }

    @Override
    public int compare(K s1, K s2) {
        return -map.get(s1).compareTo(map.get(s2));//descending order
    }
}
