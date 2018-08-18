package my.dzeko.timetable.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Subject implements Comparable<Subject> {
    @SerializedName("day_number")
    private int mDayId;

    @SerializedName("lesson_week")
    private int mWeekId;

    @SerializedName("lesson_number")
    private int mPosition;

    @SerializedName("lesson_name")
    private String mSubjectName;

    @SerializedName("lesson_full_name")
    private String mFullSubjectName;

    @SerializedName("lesson_room")
    private String mCabinet;

    @SerializedName("lesson_type")
    private String mType;

    @SerializedName("teacher_name")
    private String mTeacher;

    private String mGroup;

    public Subject() {
        //Constructor for use in serialization
    }

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

    public String getType() {
        return mType;
    }

    public void setDayId(int mDayId) {
        this.mDayId = mDayId;
    }

    public void setWeekId(int mWeekId) {
        this.mWeekId = mWeekId;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setSubjectName(String mSubjectName) {
        this.mSubjectName = mSubjectName;
    }

    public void setFullSubjectName(String mFullSubjectName) {
        this.mFullSubjectName = mFullSubjectName;
    }

    public void setCabinet(String mCabinet) {
        this.mCabinet = mCabinet;
    }

    public void setTeacher(String mTeacher) {
        this.mTeacher = mTeacher;
    }

    public void setGroup(String mGroup) {
        this.mGroup = mGroup;
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