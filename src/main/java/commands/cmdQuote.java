package commands;

import core.messageActions;
import core.modulesChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class cmdQuote implements Command {
    @Override
    public boolean called() {
        return false;
    }

    //TODO: Update + database
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        String status;
        status = modulesChecker.moduleStatus("quotes", event.getGuild().getId());
        if (status.equals("activated")) {
            String autor = null;
            try {
                autor = args[0];
            } catch (Exception ignored) {
            }
            String msg;
            String title;
            int random;
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.YELLOW);
            try {
                if (autor.isEmpty() || autor.equals(" ")) {
                    title = messageActions.getLocalizedString("quote_error_title", "user", event.getAuthor().getId());
                    msg = messageActions.getLocalizedString("qoute_error_usage", "user", event.getAuthor().getId());
                } else if (autor.equals("hdrf")) {
                    String[] zitate_hdrf = {"Denn es stellte sich jemand und zwar Erandil, der auf einem fliegendem Schiff stand und hat sich gedacht, jetzt wirds Zeit, jetzt wird Zeit, dass ich diesen Drachen endlich vernichte.",
                            "Die Einwohner w\u00FCrden die Zerst\u00F6rung des Balrogs zuerst gar nicht bemerken",
                            "Hallo und herzlich Willkommen und zwar geht es heute darum...",
                            "Das Schattenheer von Dunharg. Das ist diese Geisterarmee die wir aus den Herr der Ringe Filmen kennen. Der Herr der Ringe Film kennt ja wei\u00DF Gott jeder.",
                            "Das waren immer so fragen, die in den Kommentaren entstanden sind.",
                            "Doch kommen wir erst mal ein bisschen genauer in die Geschichte.",
                            "Das ist auch so ein Ort wo viele Abstand halten, weil das eigentlich ein gef\u00E4hrlicher Pfad ist. Denn wer geht bittesch\u00F6n freiwillig zu Geistern hin und dr\u00FCckt denen mal schnell die Hand und sagt Gr\u00FC\u00DF Gott, wie gehts euch eigentlich? Ne",
                            "Nur der Erbe Isildur darf diese Pfade eigentlich betreten und Aragorn war auch so der einzigste, der sich \u00FCberhaupt da rein getraut hat.",
                            "Wir wollen endlich Ruhe und Frieden einfach nur ham.",
                            "Der erste Auftrag war einfach nur und zwar die Korsaren aus Umbar haben daf\u00FCr gesorgt, dass die gro\u00DFe Gondor Stadt Peagir also die besondere Hafenstadt die wurden n\u00E4hmlich angegriffen und komplett vernichtet",
                            "Denn die Geister \u00FCberrannten die ganze Stadt ohne Ende.",
                            "Und ab da hat Aragorn gesagt: Alles ist gut Jungs ihr k\u00F6nnt jetzt gehen hier habt ihr euern Eid erf\u00FCllt",
                            "Abonnieren sonst stirbt Isildur", "Der Herr der Ringe ist ein ziemlich erfolgreiches Buch und auch ein erfolgreicher Film denn es wurde schlie\u00DFlich auch verfilmt.",
                            "Man merkt in der Herr der Ringe, dass es dort eine Zielperson gibt, die vernichtet werden muss.",
                            "Sauron ist ziemlich wichtig. Das wissen wir.",
                            "Sauron hat ne ziemlich gro\u00DFe Biographie.",
                            "Keiner will ja sagen: Oh Gott da kommt Gorthauer der grausame. Das sagt keiner. Aber Sauron sch\u00F6n kompakt und kann man ganz leicht sagen. Finde ich auch gut",
                            "Sauron ist ja als Spezies her da denkt man sich was ist der wohl f\u00FCr eine Spezies.",
                            "Und \u00FCber die Maia k\u00F6nnen wir in einem sp\u00E4teren Video reden das nicht die Maya die hier \u00E4h irgendwo Mexiko ihre Pyramiden bauen um Gottes Willen nein da kommen wir nochmal in ein extra Video wieder was ein Maia eigentlich dann ist.",
                            "Ich mach auch mal ein Video was eigentlich Aule ist.",
                            "Geschlecht ist er [Sauron] m\u00E4nnlich. Das h\u00E4tte sich eigentlich jeder denken k\u00F6nnen. Er ist kein Zwitter und auch nicht weiblich.",
                            "Geburt ist Zeit vor den Tagen.",
                            "Tod ist nat\u00FCrlich 3019 also drittes Zeitalter und zwar durch Frodo weil er den Ring in den Schicksalsberg hinein feuert.",
                            "Das merkt man in der Hobbit, da rennt er als Nekromant also als Schatten rum und macht viel Angst.",
                            "Als Beruf war er [Sauron] Feldheer Melkors. Hmm Feldheer Melkors als erster Beruf. Was ist wohl Melkor? Melkor ist sein oberster Boss weil Sauron ist eigentlich nur ein Diener tats\u00E4chlich Sauron ist nicht hier der h\u00F6chste dunkle Herrscher aller Welten- Nein er ist nur ein Feldheer Melkors gewesen und ist auch immer noch der Diener Melkor.",
                            "Sauron ist auch nur ein Diener, der ist nicht hier oberstes Gebot, nein der ist einfach nur ein Diener.",
                            "Sauron hei\u00DFt ja \u00FCbersetzt Gorthauer der grausame.",
                            "Morgoth war ein Valar, denn Morgoth ist Melkor tut mir leid war da ein bisschen durcheinander.",
                            "Morgoth war ein ziemlich hohes Tier.",
                            "Vala ist ein ziemlich hoher Gott.",
                            "Aber Morgoth war dann sp\u00E4ter auch auf Melkors Seite.",
                            "Arda ist kein Mittelerde.",
                            "Arda ist kein Mittelerde, das ist eine ganz andere Welt.",
                            "Es gibt viel viel st\u00E4rkere Wesen, die Sauron das Wasser reichen k\u00F6nnen.",
                            "Die Menschen wurden alle korrumpiert, waren zu schwach und wurden alle zu den Natzguls.",
                            "...und dem daraus hervorgegangenen Ringkrieg  war der Untergang Saurons, durch die Zerst\u00F6rung des einen Ring, besiegt.",
                            "\u00FCber die Biographie greifen wir gleich richtig doll ein aber ich werde dieses Video in verschiedene Parts einbetonieren weil sonst ich hab nicht das beste Internet das geht gar nicht sonst lade ich mich hier dumm und d\u00E4mlich beim hochladen. Ringkrieg was ist das?",
                            "...und dann entstehen doch immer diese Kriege von Sauron nat\u00FCrlich befohlen und zwar Minas Tirith Helms Klamm oder was wei\u00DF ich noch wo.",
                            "Numenor war eine Insel und die Menschen hie\u00DFen auch Numenor Menschen sozusagen.",
                            "Und diese Numenor Insel war mal wie gesagt eine Insel die auch mal untergegangen sind.",
                            "Weil wenn die Insel untergeht das ist schlecht da geht man unter und ertrinkt man ist nicht so gut!",
                            "So das hatten wir erstmal grob hier den ganzen Zusammenfassung gemacht die Biographie wird ziemlich ziemlich gro\u00DF.", "Von der Spezies her war er erst ein Ainur, dann ein Maia und dann ein Istari.",
                            "Bevor \u00FCberhaupt diese Zeitrechnung kam mit ersten Zeitalter, drittes Zeitalter, zweites Zeitalter.",
                            "Tod ist nat\u00FCrlich nicht gewesen, denn er segelt doch dann in den Westen bei der Herr der Ringe im Film und da geht er nat\u00FCrlich zur\u00FCck nach Vallenor.",
                            "Er wurde vom Eru Iluvata geschaffen und wurde aus sogar einer der weisesten von ihnen.",
                            "Wenn Sauron den Ring wieder hat sind auch alle Valars in Gefahr.",
                            "Saruman war die vollkommene Macht.",
                            "Gandalf ist daf\u00FCr verantwortlich nat\u00FCrlich, dass Thorin und die 13 Zwerge \u00FCberhaupt losbrechen.",
                            "Daf\u00FCr [Erebor zur\u00FCckerobern] ist ja Gandalf zust\u00E4ndig, weil der hat ja gesagt zu Dúrin los das schaffen wir.",
                            "Das war ja erstmal das erste Ding.",
                            "Bei Herr der Ringe kam n\u00E4mlich der n\u00E4chste Auftrag und zwar war er nat\u00FCrlich vong Feuerwerk und bla und er dann hatte ja der Bilbo Geburtstag und ist ja schon \u00E4lter als die Steinkohle geworden.",
                            "Und zwar Moria stirbt leider, Gandalf der Graue",
                            "Bis er [Gandalf] stirbt und der Balrog stirbt und halt Gandalf zusammen.",
                            "Somit war das jetzt ne ganz sch\u00F6n schnelle Geschichte sag ich mal so.",
                            "Und so sieht man auch mal so ein bisschen die Geschichte das Gandalf \u00FCberhaupt kein Mensch ist ne, denn man immer ja gut er sieht aus wie ein Mensch, ne aber er ist kein Mensch das hat man sofort gemerkt wenn sich so ein bisschen damit besch\u00E4ftigt so ein bisschen mit die B\u00FCcher liest und sowas da kommt man schon ein bissl dann merkt dann merkt man sich das ein bisschen besser alles und das hei\u00DFt schon was es gibt nat\u00FCrlich auch ja wenn man ein bisschen Film alles ein bisschen aufpasst man muss ja wundern warum gibt es nur 5 Zauberer.", "Ich kann mir nicht vorstellen, dass der Mann [Saruman] so einfach mal, obwohl er so viel Macht hat, dass er einfach mal so wie nichts einfach verschwindet und gar nicht mehr existiert, das kann ich mir \u00FCberhaupt nicht vorstellen, da der Mann erst astrein ist also richtig klasse.",
                            "Nach seinem Tod muss er n\u00E4mlich in den Westen in Mendos Hallen.",
                            "Wenn man nachdem Buch geht wird es ja auch beschrieben dass ein dunkler Nebel sich aufbaute und eine nebelige Gestalt sich aufbaute die versuchte in den Westen zu reisen.",
                            "Saruman ist einfach nur eine ausgetrocknete Oase die existiert.",
                            "Heute kommt nat\u00FCrlich eine kleine Zusammenfassung zu Saruman, denn dieser Mann ist spitze. Ich find den genial, In meinen Augen steht er sogar \u00FCber Gandalf. Ich find ihn einfach nur richtig richtig richtig geil. Der Mann hat Klasse!",
                            "Vor allem dingen im Film wie der Schauspieler wird so. Einwand frei! Da kann ich nur sagen super super super,richtig geil.",
                            "Im Film ist sein Tod [Saruman] ein bisschen \u00FCbertriebener. Gut im Buch ist auch ein bisschen \u00FCbertriebener.",
                            "Saruman besitzt n\u00E4mlich gewisse Macht die im Film nie gezeigt wird, aber er besitzt Macht, das nat\u00FCrlich sehr sehr viel.",
                            "Er wurde im dritten Zeitalter von den Valars nach Mittelerde geschickt.",
                            "Saruman besitzt eine Kunst.",
                            "Und hat dadurch durch diese Schmiedekunst hat er dann eingesetzt und dann hat er sein eigenen Ring geschmiedet, den er daf\u00FCr nutzte um seine Macht zu bereichern und sich dann auch noch als vielfarbiger zu nennen.",
                            "Ich muss aber auch sagen dagegen Saruman ist nicht dumm ne.",
                            "Der Hexenk\u00F6nig ist der Erbe von Sauron.",
                            "Vallenor",
                            "Ja das [Tom Bombadil] ist nat\u00FCrlich ein ganz mystische Person in Herr der Ringe und keine Sau wei\u00DF eigentlich, was es mit diesem Vogel auf sich hat, sieht halt aus als ob er auf Drogen w\u00E4r oder als ob er andere Sachen zu sich genommen h\u00E4tte.",
                            "Die [Goldbeere] ist jetzt nicht so mystisch w\u00FCrde ich sozusagen sagen aber doch sie hat halt eine Sch\u00F6nheit der Elben.",
                            "Bom Tom Bombadil",
                            "Wenn man bei Youtube eingibt Lied Tom Bombadil kriegt man verschiedene Lieder die einem vorgesungen werden und die sind schon irgendwie ganz sch\u00F6n eigeneartig ne.",
                            "Ein ziemlich bekannter  Person isser doch.",
                            "Der Hexenk\u00F6nig war erstens damals ein Mensch wahrscheinlich ein Numenor also da geh\u00F6rt schon was dazu denn, aber ob man sich nat\u00FCrlich zu 100 prozentig sicher ist ob er Numenor war, das steht nat\u00FCrlich in Sternen.",
                            "Kam\u00FCl",
                            "Das einzigartige dabei ist ja die 9 Natzgule er ist ja der Oberste und hat sich hochgearbeitet ohne Ende.",
                            "Deswegen hat der Hexenk\u00F6nig da nat\u00FCrlich ein super Pluspunkt von mir, denn das... das hei\u00DFt schon was also zeigt das doch relativ gute Macht.",
                            "Im Film ist es echt schade das dann so erb\u00E4rmlich stirbt und im Buch isses stirbt er nat\u00FCrlich auch ist logisch.",
                            "Der Hexenk\u00F6nig von Angmar ist der nach Sauron der Gef\u00FCrchteste in Mittelerde.",
                            "Das Teil Rudauer",
                            "Merry sticht ja mit dem also Merry sticht ja mit dem Dolch mit dem Noldor Dolch den er von Galadriel geschenkt bekommen hat den in die Ferse sozusagen und das ist nat\u00FCrlich ne Schw\u00E4che, denn der Noldor Dolch ist extra gefertigt um Schattenwesen zu verbannen und zu t\u00F6ten und deswegen gelingt es auch Eowyn sozusagen den Hexenk\u00F6nig umzubringen."
                    };
                    random = ThreadLocalRandom.current().nextInt(0, zitate_hdrf.length);
                    msg = zitate_hdrf[random];
                    title = "Herr der Ringe Fan /Tolkien Fan";
                    title = messageActions.getLocalizedString("quote_msg", "user", event.getAuthor().getId()).replace("[USER]", title);
                } else if (autor.equals("celi")) {
                    String[] zitate_celi = {
                            "Flitzpiepe!!",
                            "Du bist gemein.",
                            "\u00c4fft Ihr mich jetzt alle nach oder was?",
                            "Hihi",
                            "Das ist nicht Witzig! H\u00f6rt auf zu lachen!",
                            "Muss ich nicht!",
                            "Selber!",
                            "Ich sag doch nur selber du ... ach vergiss es",
                            "Vergiss es einfach",
                            "\u00e4\u00e4\u00e4h leute ich glaub wir haben gleich 'n ... \u00e4\u00e4h doch nicht",
                            "\u00e4\u00e4h ... leute, ich h\u00f6r euch nicht mehr.",
                            "\u00e4\u00e4h leute, das ist nicht witzig.",
                            "keine Ahnung.",
                            "Ich find das nicht soo witzig",
                            "Warum redet keiner mehr?",
                            "Au\u00dfer du",
                            "Was?",
                            "ja, genau das!",
                            "SELBER!",
                            "Ja, aber eben grade hab ich euch nicht geh\u00f6rt",
                            "Weil ihr so ruhig wart",
                            "Ah?",
                            "OOh :frowning:",
                            "Woher kenn ich das Lied?",
                            "Oooh nein :frowning:",
                            "Damit geht mir meine Freundin schon auf'n Sack",
                            "Mir ist langweilig",
                            "Erstmal stummschalten",
                            "Nein",
                            "Meine Freundin geht mir damit auf den ... *Left the Voicechat*",
                            "Neeein!  Das ist B\u00f6se, ihr Flitzpiepen!"
                    };
                    random = ThreadLocalRandom.current().nextInt(0, zitate_celi.length);
                    msg = zitate_celi[random];
                    title = "Mittelerdesuchti";
                    title = messageActions.getLocalizedString("quote_msg", "user", event.getAuthor().getId()).replace("[USER]", title);


                } else {

                    title = messageActions.getLocalizedString("quote_error_title", "user", event.getAuthor().getId());
                    msg = messageActions.getLocalizedString("quote_error_usage", "user", event.getAuthor().getId());

                }
            } catch (NullPointerException e) {

                title = messageActions.getLocalizedString("quote_error_title", "user", event.getAuthor().getId());
                msg = messageActions.getLocalizedString("qoute_error_usage", "user", event.getAuthor().getId());

            }
            embed.setTitle(title);
            embed.setDescription(msg);
            messageActions.selfDestroyEmbedMSG(embed.build(), 120000, event);
        } else {
            messageActions.moduleIsDeactivated(event, "quotes");
        }

    }


}
