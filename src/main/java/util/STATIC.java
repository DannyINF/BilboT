package util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class STATIC {

    public static final String VERSION = "v2.8.7";

    public static final String PREFIX = "/";

    public static final String BOT_ID = "393375474056953856";

    private static boolean isNarration = false;

    private static VoiceChannel narrationChannel = null;

    private static boolean isDiscussion = false;

    private static List<Member> readers = null;

    public static void changeIsNarration(boolean state) {
        isNarration = state;
    }

    public static boolean getIsNarration() {
        return isNarration;
    }

    public static void changeIsDiscussion(boolean state) {
        isDiscussion = state;
    }

    public static boolean getIsDiscussion() {
        return isDiscussion;
    }

    public static void changeNarrationChannel(VoiceChannel voiceChannel) {
        narrationChannel = voiceChannel;
    }

    public static VoiceChannel getNarrationChannel() {
        return narrationChannel;
    }

    public static void addReader(List<Member> members) {
        readers.addAll(members);
    }

    public static void removeReader(List<Member> members) {
        readers.removeAll(members);

    }

    public static List<Member> getReaders() {
        return readers;
    }

    public static void clearReaders() { readers = null; }

    private static int announcement = 0;

    public static void changeAnnouncement(int gain) {
        announcement += gain;
    }

    public static int getAnnouncement() {
        return announcement;
    }


}



