package hdvtdev;

import hdvtdev.Discord.BotBuilder;

public class Main {

    private static boolean argued = true;
    public static boolean noGUI = true;
    public static boolean sysinfo = false;

    public static void main(String[] args) {

//        if (args.length == 0)
//            argued = false;
//
//        if (argued) {
//            for (String arg : args) {
//                switch (arg.toLowerCase()) {
//                    case "--gui" -> noGUI = false;
//                    case "--sysinfo" -> sysinfo = true;
//                    default -> System.out.println("unknown argument");
//                }
//            }
//        }
//
//        if (!argued || !noGUI) {
//            System.out.println("[DisBot] [WARN] It is not recommended to use GUI mode");
//            GUI.common();
//            BotBuilder.initialize();
//        }

            BotBuilder.initialize();

    }
}