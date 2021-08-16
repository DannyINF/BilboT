package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.derby.impl.sql.execute.CurrentDatetime;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.SHOPS;
import util.STATIC;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Random;

public class CmdShop {

    public static void shop(SlashCommandEvent event)
    {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else

        if (event.getMember().getId().equals("277746420281507841")) {
            //SHOPS.shop.first_page.postMenuPage(event);
        } else {
            event.getChannel().sendMessage("Oh no no no no no no no :smirk:").queue();
        }
        /*
        FakeDB fdb = new FakeDB(0,false, 0, false, false,"", "", "", "", "", "", "", "", "", "");
        if (args.length == 0)
        {
            //make sure the user is available (no reporting, no quiz etc)
            //change value isShopping to true in db
            STATIC.fakeDB.setShopstep(0);
            STATIC.fakeDB.setType(1);
            event.getAuthor().openPrivateChannel().queue(privateChannel ->
            {
                util.STATIC.fakeDB.setShopstep(0);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Shop");
                embedBuilder.setDescription("Willkommen im Shop des BilboTs. Hier kannst du dir verschiedene kosmetische Artikel für Coins kaufen.\n" +
                        "Wonach suchst du denn?\n" +
                        "\uD83C\uDFA7 Voiceintros\n" +
                        "\uD83D\uDC8C Willkommensbanner\n" +
                        "\uD83D\uDCBC Inventar");
                privateChannel.sendMessage(embedBuilder.build()).queue(msg -> {
                    msg.addReaction("\uD83C\uDFA7").queue();
                    msg.addReaction("\uD83D\uDC8C").queue();
                    msg.addReaction("\uD83D\uDCBC").queue();
                });
            });
        }
        else
        {
            switch (args[0].toLowerCase())
            {
                case "test":

                    String[] images = {
                            "Schatten",
                            fdb.equipped_background,
                            fdb.equipped_symbol,
                            fdb.equipped_text,
                            fdb.equipped_border
                    };

                    BufferedImage profile = null;
                    try
                    {
                        URL url = new URL(event.getAuthor().getAvatarUrl());
                        profile = ImageIO.read(url);
                    } catch (IOException e)
                    { }

                    ArrayList<BufferedImage> al = new ArrayList<>();
                    BufferedImage result = null;
                    int i;
                    int k = 1;
                    if (fdb.equipped_symbol.split("_")[0].equals("leg") || fdb.equipped_text.split("_")[0].equals("leg") || fdb.equipped_background.split("_")[0].equals("leg") || fdb.equipped_border.split("_")[0].equals("leg"))
                    {
                        k = 20;
                    }
                    for (i = 1; i<=k; i++)
                    {
                        result = new BufferedImage(
                                410, 160, //work these out
                                BufferedImage.TYPE_INT_RGB);
                        Graphics g = result.getGraphics();
                        g.drawImage(profile, 19, 19, 62, 62, null);
                        for(String image : images)
                        {
                            if (!image.split("_")[0].equals("none"))
                            {
                                String addition = "";
                                if (image.split("_")[0].equals("leg"))
                                {
                                    if (i<10)
                                    {
                                        addition = "0" + i;
                                    }
                                    else
                                    {
                                        addition = String.valueOf(i);
                                    }
                                    addition = "_" + addition;
                                }
                                System.out.println("Images/" + image + addition + ".png");
                                BufferedImage bi = ImageIO.read(new File("Images/" + image + addition + ".png"));
                                g.drawImage(bi, 0, 0, null);
                            }
                        }
                        al.add(result);
                    }
                    System.out.println(i);
                    if (i == 2)
                    {

                        ImageIO.write(result, "png", new File(event.getAuthor().getId() + ".png"));
                        event.getChannel().sendFile(new File(event.getAuthor().getId() + ".png")).queue();
                    }
                    else
                    {
                        writeAnimatedGif(new FileOutputStream(event.getAuthor().getId() + ".gif"), al, 100, 10000);
                        event.getChannel().sendFile(new File(event.getAuthor().getId() + ".gif")).queue();
                    }
                    break;
            }
        }*/

    }
    /*
    static void writeAnimatedGif(OutputStream stream,
                                 Iterable<BufferedImage> frames,
                                 int delayInMilliseconds,
                                 Integer repeatCount)
            throws IOException {
        try (ImageOutputStream iioStream =
                     ImageIO.createImageOutputStream(stream)) {

            ImageWriter writer =
                    ImageIO.getImageWritersByMIMEType("image/gif").next();
            writer.setOutput(iioStream);

            writer.prepareWriteSequence(null);

            for (BufferedImage frame : frames) {
                writeFrame(frame, delayInMilliseconds, writer, repeatCount);
                repeatCount = null;
            }

            writer.endWriteSequence();
            writer.dispose();
        }
    }

    static void writeFrame(BufferedImage image,
                           int delayInMilliseconds,
                           ImageWriter writer,
                           Integer repeatCount)
            throws IOException {
        ImageTypeSpecifier type =
                ImageTypeSpecifier.createFromRenderedImage(image);
        IIOMetadata metadata = writer.getDefaultImageMetadata(type, null);
        String format = metadata.getNativeMetadataFormatName();

        Node tree = metadata.getAsTree(format);

        if (repeatCount != null)
        {
            setRepeatCount(repeatCount, tree);
        }

        setDelayTime(delayInMilliseconds, tree);

        metadata.setFromTree(format, tree);

        writer.writeToSequence(new IIOImage(image, null, metadata), null);
    }

    private static void setRepeatCount(Number repeatCount, Node imageMetadata)
    {
        Element root = (Element) imageMetadata;

        ByteBuffer buf = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN);
        buf.put((byte) 1);  // sub-block index (always 1)
        byte[] appExtBytes = buf.putShort(repeatCount.shortValue()).array();

        Element appExtContainer;
        NodeList nodes = root.getElementsByTagName("ApplicationExtensions");
        if (nodes.getLength() > 0) {
            appExtContainer = (Element) nodes.item(0);
        } else {
            appExtContainer = new IIOMetadataNode("ApplicationExtensions");

            Node reference = null;
            nodes = root.getElementsByTagName("CommentExtensions");
            if (nodes.getLength() > 0) {
                reference = nodes.item(0);
            }

            root.insertBefore(appExtContainer, reference);
        }

        IIOMetadataNode appExt =
                new IIOMetadataNode("ApplicationExtension");
        appExt.setAttribute("applicationID", "NETSCAPE");
        appExt.setAttribute("authenticationCode", "2.0");
        appExt.setUserObject(appExtBytes);

        appExtContainer.appendChild(appExt);
    }


    private static void setDelayTime(int delayInMilliseconds, Node imageMetadata)
    {
        Element root = (Element) imageMetadata;

        Element gce;
        NodeList nodes = root.getElementsByTagName("GraphicControlExtension");
        if (nodes.getLength() > 0) {
            gce = (Element) nodes.item(0);
        } else {
            gce = new IIOMetadataNode("GraphicControlExtension");

            Node reference = null;
            nodes = root.getElementsByTagName("PlainTextExtension");
            if (nodes.getLength() > 0) {
                reference = nodes.item(0);
            }
            if (reference == null) {
                nodes = root.getElementsByTagName("ApplicationExtensions");
                if (nodes.getLength() > 0) {
                    reference = nodes.item(0);
                }
            }
            if (reference == null) {
                nodes = root.getElementsByTagName("CommentExtensions");
                if (nodes.getLength() > 0) {
                    reference = nodes.item(0);
                }
            }

            root.insertBefore(gce, reference);
        }

        gce.setAttribute("delayTime",
                String.valueOf(delayInMilliseconds / 10));
    }*/
}