package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class CmdSetProfile implements Command {
    private final Properties prop1 = new Properties();

    @Override
    public boolean called() {
        return false;
    }

    // TODO: Update
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        String alter, name, herkunft, user, kurzbeschreibung;
        StringBuilder beschreibung = new StringBuilder();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.white);
        if (args.length < 4) {
            embed.setTitle("ERROR");
            embed.setDescription("Du hast zu wenig Argumente angegeben!\nSyntax: /setprofile  Alter(1 Wort)  Vorname(1 Wort)  Herkunft(1 Wort)  Kurzbeschreibung");
        } else {
            alter = args[0];
            name = args[1];
            herkunft = args[2];
            int i = 3;
            while (i < args.length) {
                beschreibung.append(args[i]);
                beschreibung.append(" ");
                i++;
            }
            kurzbeschreibung = beschreibung.toString();
            user = event.getAuthor().toString();
            StringBuilder sb = new StringBuilder();
            sb.append(alter);
            sb.append("###");
            sb.append(name);
            sb.append("###");
            sb.append(herkunft);
            sb.append("###");
            sb.append(user);
            sb.append("###");
            sb.append(kurzbeschreibung);
            sb.append("###");

            try {
                OutputStream output1 = new FileOutputStream("Properties/Profiles/profiles.properties");
                prop1.setProperty("profile_" + event.getAuthor().getAsMention(), sb.toString());
                prop1.store(output1, null);
                output1.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            embed.setTitle("Profil erstellt!");
            embed.setTitle("Es wurde ein Profil f\u00FCr " + event.getAuthor().getName() + " erstellt!");
        }
        event.getChannel().sendMessage(embed.build()).queue();

    }


}
