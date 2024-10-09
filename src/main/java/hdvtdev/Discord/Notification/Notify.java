package hdvtdev.Discord.Notification;

import hdvtdev.Discord.BotBuilder;
import hdvtdev.Schedule.ClassData;
import hdvtdev.Schedule.DailyGroupSchedule;
import hdvtdev.Schedule.ScheduleManager;
import hdvtdev.Schedule.WeeklyGroupSchedule;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.requests.RestAction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Notify {

    private static final String[] classEndTime = new String[]{"09:39", "11:09", "12:09", "14:09", "16:52", "17:29"};

    public static void initialize() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(Notify::sendNotification, 0, 1, TimeUnit.SECONDS);
        UserDataManager.addUserData(new UserData("519931644111749131", "216-ИС-23", "вторник"));
        testNotify();// изменено на 1 минуту
    }

    public static int checkTime() {
        LocalTime localTime = LocalTime.now();

        for (int i = 0; i < classEndTime.length; i++) {
            if (localTime.format(DateTimeFormatter.ofPattern("HH:mm")).equals(classEndTime[i])) {
                return i;
            }
        }
        return -1;
    }

    private static String checkWeek() {
        LocalDate today = LocalDate.now();
        int weekOfYear = today.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        return (weekOfYear % 2 == 0) ? "even" : "odd";
    }

    public static void sendNotification() {

        int classTime = checkTime();
        if (classTime == -1) {
            return;
        }

        JDA jda = BotBuilder.getJDA();

        for (UserData ud : UserDataManager.getUserDataFromJSON()) {

            String text = getClassData(ud.group(), classTime, checkWeek());
            if (text == null)
                continue;

            jda.retrieveUserById(ud.userID()).queue(user -> user.openPrivateChannel().queue(dmChannel -> dmChannel.sendMessage(text).queue()));
        }
    }

    private static String getClassData(String group, int classNumber, String week) {

        WeeklyGroupSchedule weeklyGroupSchedule = ScheduleManager.parseSchedule(group, week);
        String result = null;
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        for (DailyGroupSchedule dgs : weeklyGroupSchedule.dailyGroupSchedule()) {
            if (dgs.dayOfWeek().equalsIgnoreCase(dayOfWeek.toString())) {
                ClassData[] classData = dgs.classData();
                if (classNumber >= classData.length || classNumber < 0) {
                    return null;
                }
                result = classData[classNumber].className() + " " + classData[classNumber].classRoom();
                break; // добавлен выход после нахождения нужного дня
            }
        }
        return result;
    }

    public static void testNotify() {
        JDA jda = BotBuilder.getJDA();

        System.out.println("отправил1");

        for (UserData ud : UserDataManager.getUserDataFromJSON()) {

            System.out.println("отправил2");

            String text = getClassData(ud.group(), 4, checkWeek());
            if (text == null)
                continue;

            System.out.println("отправил3");

            jda.retrieveUserById(ud.userID()).queue(user -> {
                user.openPrivateChannel().queue(dmChannel -> {
                    dmChannel.sendMessage(text).queue();
                });
            });
        }
    }


}
