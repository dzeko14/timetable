package my.dzeko.timetable.interfaces;

import android.content.Context;

import java.util.List;

import my.dzeko.timetable.models.Schedule;

public interface IModel {
    List<String> getGroupList();
    Schedule getSelectedSchedule();
    void selectSchedule(String groupName);
    String getSelectedScheduleGroupName();
    void parseSchedule(String groupName);
}
