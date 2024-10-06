package hdvtdev.Discord.Commands;

import hdvtdev.Schedule.ClassData;
import hdvtdev.Schedule.DailyGroupSchedule;
import hdvtdev.Schedule.ScheduleManager;
import hdvtdev.Schedule.WeeklyGroupSchedule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.poi.util.Beta;

import java.awt.*;
import java.util.*;

public class EmbedSchedule {

    private static int maxLength = 0;
    private static final ArrayList<String> name = new ArrayList<>();
    private static final ArrayList<String> value = new ArrayList<>();

    public ArrayList<MessageEmbed> scheduleEmbedBuilder(String targetGroup, String dayOfWeek) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        ArrayList<MessageEmbed> embeds = new ArrayList<>();
        WeeklyGroupSchedule weeklyGroupSchedule = ScheduleManager.parseSchedule("src/main/resources/schedule.csv", targetGroup);
        int index = 1;

        if (weeklyGroupSchedule.groupName().isEmpty()) {
            embeds.add(embedBuilder.setTitle("Неизвестная группа \"" + targetGroup + "\".").setColor(Color.RED).build());
            return embeds;
        }

        DailyGroupSchedule[] dailyGroupSchedule = weeklyGroupSchedule.dailyGroupSchedule();

        for (DailyGroupSchedule dgs : dailyGroupSchedule) {
            ClassData[] classData = dgs.classData();
            if (dgs.dayOfWeek().equalsIgnoreCase(dayOfWeek)) {
                for (ClassData cd : classData) {
                    if (cd == null) {
                        name.add("Пара " + index);
                        value.add("Нет пары");
                    } else {
                        name.add("Пара " + cd.classNumber());
                        value.add(cd.className() + " " + cd.classRoom());
                    }
                    index++;
                }

                findMaxStringLength();
                index = 1;

                for (ClassData cd : classData) {
                    if (cd == null) {
                        embeds.add(embedBuilder.addField(formatToMaxLength("Пара " + index, "Нет пары"), "Нет пары", false).setColor(Color.decode("#006e62")).build());
                        embedBuilder.clear();
                    } else {
                        embeds.add(embedBuilder.addField(formatToMaxLength("Пара " + cd.classNumber(), cd.className() + " " + cd.classRoom()), cd.className() + " " + cd.classRoom(), false).setColor(Color.decode("#006e62")).build());
                        embedBuilder.clear();
                    }
                    index++;
                }
            }
        }

        embeds.addFirst(embedBuilder.setTitle(formatToMaxLength(targetGroup + ",  " + dayOfWeek, "       ")).setColor(Color.decode("#006e62")).build());
        return embeds;
    }

    @Beta
    private static String formatToMaxLength(String toFormat, String add) {
        int lenght = (toFormat.length() + add.length()) + 7;
        return toFormat +
                "⠀".repeat(Math.max(0, maxLength - lenght));
    }

    private static void findMaxStringLength() {
        ArrayList<String> gluedStrings = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            gluedStrings.add(name.get(i) + value.get(i));
        }
        for (String s : gluedStrings) {
            if (s.length() > maxLength)
                maxLength = s.length();
        }
    }
}


