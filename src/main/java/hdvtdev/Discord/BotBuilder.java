package hdvtdev.Discord;

import hdvtdev.System.ENV;
import hdvtdev.System.Stats;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static hdvtdev.Main.sysinfo;

public class BotBuilder {

    private static JDA jda;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private static final StringBuilder activityBuilder = new StringBuilder();

    public static void initialize() {

        if (ENV.getDiscordToken().equalsIgnoreCase("YOUR_TOKEN")) {
            System.out.println("[DisBot] [ERROR] Discord token in .env file is undefined");
            return;
        }

        JDABuilder jdaBuilder = JDABuilder.createLight(ENV.getDiscordToken())
                .addEventListeners(new EventListener())
                .setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda = jdaBuilder.build();

        jda.upsertCommand("clear-ram", "запрашивает сборку мусора у JVM").queue();
        jda.upsertCommand("restart", "рестарт").queue();
        jda.upsertCommand("schedule", "расписание группы")
                .addOption(OptionType.STRING, "group", "группа", true)
                .addOptions(new OptionData(OptionType.STRING, "dayofweek", "день недели", true)
                        .addChoice("Понедельник", "понедельник")
                        .addChoice("Вторник", "вторник")
                        .addChoice("Среда", "среда")
                        .addChoice("Четверг", "четверг")
                        .addChoice("Пятница", "пятница")
                        .addChoice("Суббота", "суббота")
                )
                .queue();


        Stats.startTimeNow();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread.ofVirtual().start(BotBuilder::updatePresence);
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

        executorService.schedule(BotBuilder::updatePresence, nextUpdate, TimeUnit.SECONDS);
    }

}
