package core;

import commands.Command;

import java.util.HashMap;

public class CommandHandlerMusic {

    public static final HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(CommandParser.commandContainer cmd) throws Exception {
        System.out.println("handler " + cmd.invoke);
        if (commands.containsKey(cmd.invoke) && (cmd.invoke.equals("music") || cmd.invoke.equals("fakejoin") || cmd.invoke.equals("target"))) {
            System.out.println("handle command");
            boolean safe = commands.get(cmd.invoke).called();
            System.out.println("safe: " + safe);
            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
            }
        }
    }
}
