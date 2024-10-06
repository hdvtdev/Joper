package hdvtdev.Discord.Commands;

import hdvtdev.Schedule.ClassData;
import hdvtdev.Schedule.DailyGroupSchedule;
import hdvtdev.Schedule.ScheduleManager;
import hdvtdev.Schedule.WeeklyGroupSchedule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EmbedSchedule {

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
                         embeds.add(embedBuilder.addField("Пара " + index + ": " , "Нет пары", false).setColor(Color.decode("#006e62")).build());
                         embedBuilder.clear();
                    } else {
                        embeds.add(embedBuilder.addField("Пара " + cd.classNumber() + ":" , cd.className() + " " + cd.classRoom(), false).setColor(Color.decode("#006e62")).build());
                        embedBuilder.clear();
                    }
                    index++;
                }

            }
        }

        embeds.addFirst(embedBuilder.setTitle(targetGroup + ", " + dayOfWeek).setColor(Color.decode("#006e62")).build());
        return embeds;
    }


}
