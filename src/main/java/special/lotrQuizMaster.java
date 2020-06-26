package special;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

// --Commented out by Inspection START (25.06.2020 13:43):
// --Commented out by Inspection START (25.06.2020 13:43):
////class lotrQuizMaster extends ListenerAdapter {
////
////    private final Properties prop = new Properties();
////    private InputStream input = null;
////    // --Commented out by Inspection (13.12.2018 22:15):private OutputStream output = null;
////
////    @Override
////    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
////
////        if (!event.getAuthor().isBot()) {
////            if (event.getChannel().getName().contains("user-quiz-")) {
////                int i = 0;
////                int y = 0;
////                StringBuilder sb = new StringBuilder();
////                List<TextChannel> channellist = event.getGuild().getTextChannels();
////                while (y < channellist.size()) {
////                    if (channellist.get(y).getName().contains("user-quiz-")) {
////                        sb.append(channellist.get(y).getName());
////                        sb.append("###");
////                    }
////                    y++;
////
////                }
////                String[] channels = sb.toString().split("###");
////                while (i < channels.length) {
////
////                    if (event.getChannel().getName().equals(channels[i])) {
////                        try {
////                            input = new FileInputStream("Properties/Quiz/quizmaster.properties");
////                            prop.load(input);
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                        String right_answer = prop.getProperty(channels[i] + "_right_answer");
////                        try {
////                            input.close();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                        if (event.getMessage().getContentRaw().toLowerCase().equals(right_answer.toLowerCase())) {
////                            event.getChannel().sendMessage("Deine Antwort war richtig!").queue();
////
////                        } else {
////                            event.getChannel().sendMessage("Deine Antwort war falsch!").queue();
// --Commented out by Inspection STOP (25.06.2020 13:43)
//                        }
//                    }
//                    i++;
//                }
//            }
//        }
//
//    }
//}
// --Commented out by Inspection STOP (25.06.2020 13:43)
