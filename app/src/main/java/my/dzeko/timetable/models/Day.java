package my.dzeko.timetable.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Day implements Comparable<Day> {
    private int mId;
    private int mWeekId;
    private String mName;
    private String mDate;
    private String mWeek;
    private ArrayList<Subject> mSubjects;
    private boolean mIsFirstDayInTheWeek;
    private boolean mIsSelected;

    public Day(int id, int weekId, String name, String date, String week, boolean isFirstDayInTheWeek) {
        mSubjects = new ArrayList<>(5);
        mId = id;
        mWeekId = weekId;
        mName = name;
        mDate = date;
        mWeek = week;
        mIsFirstDayInTheWeek = isFirstDayInTheWeek;
        mIsSelected = false;
    }

    public String getName() {
        return mName;
    }

    public String getDate() {
        return mDate;
    }

    public final ArrayList<Subject> getSubjects() {
        return mSubjects;
    }

    public String getWeek() {
        return mWeek;
    }

    public boolean isFirstDayInTheWeek() {
        return mIsFirstDayInTheWeek;
    }

    public void setIsFirstDayInTheWeek(boolean isFirstDayInTheWeek){
        mIsFirstDayInTheWeek = isFirstDayInTheWeek;
    }

    public int getId() {
        return mId;
    }

    public int getWeekId() {
        return mWeekId;
    }

    public int getSubjectAmount() {
        return mSubjects.size();
    }

    public void attachSubject(Subject subject) {
        mSubjects.add(subject);
    }

    public int getDayOfMonth() {
        return Integer.parseInt(mDate.substring(0,2));
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

/*    public static ArrayList<Day> createScheduleTemplate() {
        DateUtils dateUtils = DateUtils.getInstance();
        ArrayList<Day> days = new ArrayList<>(12);
        for (int i = 0; i < 6; i++) {
            days.add(new Day(i,
                    0,
                    dateUtils.DAYS_NAMES[i],
                    dateUtils.getDate(i,
                            0),
                    dateUtils.WEEKS_NAMES[0],
                    i == 0)
            );
        }

        for (int i = 0; i < 6; i++) {
            days.add(new Day(i,
                    1,
                    dateUtils.DAYS_NAMES[i],
                    dateUtils.getDate(i, 1),
                    dateUtils.WEEKS_NAMES[1],
                    i == 0)
            );
        }

        return days;
    } */

    public static void removeEmptyDaysFromSchedule(ArrayList<Day> schedule) {
        for (int i = 0; i < schedule.size(); i++) {
            Day day = schedule.get(i);
            if (day.getSubjectAmount() == 0) {
                if(day.isFirstDayInTheWeek()){
                    try {
                        schedule.get(i + 1).setIsFirstDayInTheWeek(true);
                    } catch (IndexOutOfBoundsException e) {
                        Log.d("MyLog", "Empty week");
                    }
                }

                schedule.remove(day);
                i--;
            }
        }
    }


    public void sortSubjects(){
        Collections.sort(mSubjects);
    }

    public void removeAllSubjects(){
        mSubjects.clear();
    }

    @Override
    public int compareTo(@NonNull Day day) {
        //return (this.mId + 1)*(this.mWeekId + 1) - (day.mId + 1)*(day.mWeekId + 1);
        if(mWeekId == day.mWeekId) {
            return mId - day.mId;
        }
        else if(mWeekId < day.mWeekId) return -1;
        else return 1;
    }
}
