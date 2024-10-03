package hdvtdev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static JDA jda;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private static final StringBuilder activityBuilder = new StringBuilder();
    private static boolean argued = true;
    public static boolean noGUI = true;
    private static boolean sysinfo = false;

    public static void main(String[] args) {

        if (args.length == 0)
            argued = false;

        if (argued) {
            for (String arg : args) {
                switch (arg.toLowerCase()) {
                    case "--gui" -> noGUI = false;
                    case "--sysinfo" -> sysinfo = true;
                    default -> System.out.println("unknown argument");
                }
            }
        }

        if (!argued || !noGUI) {
            System.out.println("[DisBot] [WARN] It is not recommended to use GUI mode");
            GUI.common();
            initialize();
        }

        if (noGUI) {
            initialize();
        }
    }

    public static void initialize() {
        JDABuilder jdaBuilder = JDABuilder.createLight(ENV.getDiscordToken())
                .addEventListeners(new EventListener())
                .setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda = jdaBuilder.build();

        jda.upsertCommand("clear-ram", "запрашивает сборку мусора у JVM").queue();
        jda.upsertCommand("restart", "рестарт").queue();

        Stats.startTimeNow();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread.ofVirtual().start(Main::updatePresence);
        if (sysinfo)
            executorService.scheduleAtFixedRate(Stats::statsLogger, 0, 1, TimeUnit.SECONDS);

        System.out.println("[DisBot] Loaded all modules. Starting cleanup...");
        System.gc();
    }

    public static void updatePresence() {

        short nextUpdate = (short) (Math.random() * 10);

        activityBuilder.setLength(0);
        activityBuilder.append(Stats.getMemoryUsage())
                .append(" | ")
                .append(Stats.getCpuUsage())
                .append(" | ")
                .append(Stats.getUptime());
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.PLAYING, activityBuilder.toString()));

        executorService.schedule(Main::updatePresence, nextUpdate, TimeUnit.SECONDS);
    }

}
