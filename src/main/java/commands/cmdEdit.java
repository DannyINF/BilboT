package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class cmdEdit implements Command {

    //TODO: create

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        if (Objects.requireNonNull(event.getMember()).getUser().getId().equals("277746420281507841")) {
            switch (args[0]) {
                case "update":
                    List<String> list_update = new ArrayList<>();
                    list_update.add(args[2]);
                    list_update.add("id = '" + args[3] + "'");
                    int i = 4;
                    while (i < args.length) {
                        list_update.add(args[i]);
                        i++;
                    }
                    String[] arguments_update = list_update.toArray(new String[0]);
                    core.databaseHandler.database(args[1], "update", arguments_update);
                    break;
                case "create":
                    List<String> list_create = new ArrayList<>();
                    list_create.add(args[2]);
                    i = 3;
                    while (i < args.length) {
                        list_create.add(args[i] + " " + args[i + 1]);
                        i += 2;
                    }
                    String[] arguments_create = list_create.toArray(new String[0]);
                    core.databaseHandler.database(args[1], "create", arguments_create);
                    break;
                case "drop":
                    List<String> list_drop = new ArrayList<>();
                    list_drop.add(args[2]);
                    String[] arguments_drop = list_drop.toArray(new String[0]);
                    core.databaseHandler.database(args[1], "drop", arguments_drop);
                    break;
                case "select":
                    List<String> list_select = new ArrayList<>();
                    list_select.add(args[2]);
                    list_select.add("id = '" + args[3] + "'");
                    i = 4;
                    while (i < args.length) {
                        list_select.add(args[i]);
                        i++;
                    }
                    String[] arguments_select = list_select.toArray(new String[0]);
                    String[] answer;
                    answer = core.databaseHandler.database(args[1], "select", arguments_select);
                    StringBuilder sb_answer = new StringBuilder();
                    assert answer != null;
                    for (String str : answer) {
                        sb_answer.append(str).append("\n");
                    }
                    event.getChannel().sendMessage(sb_answer.toString()).queue();
                    break;
                case "insert":
                    List<String> list_insert = new ArrayList<>();
                    list_insert.add(args[2]);
                    list_insert.add("id = '" + args[3] + "'");
                    i = 4;
                    while (i < args.length) {
                        list_insert.add(args[i]);
                        i++;
                    }
                    String[] arguments_insert = list_insert.toArray(new String[0]);
                    core.databaseHandler.database(args[1], "insert", arguments_insert);
                    break;
                case "alter":
                    List<String> list_alter = new ArrayList<>();
                    list_alter.add(args[2]);
                    list_alter.add(args[3]);
                    list_alter.add(args[4]);
                    String[] arguments_alter = list_alter.toArray(new String[0]);
                    core.databaseHandler.database(args[1], "alter", arguments_alter);

                    List<String> list_updatealter = new ArrayList<>();
                    list_updatealter.add(args[2]);
                    list_updatealter.add("true");
                    list_updatealter.add(args[3]);
                    list_updatealter.add(args[5]);

                    String[] arguments_updatealter = list_updatealter.toArray(new String[0]);
                    core.databaseHandler.database(args[1], "update", arguments_updatealter);
                    break;
                case "help":
                    event.getChannel().sendMessage("\\/edit update <db> <table> <id> <category_n> <value_n>").queue();
                    event.getChannel().sendMessage("\\/edit create <db> <table> <category_n> <value_n>").queue();
                    event.getChannel().sendMessage("\\/edit insert <db> <table> <id> <category_n> <value_n>").queue();
                    event.getChannel().sendMessage("\\/edit select <db> <table> <id> <size> <category_n>").queue();
                    event.getChannel().sendMessage("\\/edit alter <db> <table> <row> <data_type> <insertion>").queue();
                    break;
            }
        }
    }
}
