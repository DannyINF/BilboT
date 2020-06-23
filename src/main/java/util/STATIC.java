package util;

import com.google.common.base.Stopwatch;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.javatuples.Triplet;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Stopwatch.*;

public class STATIC {

    public static final String VERSION = "v2.15.6";

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

    private static List<Pair<String, Integer>> commandSpammer = new ArrayList<>();

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

    private static Map<String, Map<String, Triplet<Long, Long, Long>>> experienceUser = new HashMap<>();
/*
    public static Triplet getExperienceUser(String id, String guildId) throws SQLException {
        Stopwatch stopwatch = createStarted();
        long xp;
        long level;
        long coins;
        // nur temporär
        if (id.equals("393375474056953856"))
            return Triplet.with(0L, 0L, 0L);
        String[] answer;
        try {
            return experienceUser.get(id).get(guildId);
        } catch (Exception ignored) {}
        answer = core.databaseHandler.database(guildId, "select xp, level, coins from users where id = '" + id + "'");
        assert answer != null;

        xp = answer[0] == null ? 0L : Long.parseLong(answer[0]);
        level = answer[1] == null ? 0L : Long.parseLong(answer[1]);
        coins = answer[2] == null ? 0L : Long.parseLong(answer[2]);

        experienceUser.put(id, (Map<String, Triplet<Long, Long, Long>>) new HashMap<>().put(guildId, Triplet.with(xp, level, coins)));
        System.out.println("get " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return Triplet.with(xp, level, coins);
    }

    public static void updateExperienceUser(String id, String guildId, Long xp, Long level, Long coins) {
        Stopwatch stopwatch = createStarted();
        try {
            Long userXP = experienceUser.get(id).get(guildId).getValue0() + xp;
            Long userLevel = experienceUser.get(id).get(guildId).getValue1() + level;
            Long userCoins = experienceUser.get(id).get(guildId).getValue2() + coins;
            experienceUser.get(id).remove(guildId);
            experienceUser.get(id).put(guildId, Triplet.with(userXP, userLevel, userCoins));
            Stream.of(experienceUser.keySet().toString())
                    .forEach(System.out::println);
        } catch (Exception ignored) {}
        System.out.println("update " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }*/
    public static Triplet getExperienceUser(String id, String guildId) throws SQLException {
        Stopwatch stopwatch = createStarted();
        String[] answer;
        answer = core.databaseHandler.database(guildId, "select xp, level, coins from users where id = '" + id + "'");
        assert answer != null;
        Long xp = Long.valueOf(answer[0]);
        Long level = Long.valueOf(answer[1]);
        Long coins = Long.valueOf(answer[2]);

        System.out.println("get " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return Triplet.with(xp, level, coins);
    }

    public static void updateExperienceUser(String id, String guildId, Long xp, Long level, Long coins) {
        Stopwatch stopwatch = createStarted();
        STATIC.exec.execute(() -> {
            try {
                core.databaseHandler.database(guildId, "update users set xp = xp + " + xp + ", level = level + " + level + ", coins = coins + " + coins + " where id = '" + id + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        System.out.println("update " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
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

    public static ExecutorService exec = Executors.newCachedThreadPool();
}




