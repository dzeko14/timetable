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

    public List<Day> getFirstWeek() {
        List<Day> firstWeek = new ArrayList<>();
        for (Day day : mSchedule) {
            if(day.getWeekId() == 1) firstWeek.add(day);
        }
        return firstWeek.size() == 0 ? null : firstWeek;
    }

    public List<Day> getSecondWeek() {
        List<Day> secondWeek = new ArrayList<>();
        for (Day day : mSchedule) {
            if(day.getWeekId() == 2) secondWeek.add(day);
        }
        return secondWeek.size() == 0 ? null : secondWeek;
    }

}
