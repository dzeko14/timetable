package my.dzeko.timetable.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Subject implements Comparable<Subject> {
    private int mDayId;
    private int mWeekId;
    private int mPosition;
    private String mGroup;
    private String mSubjectName;
    private String mFullSubjectName;
    private String mCabinet;
    private String mTeacher;

    public Subject (int dayId, int weekId, String group, String subjectName,
                    String fullSubjectName, String cabinet, String teacher, int position) {
        mDayId = dayId;
        mWeekId = weekId;
        mGroup = group;
        mSubjectName = subjectName;
        mCabinet = cabinet;
        mTeacher = teacher;
        mPosition = position;
        mFullSubjectName = fullSubjectName;
    }

    public String getSubjectName() {
        return mSubjectName;
    }

    public String getCabinet() {
        return mCabinet;
    }

    public String getTeacher() {
        return mTeacher;
    }

    public int getPosition() {
        return mPosition;
    }

    public String getGroup() {
        return mGroup;
    }

    public int getDayId() {
        return mDayId;
    }

    public int getWeekId() {
        return mWeekId;
    }

    public String getFullSubjectName() {
        return mFullSubjectName;
    }

    public void attachToDay(ArrayList<Day> days, String groupName) throws Exception {
        Day day = null;

        for (Day d : days) {
            if (d.getId() == mDayId && d.getWeekId() == mWeekId && groupName.equalsIgnoreCase(mGroup)){
                day = d;
                break;
            }
        }

        if(day == null) {
            throw new Exception("Day hadnt been found!!!");
        }

        day.attachSubject(this);
    }

    @Override
    public String toString() {
        return String.format("mDay %s \n mWeek %s \n mPosition %s\n mGroup %s\n mSubjectName %s\n mFullSubjectName %s\n mCabinet %s\n mTeacher %s\n",
                mDayId, mWeekId, mPosition, mGroup, mSubjectName, mFullSubjectName, mCabinet, mTeacher);
    }

    @Override
    public int compareTo(@NonNull Subject subject) {
        return mPosition - subject.mPosition;
    }
}