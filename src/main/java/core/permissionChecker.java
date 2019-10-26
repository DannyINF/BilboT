package core;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class permissionChecker {
    public static boolean checkPermission(Permission[] permission, Member member) {
        boolean hasPermission = true;
        for (Permission perm : permission) {
            if (!member.hasPermission(perm)) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }
}
