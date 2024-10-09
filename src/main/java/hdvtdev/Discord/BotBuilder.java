package hdvtdev.Discord;

import hdvtdev.Discord.Notification.Notify;
import hdvtdev.System.ENV;
import hdvtdev.System.Stats;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

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
                .addOptions(new OptionData(OptionType.STRING, "week", "неделя четная/нечетная", true)
                        .addChoice("Четная", "even")
                        .addChoice("Нечетная", "odd")
                )
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
        jda.upsertCommand("notify", "прислать уведомление с информацией о следующей паре")
                        .addOption(OptionType.STRING, "group", "группа", true)
                        .addOptions(new OptionData(OptionType.STRING, "dayofweek", "день недели", true)
                        .addChoice("Понедельник", "понедельник")
                        .addChoice("Вторник", "вторник")
                        .addChoice("Среда", "среда")
                        .addChoice("Четверг", "четверг")
                        .addChoice("Пятница", "пятница")
                        .addChoice("Суббота", "суббота")
                        .addChoice("Вся неделя (кроме сб)", "all")
                        ).queue();


        test();

        Stats.startTimeNow();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread.ofVirtual().start(BotBuilder::updatePresence);
        Notify.initialize();

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

    private static void test() {
        String userId = "519931644111749131";

        // Получаем объект пользователя по его ID
        User user = jda.retrieveUserById(userId).complete(); // .complete() блокирует поток до завершения запроса

        // Открываем приватный канал с пользователем
        RestAction<PrivateChannel> dmChannelAction = user.openPrivateChannel();
        PrivateChannel dmChannel = dmChannelAction.complete(); // Ожидаем о

        // Отправляем сообщение в личные сообщения
        dmChannel.sendMessage("Привет! Это личное сообщение.").queue();
    }

    public static JDA getJDA() {
        return jda;
    }

}
