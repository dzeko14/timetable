package my.dzeko.timetable.utils;

import java.util.ArrayList;
import java.util.List;

import my.dzeko.timetable.models.ApiRespond;
import my.dzeko.timetable.models.Day;
import my.dzeko.timetable.models.Schedule;
import my.dzeko.timetable.models.Subject;

public abstract class ScheduleUtils {
    public static Schedule fetchSchedule(ApiRespond apiRespond, String groupName) {
        List<Subject> subjects = apiRespond.getData();
        List<Day> days = createDaysList();

        for (Subject s : subjects) {
            //Set name to the subject, cause parsed list of subjects hadn't it
            s.setGroup(groupName);
            for (Day d: days) {
                if(d.getId() == s.getDayId() && d.getWeekId() == s.getWeekId())
                    d.attachSubject(s);
            }
        }

        List<Day> resultDays = new ArrayList<>();
        for (Day d:
             days) {
            if(d.getSubjectAmount() != 0) {
                resultDays.add(d);
            }
        }

        resultDays.get(0).setIsFirstDayInTheWeek(true);
        for (Day d: resultDays) {
            if(d.getWeekId() == 2) {
                d.setIsFirstDayInTheWeek(true);
                break;
            }
        }

        return new Schedule(groupName, resultDays);
    }

    private static List<Day> createDaysList() {
        final String[] DAY_OF_WEEKS_NAME = {
                "",
                "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота"
        };

        List<Day> days = new ArrayList<>(12);
        for (int i = 1; i < 7; i++) {
            days.add(new Day(
                    i,
                    1 ,
                    DAY_OF_WEEKS_NAME[i],
                    "12.01",
                    "Перший тиждень",
                    false
            ));
        }

        for (int i = 1; i < 7; i++) {
            days.add(new Day(
                    i,
                    2 ,
                    DAY_OF_WEEKS_NAME[i],
                    "12.01",
                    "Другий тиждень",
                    false
            ));
        }
        return days;
    }
}
