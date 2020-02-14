package util;

public class STATIC {

    public static final String VERSION = "1.0.0";

    public static final String PREFIX = "/";

    public static final String BOT_ID = "393375474056953856";

    private static boolean isLyrikabend = false;

    public static void changeIsLyrikabend(boolean state) {
        isLyrikabend = state;
    }

    public static boolean getIsLyrikabend() {
        return isLyrikabend;
    }


}
