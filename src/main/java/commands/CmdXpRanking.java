package commands;

import core.DatabaseHandler;
import core.MessageActions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

class CmdXpRanking {

    public static void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        int start;
        try {
            start = Integer.parseInt(args[1]);
        } catch (Exception e) {
            start = Integer.parseInt(Objects.requireNonNull(DatabaseHandler.database(event.getGuild().getId(), "select * from (select row_number() over (), id from (select id, xp, level, coins from users where ticket = 0 and not profile like 'bot' order by xp desc) as tmp) as temp where id = '" + event.getAuthor().getId() + "'"))[0]) - 5;
        }
        if (start <= 0)
            start = 1;
        int k;
        String name;
        String level;
        String xp;
        String coins;

        String[] answer = DatabaseHandler.database(event.getGuild().getId(), "select id, xp, level, coins from users where ticket = 0 and not profile like 'bot' order by xp desc offset " + (start - 1) + " rows fetch next 10 rows only");

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < 10; j++) {
            try {
                name = Objects.requireNonNull(event.getJDA().getUserById(answer[j * 4])).getAsTag();
            } catch (Exception e) {
                name = Objects.requireNonNull(answer)[j * 4];
            }
            xp = answer[j*4+1];
            level = answer[j*4+2];
            coins = answer[j*4+3];

            if (name.equals(event.getAuthor().getAsTag()) && j == 0) {
                sb.append("```css\n");
            } else if (!name.equals(event.getAuthor().getAsTag()) && j == 0) {
                sb.append("```");
            } else if (name.equals(event.getAuthor().getAsTag())) {
                sb.append("\n``````css\n");
            }

            sb.append(start + j);
            sb.append(". ");
            sb.append(name);
            k = name.length();
            while (k < 35) {
                sb.append(" ");
                k++;
            }
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
            sb.append(MessageActions.getLocalizedString("xp_ranking_level", "user", event.getAuthor().getId()));
            sb.append(numberFormat.format(Long.parseLong(level)));
            k = level.length();
            while (k < 10) {
                sb.append(" ");
                k++;
            }
            sb.append(MessageActions.getLocalizedString("xp_ranking_xp", "user", event.getAuthor().getId()));
            sb.append(numberFormat.format(Long.parseLong(xp)));
            k = xp.length();
            while (k < 10) {
                sb.append(" ");
                k++;
            }
            sb.append(MessageActions.getLocalizedString("xp_ranking_coins", "user", event.getAuthor().getId()));
            sb.append(numberFormat.format(Long.parseLong(coins)));
            k = coins.length();
            while (k < 10) {
                sb.append(" ");
                k++;
            }
            sb.append("\n");
            if (name.equals(event.getAuthor().getAsTag()) && !(j == 9)) {
                sb.append("\n``````");
            }

        }
        sb.append("```");

        event.getChannel().sendMessage(sb.toString()).queue();

    }

}
