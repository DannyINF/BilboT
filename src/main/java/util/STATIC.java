package util;

import com.google.common.base.Stopwatch;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import org.javatuples.Triplet;

import java.util.*;
import java.util.concurrent.*;

import static listeners.CasualQuizListener.failure;

public class STATIC {
    //TODO: clean

    public static final String VERSION = "v2.22.2";

    public static final String PREFIX = "/";

    public static final String QUESTION_CHANNEL = "740691553689010226";

    public static final String GUILD_ID = "388969412889411585";

    public static final String SEASON = "0";

    public static boolean is2x = false;

    public static boolean toggle2x() {
        is2x = !is2x;
        return is2x;
    }

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

    public static final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    public static String getInvite(Guild guild) {
        String url = null;
        for (Invite inv : guild.retrieveInvites().complete())
            if (!inv.isTemporary() && Objects.equals(inv.getInviter(), Objects.requireNonNull(guild.getOwner()).getUser()))
                url = inv.getUrl();

        if (url == null)
            url = guild.retrieveInvites().complete().get(0).getUrl();
        return url;
    }

    private static final ArrayList<Triplet<String, Stopwatch, ScheduledFuture>> quizStopwatch = new ArrayList<>();

    public static long getQuizStopwatch(PrivateMessageReceivedEvent event) {
        long stopwatch = 0L;
        Triplet target = null;
        for (Triplet<String, Stopwatch, ScheduledFuture> triplet : quizStopwatch) {
            if (triplet.getValue0().equals(event.getAuthor().getId())) {
                if (triplet.getValue1().isRunning())
                    triplet.getValue1().stop();
                stopwatch = triplet.getValue1().elapsed(TimeUnit.MILLISECONDS);
                triplet.getValue1().reset();
                triplet.getValue2().cancel(true);
                target = triplet;
            }
        }
        if (target != null)
            System.out.println(quizStopwatch.remove(target));
        return stopwatch;
    }

    public static void restartQuizStopwatch(PrivateMessageReceivedEvent event, long timeInMillies) {
        ScheduledFuture future = ses.schedule(() -> {
            event.getChannel().sendMessage(">>> Deine Zeit ist abgelaufen!").queue();
            failure(event);
        }, timeInMillies, TimeUnit.MILLISECONDS);
        quizStopwatch.add(new Triplet<>(event.getAuthor().getId(), Stopwatch.createStarted(), future));
    }

    public static int shopstep = 0;

    public static void changeShopstep(int number) {
        shopstep = number;
    }

    public static int getShopstep() {
        return shopstep;
    }
}