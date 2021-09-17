package commands;

import core.MessageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Properties;

//TODO: Update and implement database-usage
public class CmdNewspaper implements Command {

    private final Properties prop2 = new Properties();
    // --Commented out by Inspection (25.06.2020 13:43):Properties prop1 = new Properties();

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(MessageActions.getLocalizedString("newspaper_title", "user", event.getAuthor().getId()));
        if ("buy".equals(args[0])) {
            InputStream input2 = new FileInputStream("Properties/XP/xp.properties");
            prop2.load(input2);
            int coins = Integer.parseInt(prop2.getProperty("coins_" + event.getAuthor()));
            int xp = Integer.parseInt(prop2.getProperty("xp_" + event.getAuthor()));
            int coins_jakob = Integer.parseInt(prop2.getProperty("coins_" + Objects.requireNonNull(event.getGuild().getMemberById("471366682263289886")).getUser()));
            int coins_benni = Integer.parseInt(prop2.getProperty("coins_" + Objects.requireNonNull(event.getGuild().getMemberById("354354147614654465")).getUser()));
            input2.close();
            int prize;
            if (xp < 1215) {
                prize = 10;
            } else if (xp < 5415) {
                prize = 20;
            } else if (xp < 12615) {
                prize = 40;
            } else if (xp < 22815) {
                prize = 80;
            } else {
                prize = 100;
            }
            embed.setDescription(MessageActions.getLocalizedString("newspaper_help", "user", event.getAuthor().getId()));
            double premprize = prize * 1.5;
            if (coins >= prize) {
                OutputStream output;
                switch (args[1]) {
                    case "normal":
                        embed.setColor(Color.GREEN);
                        embed.setDescription(MessageActions.getLocalizedString("newspaper_normal_success", "server", event.getGuild().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()));
                        coins -= prize;
                        coins_jakob += prize / 2;
                        coins_benni += prize / 2;
                        try {
                            output = new FileOutputStream("Properties/XP/xp.properties");
                            prop2.setProperty("coins_" + event.getAuthor(), String.valueOf(coins));
                            prop2.setProperty("coins_" + Objects.requireNonNull(event.getGuild().getMemberById("471366682263289886")).getUser(), String.valueOf(coins_jakob));
                            prop2.setProperty("coins_" + Objects.requireNonNull(event.getGuild().getMemberById("354354147614654465")).getUser(), String.valueOf(coins_benni));
                            prop2.store(output, null);
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        EmbedBuilder transaktion = new EmbedBuilder();
                        transaktion.setColor(Color.GREEN);
                        transaktion.setTitle(MessageActions.getLocalizedString("newspaper_transaction", "user", event.getAuthor().getId()));
                        transaktion.setDescription(MessageActions.getLocalizedString("newspaper_normal_internal", "server", event.getGuild().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()).replace("[COINS]", String.valueOf(prize / 2)));
                        Objects.requireNonNull(event.getGuild().getMemberById("471366682263289886")).getUser().openPrivateChannel().queue((channel) -> channel.sendMessageEmbeds(transaktion.build()).queue());
                        Objects.requireNonNull(event.getGuild().getMemberById("354354147614654465")).getUser().openPrivateChannel().queue((channel) -> channel.sendMessageEmbeds(transaktion.build()).queue());
                        transaktion.setDescription(MessageActions.getLocalizedString("newspaper_normal_buyer", "user", event.getAuthor().getId()));
                        event.getAuthor().openPrivateChannel().queue((channel) -> {
                            channel.sendMessageEmbeds(transaktion.build()).queue();
                            channel.sendFile(new File("Properties/Newspaper/Normal.pdf")).queue();
                        });
                        break;
                    case "premium":
                        embed.setColor(Color.GREEN);
                        embed.setDescription(MessageActions.getLocalizedString("newspaper_premium_success", "server", event.getGuild().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()));
                        coins -= premprize;
                        coins_jakob += premprize / 2;
                        coins_benni += premprize / 2;
                        try {
                            output = new FileOutputStream("Properties/XP/xp.properties");
                            prop2.setProperty("coins_" + event.getAuthor(), String.valueOf(coins));
                            prop2.setProperty("coins_" + Objects.requireNonNull(event.getGuild().getMemberById("471366682263289886")).getUser(), String.valueOf(coins_jakob));
                            prop2.setProperty("coins_" + Objects.requireNonNull(event.getGuild().getMemberById("354354147614654465")).getUser(), String.valueOf(coins_benni));
                            prop2.store(output, null);
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        transaktion = new EmbedBuilder();
                        transaktion.setColor(Color.GREEN);
                        transaktion.setTitle(MessageActions.getLocalizedString("newspaper_transaction", "user", event.getAuthor().getId()));
                        transaktion.setDescription(MessageActions.getLocalizedString("newspaper_premium_internal", "server", event.getGuild().getId())
                                .replace("[USER]", event.getAuthor().getAsMention()).replace("[COINS]", String.valueOf(prize / 2)));
                        Objects.requireNonNull(event.getGuild().getMemberById("471366682263289886")).getUser().openPrivateChannel().queue((channel) -> channel.sendMessageEmbeds(transaktion.build()).queue());
                        Objects.requireNonNull(event.getGuild().getMemberById("354354147614654465")).getUser().openPrivateChannel().queue((channel) -> channel.sendMessageEmbeds(transaktion.build()).queue());
                        Thread.sleep(10);
                        transaktion.setDescription(MessageActions.getLocalizedString("newspaper_premium_buyer", "user", event.getAuthor().getId()));
                        event.getAuthor().openPrivateChannel().queue((channel) -> {
                            channel.sendMessageEmbeds(transaktion.build()).queue();
                            channel.sendFile(new File("Properties/Newspaper/Premium.pdf")).queue();
                        });
                        break;
                    default:
                        embed.setColor(Color.RED);
                        embed.setDescription(MessageActions.getLocalizedString("newspaper_error_format", "user", event.getGuild().getId()));
                        break;
                }
            }
        }
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }


}
