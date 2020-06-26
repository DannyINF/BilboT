package special;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// --Commented out by Inspection START (25.06.2020 13:43):
// --Commented out by Inspection START (25.06.2020 13:43):
////class lotrQuiz extends ListenerAdapter {
////
////// --Commented out by Inspection START (13.12.2018 22:15):
////// --Commented out by Inspection START (13.12.2018 22:15):
////////    // --Commented out by Inspection (13.12.2018 22:15):private static Properties prop = new Properties();
////////    private static Properties prop2 = new Properties();
//////// --Commented out by Inspection STOP (13.12.2018 22:15)
//////    // --Commented out by Inspection (13.12.2018 // --Commented out by Inspection (13.12.2018 22:15):22:15):private static InputStream input = null;
//////    private static OutputStream output = null;
////// --Commented out by Inspection STOP (13.12.2018 22:15)
////
////    private static TextChannel createQuizChannel(GuildMessageReceivedEvent event) {
////        TextChannel text = event.getGuild().getCategoriesByName("quiz", true).get(0).createTextChannel("user-quiz-" + event.getAuthor().getName())
////                .setTopic("Quizchannel f\u00FCr " + event.getAuthor().getName()).complete();
////        text.createPermissionOverride(event.getGuild().getPublicRole())
////                .setDeny(Permission.VIEW_CHANNEL)
////                .setDeny(Permission.ALL_TEXT_PERMISSIONS).complete();
////        text.createPermissionOverride(event.getMember())
////                .setAllow(Permission.VIEW_CHANNEL)
////                .setAllow(Permission.MESSAGE_READ)
////                .setAllow(Permission.MESSAGE_HISTORY)
////                .setAllow(Permission.MESSAGE_WRITE).complete();
////
////
////        return text;
////    }
////
////    private static void postManual(GuildMessageReceivedEvent event, long id) {
////        event.getGuild().getTextChannelById(id).sendMessage(
////                "Herzlich willkommen beim Herr-Der-Ringe-Quiz des HdR-Forum-Discords.\n" +
////                        "\nIn diesem Channel kannst du das Quiz absolvieren und in der Rangliste aufsteigen.\n" +
////                        "Dabei gibt es 2 Aufgabentypen. Aufgabentyp 1 gleicht einer Multiple-Choice-Aufgabe, bei der du nur eine " +
////                        "Zahl angeben musst. Beispiel:\n" +
////                        "```Frage: Wie hei\u00DFt der Bot?\n1: BilboT\n2: FrodoBot\n3: Bot```" +
////                        "\nUm die Frage richtig zu beantworten, musst du nur '1' in den Chat schreiben." +
////                        "\nW\u00FCrde die Frage keine Antwortm\u00F6glichkeiten umfassen, musst du den korrekten Namen ausschreiben:" +
////                        "\n```Wie hei\u00DFt der Bot?```" +
////                        "\nDie richtige Antwort w\u00E4re hier 'BilboT'." +
////                        "\n\nViel Spa\u00DF!"
////        ).queue();
////    }
////
////    private static void postInterrupt(GuildMessageReceivedEvent event, long id) {
////        event.getGuild().getTextChannelById(id).sendMessage(
////                "\n\n--------------------------------------------------------------------------------------------------------------------------------------\n\n"
////        ).queue();
////    }
////
////    @Override
////    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
////        String channel = event.getChannel().getName();
////
////        //check quiz channel
////        if (channel.equals("quiz")) {
////            String command = event.getMessage().getContentRaw();
////
////            //checks start command
////            if (command.equals("quiz")) {
////                event.getMessage().delete().queue();
////                TextChannel quizchannel = createQuizChannel(event);
////                postManual(event, quizchannel.getIdLong());
////                postInterrupt(event, quizchannel.getIdLong());
// --Commented out by Inspection STOP (25.06.2020 13:43)
//                askQuestion.createQuestion(event, quizchannel.getIdLong(), event.getMember());
//
//            }
//        }
//    }
//}
// --Commented out by Inspection STOP (25.06.2020 13:43)
