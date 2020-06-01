package listeners;


// --Commented out by Inspection START (13.12.2018 22:15):
//class boosterListener extends ListenerAdapter {
//    private final Properties prop1 = new Properties();
//    private InputStream input1 = null;
//    private OutputStream output1 = null;
//
//
//    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
//        String boostertime;
//        try {
//            input1 = new FileInputStream("Properties/XP/xp.properties");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            prop1.load(input1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            boostertime = prop1.getProperty("boostertime_" + event.getAuthor().toString());
//            if (LocalDateTime.parse(boostertime).isBefore(LocalDateTime.now())) {
//                try {
//                    output1 = new FileOutputStream("Properties/XP/xp.properties");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                prop1.setProperty("booster_" + event.getAuthor().toString(), String.valueOf(1));
//                prop1.setProperty("boostertime_" + event.getAuthor().toString(), null);
//                try {
//                    prop1.store(output1, null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    output1.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            boostertime = null;
//        }
//        try {
//            input1.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//}
// --Commented out by Inspection STOP (13.12.2018 22:15)
