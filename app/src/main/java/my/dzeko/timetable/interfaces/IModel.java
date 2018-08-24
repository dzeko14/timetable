package my.dzeko.timetable.interfaces;

import java.util.List;

import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;

public interface IModel {
    List<Group> getGroupList();
    Schedule getSelectedSchedule();
    void selectSchedule(String groupName);
    String getSelectedScheduleGroupName();
    void parseSchedule(String groupName);
}
