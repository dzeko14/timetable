package my.dzeko.timetable.entities;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private String mGroupName;
    private List<Day> mSchedule;

    public Schedule(String mGroupName, List<Day> mSchedule) {
        this.mGroupName = mGroupName;
        this.mSchedule = mSchedule;
    }

    public static Schedule getEmptySchedule() {
        return new Schedule("empty", new ArrayList<Day>());
    }

    public String getGroupName() {
        return mGroupName;
    }

    public List<Day> getSchedule() {
        return mSchedule;
    }

    public void setSchedule(List<Day> schedule) {
        this.mSchedule = schedule;
    }

    public Week getFirstWeek() {
        List<Day> firstWeek = new ArrayList<>();
        for (Day day : mSchedule) {
            if(day.getWeekId() == 1) firstWeek.add(day);
        }
        return new Week(firstWeek, 1);
    }

    public Week getSecondWeek() {
        List<Day> secondWeek = new ArrayList<>();
        for (Day day : mSchedule) {
            if(day.getWeekId() == 2) secondWeek.add(day);
        }
        return new Week(secondWeek, 2);
    }

}
