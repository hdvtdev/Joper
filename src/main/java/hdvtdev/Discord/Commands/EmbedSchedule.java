package hdvtdev.Discord.Commands;

import hdvtdev.Schedule.ClassData;
import hdvtdev.Schedule.DailyGroupSchedule;
import hdvtdev.Schedule.ScheduleManager;
import hdvtdev.Schedule.WeeklyGroupSchedule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.*;

public class EmbedSchedule {

    private static final ArrayList<String> name = new ArrayList<>();
    private static final ArrayList<String> value = new ArrayList<>();

    public ArrayList<MessageEmbed> scheduleEmbedBuilder(String targetGroup, String dayOfWeek, String week) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        ArrayList<MessageEmbed> embeds = new ArrayList<>();
        WeeklyGroupSchedule weeklyGroupSchedule = ScheduleManager.parseSchedule(targetGroup, week);
        int index = 1;

        if (weeklyGroupSchedule.groupName().isEmpty()) {
            embeds.add(embedBuilder.setTitle("Неизвестная группа \"" + targetGroup + "\".").setColor(Color.RED).build());
            return embeds;
        }

        if (week.equalsIgnoreCase("even")) {
            week = "Чётная";
        } else {
            week = "Нечётная";
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

                index = 1;

                for (ClassData cd : classData) {
                    if (cd == null) {
                        embeds.add(embedBuilder.addField("Пара " + index, "Нет пары", false).setColor(Color.decode("#006e62")).build());
                        embedBuilder.clear();
                    } else {
                        embeds.add(embedBuilder.addField("Пара " + cd.classNumber(), cd.className() + " " + cd.classRoom(), false).setColor(Color.decode("#006e62")).build());
                        embedBuilder.clear();
                    }
                    index++;
                }
            }
        }

        if (!embeds.isEmpty()) {
            embeds.addFirst(embedBuilder.setTitle(targetGroup + ",  " + week + " " + dayOfWeek, "").setColor(Color.decode("#006e62")).build());
        } else embeds.addFirst(embedBuilder.setTitle("Группа не найдена").setColor(Color.RED).build());
        return embeds;
    }
}


