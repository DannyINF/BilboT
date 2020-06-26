package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.InputStream;
import java.util.Properties;

//TODO: Update -> convert to extra class + implement for all-server-support
public class cmdTransfer implements Command {
    // --Commented out by Inspection (25.06.2020 13:43):private final Properties prop2 = new Properties();
    // --Commented out by Inspection (25.06.2020 13:43):private InputStream input2 = null;

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        /*Member member = event.getMember();
        try {
            input2 = new FileInputStream("Properties/XP/xp.properties");
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }
        try {
            prop2.load(input2);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        String xp;
        String level;
        String coins;
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        xp = prop2.getProperty("xp_" + member.getUser().getId() + "_" + event.getGuild().getId());
        level = prop2.getProperty("level_" + member.getUser().getId() + "_" + event.getGuild().getId());
        coins = prop2.getProperty("coins_" + member.getUser().getId() + "_" + event.getGuild().getId());

        try {
            input2.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        String id = event.getGuild().getId();

        String[] arguments = {"users", "id", "'" + event.getAuthor().getId() + "'", "xp", xp, "level", level, "coins", coins, "ticket", "0", "intro",
                "''", "profile", "''", "words", "0", "msg", "0", "chars", "0", "voicetime", "0", "report", "0", "moderations", "0",
                "loginstreak", "0", "nextlogin", "0", "verifystatus", "TRUE", "activity", "0", "banlog", "''"};
        String[] selecargs = {"users", "id = '" + event.getMember().getUser().getId() + "'", "1", "id"};

        if (core.databaseHandler.database(id, "select", selecargs)[0]!=null) {
            System.out.println("update");
            core.databaseHandler.database(id, "update", arguments);
        } else {
            System.out.println("insert");
            core.databaseHandler.database(id, "insert", arguments); // 11
        }*/

    }


}
