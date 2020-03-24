package core;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.util.ArrayList;
import java.util.Collections;

public class commandParser {

    public static commandContainer parser(String raw, MessageReceivedEvent event) {
        String beheaded;
        beheaded = raw.replaceFirst(STATIC.PREFIX, "");

        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        Collections.addAll(split, splitBeheaded);
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new commandContainer(raw, beheaded, splitBeheaded, invoke, args, event);

    }

    public static class commandContainer {

        public final String[] args;
        public final MessageReceivedEvent event;
        final String raw;
        final String beheaded;
        final String[] splitBeheaded;
        final String invoke;

        commandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent event) {
            this.raw = rw.toLowerCase();
            this.beheaded = beheaded.toLowerCase();
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke.toLowerCase();
            this.args = args;
            this.event = event;
        }

    }

}
