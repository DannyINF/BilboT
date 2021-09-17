package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

//TODO complete  + update to database
public class CmdJackpot implements Command {

    private final Properties prop1 = new Properties();
    private final Properties prop2 = new Properties();
    private InputStream input1 = null;
    private InputStream input2 = null;

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (event.getChannel().getName().equals("jackpot")) {
            int count_lose = 0;
            if (args.length < 1) {
                count_lose++;
            } else {
                count_lose = Integer.parseInt(args[0]);
            }
            try {
                input1 = new FileInputStream("Properties/Jackpot/jackpot.properties");
                prop1.load(input1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int real_lose;
            try {
                real_lose = Integer.parseInt(prop1.getProperty("lose_" + event.getAuthor().getId() + "_" + event.getGuild().getId()));
            } catch (Exception e) {
                real_lose = 0;
            }

            try {
                input1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (real_lose >= count_lose) {
                int i = 0;
                while (i < count_lose) {
                    real_lose--;
                    jackpot(event, real_lose);
                    i++;
                }
            } else {
                Object obj = "Du besitzt nicht gen\u00fcgend Lose!";
                error(obj, event);
            }
        } else {
            Object obj = "Du musst dich im Channel " + event.getGuild()
                    .getTextChannelsByName("jackpot", true).get(0).getAsMention() + " befinden!";
            error(obj, event);
        }
    }


    private void jackpot(GuildMessageReceivedEvent event, int real_lose) {
        OutputStream output1;
        try {
            output1 = new FileOutputStream("Properties/Jackpot/jackpot.properties");
            prop1.setProperty("lose_" + event.getAuthor().getId(), String.valueOf(real_lose));
            prop1.store(output1, null);
            output1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String winstring = "1T";
        while (winstring.contains("T")) {
            winstring = winclass(Integer.parseInt(winstring.split("T")[0]));
        }
        int coins;
        int lose;
        boolean niete = false;
        StringBuilder gewinnstr = new StringBuilder();


        if (winstring.contains("N")) {
            gewinnstr.append("Niete");
        } else if (winstring.contains("C")) {
            coins = Integer.parseInt(winstring.split("C")[0]);
            gewinnstr.append(coins).append(" Coin/s");
            try {
                input2 = new FileInputStream("Properties/XP/xp.properties");
                prop2.load(input2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int current_coins;
            try {
                current_coins = Integer.parseInt(prop2.getProperty("coins_" + event.getAuthor().getId() + "_" + event.getGuild().getId()));
            } catch (Exception e) {
                current_coins = 0;
            }
            try {
                input2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int add_coins = current_coins + coins;
            try {
                OutputStream output2 = new FileOutputStream("Properties/XP/xp.properties");
                prop2.setProperty("coins_" + event.getAuthor().getId(), String.valueOf(add_coins));
                prop2.store(output2, null);
                output2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (winstring.contains("L")) {
            lose = Integer.parseInt(winstring.split("L")[0]);
            gewinnstr.append(lose).append(" Los/e");
            try {
                input1 = new FileInputStream("Properties/Jackpot/jackpot.properties");
                prop1.load(input1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int current_lose;
            try {
                current_lose = Integer.parseInt(prop1.getProperty("lose_" + event.getAuthor().getId() + "_" + event.getGuild().getId()));
            } catch (Exception e) {
                current_lose = 0;
            }

            try {
                input1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int addlose = current_lose + lose;
            try {
                output1 = new FileOutputStream("Properties/Jackpot/jackpot.properties");
                prop1.setProperty("lose_" + event.getAuthor().getId(), String.valueOf(addlose));
                prop1.store(output1, null);
                output1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String tier = winstring.split("---")[1];

        EmbedBuilder win = new EmbedBuilder();
        win.setColor(Color.GREEN);
        win.setTitle("Gewinn");
        win.setDescription(event.getAuthor().getAsMention() + " hat einen Tier-" + tier + "-Gewinn! \n" +
                gewinnstr);
        event.getChannel().sendMessageEmbeds(win.build()).queue();


    }

    private void error(Object description, GuildMessageReceivedEvent event) {
        EmbedBuilder error = new EmbedBuilder();
        error.setDescription(description.toString());
        error.setTitle("Error");
        error.setColor(Color.RED);
        event.getChannel().sendMessageEmbeds(error.build()).queue();
    }

    private String winclass(int winning_class) {
        StringBuilder gewinn = new StringBuilder();
        int winning_class_int = 0;
        switch (winning_class) {
            case 1:
                winning_class_int = 1;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append("1L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(1, 2)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 2:
                winning_class_int = 2;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append("2L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(2, 6)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 3:
                winning_class_int = 3;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append("3L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(6, 11)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 4:
                winning_class_int = 4;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append("4L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(11, 16)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 5:
                winning_class_int = 5;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append("5L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(16, 21)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 6:
                winning_class_int = 6;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(6, 8)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(20, 31)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 7:
                winning_class_int = 7;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(8, 10)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(31, 41)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 8:
                winning_class_int = 8;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(10, 12)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(41, 51)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 9:
                winning_class_int = 9;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(12, 14)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(51, 61)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 10:
                winning_class_int = 10;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        gewinn.append("N");
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(14, 16)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(61, 101)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 11:
                winning_class_int = 11;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(16, 21)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(101, 151)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 12:
                winning_class_int = 12;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(21, 26)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(151, 201)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 13:
                winning_class_int = 13;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(26, 31)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(201, 301)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 14:
                winning_class_int = 14;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(31, 36)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(301, 401)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 15:
                winning_class_int = 15;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(36, 51)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(401, 601)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 16:
                winning_class_int = 16;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(50, 61)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(601, 801)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 17:
                winning_class_int = 17;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(61, 71)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(801, 1001)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 18:
                winning_class_int = 18;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(71, 81)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(1001, 10001)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 19:
                winning_class_int = 19;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(81, 91)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(10001, 25001)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 20:
                winning_class_int = 20;
                switch (ThreadLocalRandom.current().nextInt(1, 5)) {
                    case 1:
                        break;
                    case 2:
                        gewinn.append(ThreadLocalRandom.current().nextInt(91, 150)).append("L");
                        break;
                    case 3:
                        gewinn.append(ThreadLocalRandom.current().nextInt(25001, 50001)).append("C");
                        break;
                    case 4:
                        int j = winning_class_int + 1;
                        gewinn.append(j).append("T");
                        break;
                }
                break;
            case 21:
                winning_class_int = 21;

                break;

        }
        gewinn.append("---").append(winning_class_int);
        return (gewinn.toString());
    }
}
