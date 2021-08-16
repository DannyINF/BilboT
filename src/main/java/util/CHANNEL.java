package util;

import core.DatabaseHandler;

import java.util.Objects;

public class CHANNEL {
    //TODO: remove setting channel in database mb?
    public static SET_CHANNEL getSetChannel(String channel, String id) {

        String channelid = null;
        boolean msg;

        try {
            channelid = Objects.requireNonNull(DatabaseHandler.database("serversettings", "select " + channel + " from channels where id = '" + id + "'"))[0];
            msg = false;
        } catch (Exception e) {
            msg = true;
        }
        if (channelid == null) {
            msg = true;
        }

        return new SET_CHANNEL(channelid, msg);
    }
}
