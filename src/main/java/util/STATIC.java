package util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.javatuples.Triplet;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class STATIC {

    public static final String VERSION = "v2.16.2";

    public static final String PREFIX = "/";

    public static final String BOT_ID = "393375474056953856";

    private static boolean isNarration = false;

    private static VoiceChannel narrationChannel = null;

    private static boolean isDiscussion = false;

    private static List<Member> readers = new ArrayList<>();

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

    private static final List<Pair<String, Integer>> commandSpammer = new ArrayList<>();

    public static int addCommandSpammer(String id) {
        int index = 0;
        try {
            for (Pair pair : commandSpammer) {
                if (pair.getLeft().equals(id)) {
                    int uses = (int) pair.getRight() + 1;
                    if (uses >= 3)
                        uses = 0;
                    commandSpammer.remove(index);
                    commandSpammer.add(Pair.of(id, uses));
                    return uses;
                }
                index++;
            }
        } catch (Exception ignored) {}

        commandSpammer.add(Pair.of(id, 1));
        return 1;
    }

    public static Triplet getExperienceUser(String id, String guildId) throws SQLException {
        String[] answer;
        answer = core.databaseHandler.database(guildId, "select xp, level, coins from users where id = '" + id + "'");
        assert answer != null;
        Long xp = Long.valueOf(answer[0]);
        Long level = Long.valueOf(answer[1]);
        Long coins = Long.valueOf(answer[2]);

        return Triplet.with(xp, level, coins);
    }

    public static void updateExperienceUser(String id, String guildId, Long xp, Long level, Long coins) {
        STATIC.exec.execute(() -> {
            try {
                core.databaseHandler.database(guildId, "update users set xp = xp + " + xp + ", level = level + " + level + ", coins = coins + " + coins + " where id = '" + id + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static Role[] VERIFIED;

    public static void setVerified(Role[] role) {
        VERIFIED = role;
    }

    public static Role[] getVerified() {
        return VERIFIED;
    }

    private static Role[] CAM;

    public static void setCam(Role[] role) {
        CAM = role;
    }

    public static Role[] getCam() {
        return CAM;
    }

    public static final ExecutorService exec = Executors.newCachedThreadPool();
}




