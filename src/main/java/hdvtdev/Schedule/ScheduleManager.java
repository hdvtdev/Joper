package hdvtdev.Schedule;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleManager {

    private static final String pathToEven = "even.csv";
    private static final String pathToOdd = "odd.csv";

    static class Lesson {
        String subject;
        String room;

        public Lesson(String subject, String room) {
            this.subject = subject;
            this.room = room;
        }

        @Override
        public String toString() {
            String toReturn = String.format("%s, %s", subject, room);
            if (toReturn.length() < 10)
                return "null";
            return toReturn;
        }
    }

    public static WeeklyGroupSchedule parseSchedule(String targetGroup, String week) {
        boolean parsingGroup = false;
        List<String> daysOfWeek = new ArrayList<>();
        Map<String, Map<Integer, Lesson>> timetable = new HashMap<>();
        String path;



        if (week.equalsIgnoreCase("odd")) {
            path = pathToOdd;
        } else if (week.equalsIgnoreCase("even")) {
            path = pathToEven;
        } else {
            return null;
        }



        try {
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] nextLine;
            int lessonNumber;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 0 && nextLine[0] != null && nextLine[0].startsWith("Группа -")) {
                    String currentGroup = nextLine[0].replace("Группа - ", "").trim();

                    if (currentGroup.equals(targetGroup)) {
                        parsingGroup = true;
                    } else if (parsingGroup) {
                        break;
                    }
                }

                if (parsingGroup) {
                    if (nextLine.length > 0 && nextLine[0] != null && nextLine[0].equals("№")) {
                        daysOfWeek.clear();
                        for (int i = 1; i < nextLine.length; i += 2) {
                            if (i < nextLine.length && nextLine[i] != null && !nextLine[i].isEmpty()) {
                                daysOfWeek.add(nextLine[i].trim());
                            }
                        }
                    }

                    if (nextLine.length > 0 && nextLine[0] != null && nextLine[0].matches("\\d+")) {
                        lessonNumber = Integer.parseInt(nextLine[0]);

                        for (int i = 1, dayIndex = 0; i < nextLine.length && dayIndex < daysOfWeek.size(); i += 2, dayIndex++) {
                            String subject = (i < nextLine.length && nextLine[i] != null) ? nextLine[i].trim() : null;
                            String room = (i + 1 < nextLine.length && nextLine[i + 1] != null) ? nextLine[i + 1].trim() : null;

                            if (!timetable.containsKey(daysOfWeek.get(dayIndex))) {
                                timetable.put(daysOfWeek.get(dayIndex), new HashMap<>());
                            }

                            if (subject != null && !subject.isEmpty()) {
                                Lesson lesson = new Lesson(subject, room);
                                timetable.get(daysOfWeek.get(dayIndex)).put(lessonNumber, lesson);
                            }
                        }
                    }
                }
            }

            reader.close();
            DailyGroupSchedule[] dailyGroupSchedules = new DailyGroupSchedule[daysOfWeek.size()];

            for (int i = 0; i < daysOfWeek.size(); i++) {
                String day = daysOfWeek.get(i);
                ClassData[] classDataArray = new ClassData[7];

                for (int j = 1; j <= 7; j++) {
                    Lesson lesson = timetable.getOrDefault(day, new HashMap<>()).get(j);
                    if (lesson != null) {
                        classDataArray[j - 1] = new ClassData((byte) j, lesson.room, lesson.subject);
                    } else {
                        classDataArray[j - 1] = null;
                    }
                }

                dailyGroupSchedules[i] = new DailyGroupSchedule(day, classDataArray);
            }

            return new WeeklyGroupSchedule(targetGroup, dailyGroupSchedules);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
