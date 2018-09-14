package my.dzeko.timetable.interfaces;

import java.util.List;

import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;

public interface IModel {
    List<Group> getGroupList();
    Schedule getSelectedSchedule();
    void selectSchedule(String groupName);
    String getSelectedScheduleGroupName();
    void parseSchedule(String groupName);
    void saveCurrentBottomNavigationFragment(int fragmentId);
    int getCurrentBottomNavigationFragment();
    List<Subject> getSchedulesByDayIdAndWeekId(int dayId, int weekId, String groupName);

    void removeSubject(Subject subject);

    void removeDay(Day day);
}
