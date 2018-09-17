package my.dzeko.timetable.entities;

import java.util.List;

public class Week {
    private List<Day> mDaysList;
    private int mId;

    public Week(List<Day> mDaysList, int mId) {
        this.mDaysList = mDaysList;
        this.mId = mId;
    }

    public List<Day> getDaysList() {
        return mDaysList;
    }

    public int getId() {
        return mId;
    }
}
