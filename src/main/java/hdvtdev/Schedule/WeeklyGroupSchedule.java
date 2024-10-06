package hdvtdev.Schedule;

public record WeeklyGroupSchedule(

        String groupName,
        DailyGroupSchedule[] dailyGroupSchedule

) {}
