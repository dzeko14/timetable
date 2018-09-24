package my.dzeko.timetable.entities;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Day implements Comparable<Day> {
    private int mId;
    private int mWeekId;
    private String mName;
    private String mDate;
    private String mWeek;
    private ArrayList<Subject> mSubjects;
    private boolean mIsFirstDayInTheWeek;

    public Day(int id, int weekId, String name, String date, String week, boolean isFirstDayInTheWeek) {
        mSubjects = new ArrayList<>(5);
        mId = id;
        mWeekId = weekId;
        mName = name;
        mDate = date;
        mWeek = week;
        mIsFirstDayInTheWeek = isFirstDayInTheWeek;
    }

    public String getName() {
        return mName;
    }

    public String getDate() {
        return mDate;
    }

    public ArrayList<Subject> getSubjects() {
        Collections.sort(mSubjects);
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

    public int getSubjectsAmount() {
        return mSubjects.size();
    }

    public void setDate(String date) {
        this.mDate = date;
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
