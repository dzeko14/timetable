package my.dzeko.timetable.models;

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

}
