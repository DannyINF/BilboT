package commands;

import core.DatabaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class CmdSearch {

    public static void search(SlashCommandEvent event, String str_site, String query) throws IOException, SQLException {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else


        EmbedBuilder embed = new EmbedBuilder();
            StringBuilder sb = new StringBuilder();
            String clean = null;
            String site = null;
            String url = null;
            String siteurl = null;
            String[] answer;
            answer = DatabaseHandler.database("usersettings", "select language from users where id = '" + event.getUser().getId() + "'");
            assert answer != null;
            String country = answer[0];
            boolean sendMsg = false;
            boolean link = false;
            String describer = null;

            switch (str_site.toLowerCase()) {
                case "ardapedia":
                    sendMsg = true;
                    int j = 0;
                    site = "Ardapedia";
                    // building the question-string

                    // sending "search" msg

                    Message msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.LIGHT_GRAY).setTitle("Ardapedia").setDescription("Suche nach " + query + " . . .").build()).complete();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 4000);

                    Document doc;
                    siteurl = "http://ardapedia.herr-der-ringe-film.de/index.php/" + query;
                    // connecting to ardapedia
                    doc = Jsoup.connect(siteurl).get();

                    Whitelist wl = Whitelist.simpleText();
                    wl.addTags("b", "a", "p", "i"); // add additional tags here as necessary

                    /*
                    Because there are 3 different common ways of structuring the first chapter, it's sometimes a bit difficult to
                    get the right message out of the html
                     */
                    try {
                        clean = Jsoup.clean(doc.outerHtml(), wl).replaceAll("<a></a>", "#####").split("#####")[1]
                                .replace("</b>", "**").replace("<b>", "**").replace("<a>", "")
                                .replace("</a>", "").replace("<i>", "*").replace("</i>", "*")
                                .replace("\n", "").replace("&nbsp;", " ").replace("    ", "#####").split("#####")[2];
                    } catch (Exception e) {
                        clean = Jsoup.clean(doc.outerHtml(), wl).replaceAll("<p>", "#####")
                                .replace("</p>", "#####").split("#####")[11].replace("</b>", "**")
                                .replace("<b>", "**").replace("<a>", "")
                                .replace("</a>", "").replace("<i>", "*").replace("</i>", "*")
                                .replace("\n", "").replace("&nbsp;", " ");
                    }
                    if (clean.isEmpty()) {
                        clean = Jsoup.clean(doc.outerHtml(), wl).replaceAll("<p>", "#####")
                                .replace("</p>", "#####").split("#####")[11].replace("</b>", "**")
                                .replace("<b>", "**").replace("<a>", "")
                                .replace("</a>", "").replace("<i>", "*").replace("</i>", "*")
                                .replace("\n", "");
                    }
                    clean = clean.replace("<p>", "").replace("</p>", "").replace("&nbsp;", " ");
                    break;
                case "wikipedia":
                    sendMsg = true;
                    site = "Wikipedia";
                    // building the question-string


                    // sending "search" msg

                    msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.LIGHT_GRAY).setTitle("Wikipedia").setDescription("Suche nach " + query + " . . .").build()).complete();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 4000);
                    siteurl = "https://" + country + ".wikipedia.org/wiki/" + query;
                    // connecting to wikipedia
                    doc = Jsoup.connect(siteurl).get();

                    wl = Whitelist.simpleText();
                    wl.addTags("b", "a", "p", "i"); // add additional tags here as necessary

                    try {
                        url = "https:" + Jsoup.clean(doc.html().split("class=\"image\"")[1].split("src=\"")[1].split("\"")[0], wl);
                    } catch (Exception ignored) {

                    }

                    clean = Jsoup.clean((doc.html().split("<p>")[1] + "<p>").split("<div id=\"toc\"")[0], wl)
                            .replace("</b>", "**").replace("<b>", "**").replace("<a>", "")
                            .replace("</a>", "").replace("<i>", "*").replace("</i>", "*")
                            .replace("\n", "");
                    clean = clean.replace("<p>", "").replace("</p>", "").replace("&nbsp;", " ");
                    break;
                case "lotrfandom":
                    sendMsg = true;
                    site = "lotr.fandom.com";
                    // building the question-string

                    // sending "search" msg

                    msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.LIGHT_GRAY).setTitle("lotr.fandom.com").setDescription("Suche nach " + query + " . . .").build()).complete();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 4000);
                    siteurl = "https://lotr.fandom.com/wiki/" + query;
                    // connecting to wikipedia
                    doc = Jsoup.connect(siteurl).get();

                    wl = Whitelist.simpleText();
                    wl.addTags("b", "a", "p", "i"); // add additional tags here as necessary

                    try {
                        url = "https:" + Jsoup.clean(doc.html().split("class=\"image\"")[1].split("src=\"")[1].split("\"")[0], wl);
                    } catch (Exception ignored) {

                    }

                    clean = Jsoup.clean((doc.html().split("<p>")[2] + "<p>").split("</p>")[0] + "</p>", wl)
                            .replace("</b>", "**").replace("<b>", "**").replace("<a>", "")
                            .replace("</a>", "").replace("<i>", "*").replace("</i>", "*")
                            .replace("\n", "");
                    clean = clean.replace("<p>", "").replace("</p>", "").replace("&nbsp;", " ");
                    break;
                case "youtube":
                    link = true;
                    sendMsg = true;
                    site = "YouTube";
                    // building the question-string

                    // sending "search" msg

                    msg = event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.LIGHT_GRAY).setTitle("YouTube").setDescription("Suche nach " + query + " . . .").build()).complete();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 4000);
                    siteurl = "https://www.youtube.com/results?search_query=" + query;
                    // connecting to youtube
                    doc = Jsoup.connect(siteurl).get();
                    wl = Whitelist.simpleText();
                    wl.addTags("b", "a", "p", "i"); // add additional tags here as necessary
                    Document doc2 = Jsoup.connect(siteurl).get();
                    describer = Jsoup.clean((doc2.html().split("<title>")[1].split(" - YouTube</title>")[0]), wl);

                    clean = Jsoup.clean((doc.html().replace("\"", "").split("href=/watch?v=")[1].split("class")[0]), wl);
                    url = "https://i.ytimg.com/vi/" + clean + "/maxresdefault.jpg";
                    clean = "https://www.youtube.com/watch?v=" + clean;
                    //TODO: finish this
                    break;
            }

            for (int i = 0; i < 100; i++) {
                assert clean != null;
                clean = clean.replace("[" + i + "]", " ");
            }
        siteurl = siteurl.replace(" ", "%20");

        // sending msg
        embed.setThumbnail(url);
        embed.setColor(Color.lightGray);
        if (link) {
            embed.setDescription("[" + describer + "](" + clean + ")");
        } else {
            embed.setDescription(clean);
        }
        embed.setDescription(clean);
        embed.setTitle(site + ": " + sb.toString());
        embed.addField("Source:", "[" + siteurl + "](" + siteurl + ")", true);
        embed.setTimestamp(OffsetDateTime.now());
        event.getTextChannel().sendMessageEmbeds(embed.build()).queue();
    }
}