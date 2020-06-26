package special;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

class askQuestion {

    // --Commented out by Inspection (13.12.2018 22:15):private static InputStream input3 = null;
    private static final Properties prop = new Properties();
    private static final Properties prop2 = new Properties();
    private static final Properties prop3 = new Properties();
    private static InputStream input = null;
    private static InputStream input2 = null;

    public static void createQuestion(GuildMessageReceivedEvent event, long id, Member member) {
        Objects.requireNonNull(event.getGuild().getTextChannelById(id)).putPermissionOverride(member).setDeny(Permission.MESSAGE_WRITE).complete();
        int questiontype = ThreadLocalRandom.current().nextInt(1, 11);
        String right_answer;
        if (questiontype < 10) {
            try {
                input = new FileInputStream("Properties/Quiz/easy-questions-choice.properties");
                prop.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //int questionnumber = ThreadLocalRandom.current().nextInt(0, Integer.parseInt(prop.getProperty("questionnumber")) + 1);
            int questionnumber = 0;
            String answer1 = prop.getProperty("question_" + questionnumber + "_answer1");
            String answer2 = prop.getProperty("question_" + questionnumber + "_answer2");
            String answer3 = prop.getProperty("question_" + questionnumber + "_answer3");
            String answer4 = prop.getProperty("question_" + questionnumber + "_answer4");
            right_answer = prop.getProperty("question_" + questionnumber + "_right_answer");
            String question = prop.getProperty("question_" + questionnumber);
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(event.getGuild().getTextChannelById(id)).sendMessage("Frage: " + question +
                    "\n(1) " + answer1 +
                    "\n(2) " + answer2 +
                    "\n(3) " + answer3 +
                    "\n(4) " + answer4).queue();

        } else {
            try {
                input2 = new FileInputStream("Properties/Quiz/easy-questions-answer.properties");
                prop2.load(input2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //int questionnumber = ThreadLocalRandom.current().nextInt(0, Integer.parseInt(prop.getProperty("questionnumber")) + 1);
            int questionnumber = 0;
            right_answer = prop2.getProperty("question_" + questionnumber + "_right_answer");
            String question = prop2.getProperty("question_" + questionnumber);
            try {
                input2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(event.getGuild().getTextChannelById(id)).sendMessage("Frage: " + question + "\n").queue();

        }

        try {
            OutputStream output = new FileOutputStream("Properties/Quiz/quizmaster.properties");
            prop3.setProperty(Objects.requireNonNull(event.getGuild().getTextChannelById(id)).getName() + "_right_answer", right_answer);
            prop3.store(output, null);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(event.getGuild().getTextChannelById(id)).putPermissionOverride(member).setAllow(Permission.MESSAGE_WRITE).complete();
    }

}
