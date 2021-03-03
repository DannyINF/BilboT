package core;

import commands.Command;

import java.util.HashMap;

public class CommandHandler {

    public static final HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(CommandParser.commandContainer cmd) throws Exception {
        System.out.println("handler " + cmd.invoke);
        String addition = "";
        if (cmd.invoke.equals("server")) {
            addition = " " + cmd.args[0];
        }

        if (commands.containsKey(cmd.invoke + addition)) {
            System.out.println("handling");
            boolean safe = commands.get(cmd.invoke + addition).called();
            System.out.println("safe: " + safe);
            if (!safe) {
                commands.get(cmd.invoke + addition).action(cmd.args, cmd.event);
            } /*else {
                commands.get(cmd.invoke + addition).executed(cmd.event);
            }*/
        }
    }
}
