package special;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class xpGewinne extends ListenerAdapter {
    // --Commented out by Inspection (13.12.2018 22:15):private Properties prop = new Properties();
    // --Commented out by Inspection (13.12.2018 22:15):private InputStream input = null;

    // --Commented out by Inspection (13.12.2018 22:15):int i = 0;

    public void onMessageReceived(MessageReceivedEvent event) {
        /*
        String status = null;
        try {
            status = modulesChecker.moduleStatus("xp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (status.equals("activated")) {

            try {
                input = new FileInputStream("Properties/XP/xp.properties");
                prop.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                i = Integer.parseInt(prop.getProperty("jackpot"));
            } catch (Exception e) {
                i = 0;
            }


            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            i++;
            OutputStream output = null;
            if (i >= 3000) {
                if (!event.getTextChannel().getName().toLowerCase().contains("chat") && !event.getTextChannel().getName().toLowerCase().contains("diskussion")) {
                    i++;
                } else {
                    i=0;
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(java.awt.Color.YELLOW);
                    embed.setTitle("JACKPOT!!!");
                    int win_int = ThreadLocalRandom.current().nextInt(0, event.getGuild().getMembers().size());
                    int gewinn = winInt();
                    User winner = event.getGuild().getMembers().get(win_int).getUser();
                    NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                    embed.setDescription(winner.getAsMention() + " hat " + numberFormat.format(gewinn) + " "
                            + event.getGuild().getEmotesByName("Coin", true).get(0).getAsMention() + " gewonnen! Gratulation!");
                    try {
                        input = new FileInputStream("Properties/XP/xp.properties");
                        prop.load(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int coins = 0;
                    try {
                        coins = Integer.parseInt(prop.getProperty("coins_" + winner));
                    } catch (Exception e) {
                        coins = 0;
                    }


                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int newCoins = coins + gewinn;

                    try {
                        output = new FileOutputStream("Properties/XP/xp.properties");
                        prop.setProperty("coins_" + winner, String.valueOf(newCoins));
                        prop.store(output, null);
                        output.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    event.getTextChannel().sendMessage(embed.build()).queue();
                }
            }
            try {
                output = new FileOutputStream("Properties/XP/xp.properties");
                prop.setProperty("jackpot", String.valueOf(i));
                prop.store(output, null);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("DEACTIVATED!");
        }

    }
    public static Integer winInt() {
        int winint = 0;
        int i = 0;
        int j = 10;
        int k = 100;
        boolean repeat = true;
        while (repeat) {
            i = ThreadLocalRandom.current().nextInt(0, 2);
            switch (i) {
                case 0:
                    winint = ThreadLocalRandom.current().nextInt(j, k);
                    repeat = false;
                    break;
                case 1:
                    repeat = true;
                    switch (j) {
                        case 10:
                            j=100;
                            k=500;
                            break;
                        case 100:
                            j=500;
                            k=1000;
                            break;
                        case 9500:
                            winint = 10000;
                            repeat = false;
                        default:
                            j=k;
                            k+=500;
                    }
                    break;
            }
        }

        return winint;*/
    }

}
