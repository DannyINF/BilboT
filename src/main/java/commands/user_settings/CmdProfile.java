package commands.user_settings;

import commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Objects;

public class CmdProfile implements Command {
    private static BufferedImage colorImage(BufferedImage image, Color color) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                Color originalColor = new Color(image.getRGB(xx, yy), true);
                if (originalColor.getAlpha() > 0) {
                    image.setRGB(xx, yy, new Color(color.getRed(), color.getGreen(),
                            color.getBlue(), originalColor.getAlpha()).getRGB());
                }
            }
        }
        return image;
    }

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
            try {
            Color user = Color.decode("#ffcc2b");

            if (Objects.requireNonNull(event.getMember()).getRoles().contains(event.getGuild().getRolesByName("Adan", true).get(0))) {
                user = Color.decode("#ffcc2b");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Dunadan", true).get(0))) {
                user = Color.decode("#ffaf2a");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Moriquende", true).get(0))) {
                user = Color.decode("#ff9c26");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Calaquende", true).get(0))) {
                user = Color.decode("#ff721e");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Maia", true).get(0))) {
                user = Color.decode("#ff4b27");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Experte", true).get(0))) {
                user = Color.decode("#ff1c51");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Youtuber", true).get(0))) {
                user = Color.decode("#934ab6");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Vala", true).get(0))) {
                user = Color.decode("#08f0ed");
            }
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName("Il\u00favatar", true).get(0))) {
                user = Color.decode("#1ff306");
            }

            BufferedImage source = ImageIO.read(new File("Bilder/Profiles/example.jpg"));
            BufferedImage circle = colorImage(ImageIO.read(new File("Bilder/Profiles/circle.png")), user);
            URL url = new URL(event.getAuthor().getEffectiveAvatarUrl());
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            BufferedImage logo = ImageIO.read(connection.getInputStream());

            int width = logo.getWidth();
            int height = logo.getHeight();
            BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            g2.setClip(new Ellipse2D.Float(0, 0, width, width));
            g2.drawImage(logo, 0, 0, width, height, null);

            Graphics g = source.getGraphics();
            g.drawImage(circleBuffer, 45, 40, null);
            g.drawImage(circle, 0, 0, null);
            g.dispose();
            ImageIO.write(source, "JPEG", new File("Bilder/Profiles/" + event.getAuthor().getId() + ".jpg"));
            event.getChannel().sendFile(new File("Bilder/Profiles/" + event.getAuthor().getId() + ".jpg")).queue();
        } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
