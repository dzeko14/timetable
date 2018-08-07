package my.dzeko.timetable.models;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;

public class Schedule implements Iterable<Day> {
    private String mGroupName;
    private List<Day> mSchedule;

    public Schedule(String mGroupName, List<Day> mSchedule) {
        this.mGroupName = mGroupName;
        this.mSchedule = mSchedule;
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

    @NonNull
    @Override
    public Iterator<Day> iterator() {
        //TODO: Implement Iterable interface
        return null;
    }

}
