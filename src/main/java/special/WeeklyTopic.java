package special;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.time.LocalDate;
import java.util.Properties;

import static java.lang.Integer.parseInt;


// --Commented out by Inspection START (25.06.2020 13:43):
// --Commented out by Inspection START (25.06.2020 13:43):
////class weeklyTopic extends ListenerAdapter {
////    private final Properties prop = new Properties();
////    private final Properties prop2 = new Properties();
////    private final Properties prop3 = new Properties();
////    private InputStream input1 = null;
////    private InputStream input2 = null;
////    private InputStream input3 = null;
////
////    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
////
////        // checks activation
////            int i = 0;
////
////            int thisweek = LocalDate.now().getDayOfYear() / 7;
////            int thisyear = LocalDate.now().getYear();
////            int topictime = thisweek * thisyear;
////
////
////            try {
////                input1 = new FileInputStream("Properties/Weekly-Topic/weeklyTopicCore.properties");
////                prop.load(input1);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            String nextTopicTime = prop.getProperty("nextTopicTime");
////
////            try {
////                input1.close();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////
////
////            if (String.valueOf(topictime).equals(nextTopicTime)) {
////                OutputStream output1;
////                try {
////                    output1 = new FileOutputStream("Properties/Weekly-Topic/weeklyTopicCore.properties");
////                    prop.setProperty("nextTopicTime", String.valueOf((LocalDate.now().plusWeeks(1).getDayOfYear() / 7) * LocalDate.now().plusWeeks(1).getYear()));
////                    prop.store(output1, null);
////                    output1.close();
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////                try {
////                    input2 = new FileInputStream("Properties/Weekly-Topic/weeklyTopic.properties");
////                    prop2.load(input2);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                int topicnumbermax = parseInt(prop2.getProperty("topic_number"));
////                int topicnumber = 0;
////
////                try {
////                    input2.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////
////                try {
////                    input3 = new FileInputStream("Properties/Weekly-Topic/weeklyTopicOld.properties");
////                    prop3.load(input3);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                String old_topicnumbers = prop3.getProperty("old_topicnumbers");
////
////                try {
////                    input3.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////
////                while (old_topicnumbers.contains(String.valueOf(topicnumber))) {
////                    topicnumber = (int) (Math.random() * (topicnumbermax + 1));
////                }
////
////                try {
////                    input2 = new FileInputStream("Properties/Weekly-Topic/weeklyTopic.properties");
////                    prop2.load(input2);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////                String topic = prop2.getProperty("topic_" + topicnumber);
////
////                try {
////                    input2.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////
////                StringBuilder sb = new StringBuilder();
////                sb.append(old_topicnumbers);
////                sb.append(" ");
////                sb.append(topicnumber);
////
////                try {
////                    OutputStream output3 = new FileOutputStream("Properties/Weekly-Topic/weeklyTopicOld.properties");
////                    prop3.setProperty("old_topicnumbers", sb.toString());
////                    prop3.store(output3, null);
////                    output3.close();
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////                try {
////                    input1 = new FileInputStream("Properties/Weekly-Topic/weeklyTopicCore.properties");
////                    prop.load(input1);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////                String older_topic = prop.getProperty("new_topic");
////
////                try {
////                    input1.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////
////                try {
////                    output1 = new FileOutputStream("Properties/Weekly-Topic/weeklyTopicCore.properties");
////                    prop.setProperty("current_topic", older_topic);
////                    prop.setProperty("new_topic", topic);
////                    prop.store(output1, null);
////                    output1.close();
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                try {
////                    input1 = new FileInputStream("Properties/Weekly-Topic/weeklyTopicCore.properties");
////                    prop.load(input1);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////
////                event.getGuild().getTextChannelsByName("\uD83D\uDCC5-woechentliches-thema", true).get(0).getManager().setTopic("**" + prop.getProperty("current_topic") + "**  |  Hier werden Ideen \u00fcber das w\u00f6chentliche Thema ausgetauscht! Thema n\u00e4chste Woche: " + prop.getProperty("new_topic")).queue();
// --Commented out by Inspection STOP (25.06.2020 13:43)
//
//                try {
//                    input1.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//        }
//
//    }
//
//}
// --Commented out by Inspection STOP (25.06.2020 13:43)


