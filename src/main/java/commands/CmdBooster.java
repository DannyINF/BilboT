package commands;

// --Commented out by Inspection START (13.12.2018 22:15):
//public class cmdBooster implements Command {
//    private final Properties prop1 = new Properties();
//
//// --Commented out by Inspection START (13.12.2018 22:15):
////    @Override
////    public boolean called() {
////        return false;
////    }
//// --Commented out by Inspection STOP (13.12.2018 22:15)
//
//// --Commented out by Inspection START (13.12.2018 22:15):
////    @Override
////    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
////        int booster = Integer.parseInt(args[0]);
////        System.out.println(booster);
////        String time = LocalDateTime.now().plusHours(Integer.parseInt(args[1])).toString();
////        System.out.println(time);
////        String user = event.getGuild().getMembersByEffectiveName(event.getGuild().getMemberById(args[2].substring(args[2].length()-19,
////                args[2].length()-1)).getEffectiveName(), true).get(0).getUser().toString();
////        System.out.println(user);
////
////        OutputStream output1 = new FileOutputStream("Properties/XP/xp.properties");
//// --Commented out by Inspection START (13.12.2018 22:15):
//////        prop1.setProperty("booster_" + user, String.valueOf(booster));
//////        prop1.setProperty("boostertime_" + user, String.valueOf(time));
// --Commented out by Inspection STOP (13.12.2018 22:15)
// --Commented out by Inspection STOP (13.12.2018 22:15)
//        prop1.store(output1, null);
//        output1.close();
//    }
// --Commented out by Inspection STOP (13.12.2018 22:15)

//    @Override
//    public void executed(GuildMessageReceivedEvent event) {
//
//        messageActions.logCommand(event);    }
//
//    @Override
//    public String help() {
//        return null;
//    }
//}
