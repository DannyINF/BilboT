package core;

import java.sql.SQLException;

public class modulesChecker {
    public static String moduleStatus(String modul, String id) throws SQLException {
        String status;
        String[] arguments = {"modules", "id = '" + id + "'", "1", modul};
        String[] answer;
        answer = core.databaseHandler.database("serversettings", "select", arguments);
        try {
            assert answer != null;
            status = answer[0];
        } catch (Exception e) {
            status = "deactivated";
        }
        if (!status.equals("activated") && !status.equals("deactivated")) {
            status = "activated";
        }

        return status;
    }

}
