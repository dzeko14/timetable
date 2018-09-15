package my.dzeko.timetable.models;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import my.dzeko.timetable.cache.CacheData;
import my.dzeko.timetable.db.GroupDao;
import my.dzeko.timetable.db.SubjectDao;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.utils.ApiUtils;
import my.dzeko.timetable.utils.ScheduleUtils;
import my.dzeko.timetable.wrappers.DatabaseWrapper;
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper;

public class Model implements IModel{
//  --------------------------------------------------------
    private static Model mInstance;
    private Model() { }
    public static Model getInstance() {
        if(mInstance == null) {
            mInstance = new Model();
        }
        return mInstance;
    }
//  --------------------------------------------------------
    private CacheData mCacheData = new CacheData();

    @Override
    public List<Group> getGroupList() {
       return DatabaseWrapper.getDatabase().getGroupDao().getAllGroups();
    }

    @Override
    public Schedule getSelectedSchedule() {
        String selectedSchedule = SharedPreferencesWrapper.getInstance().getSelectedGroup();

        //Get selected schedule from cache if its there
        if(mCacheData.isScheduleCached(selectedSchedule)) {
            return mCacheData.getCachedSchedule(selectedSchedule);
        }

        //Else get selected schedule from DB
        try {
            Schedule schedule = ScheduleUtils.fetchScheduleFromList(
                    DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByGroupName(selectedSchedule),
                    selectedSchedule
            );
            mCacheData.addCachedSchedule(selectedSchedule, schedule);
            return schedule;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void selectSchedule(String groupName) {
        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);
        ScheduleObservable observable = ScheduleObservable.getInstance();

        Schedule schedule = null;

        if (mCacheData.isScheduleCached(groupName)) {
            //Get selected schedule from cache if its there
           schedule = mCacheData.getCachedSchedule(groupName);
        } else {
            //Else get selected schedule from DB
            SubjectDao subjectDao = DatabaseWrapper.getDatabase().getSubjectDao();
            try {
                schedule = ScheduleUtils.fetchScheduleFromList(
                        subjectDao.getSubjectsByGroupName(groupName), groupName);
                mCacheData.addCachedSchedule(groupName, schedule);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        observable.notifySelectedScheduleChanged(schedule);
    }

    @Override
    public String getSelectedScheduleGroupName() {
        return SharedPreferencesWrapper.getInstance().getSelectedGroup();
    }

    @Override
    public void parseSchedule(String groupName) {
        try {
            ApiUtils.parseCurrentWeek();
            ApiUtils.parseSchedule(groupName);
            saveGroupName(groupName);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO Implement on error handling
        } catch (ParseException e) {
            e.printStackTrace();
            //TODO Implement on error handling
        }
    }

    private void saveGroupName(String groupName) {
        GroupDao groupDao = DatabaseWrapper.getDatabase().getGroupDao();
        groupDao.saveGroup(new Group(groupName));
        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);
    }

    @Override
    public void saveCurrentBottomNavigationFragment(int fragmentId) {
        SharedPreferencesWrapper.getInstance().setSelectedFragment(fragmentId);
    }

    @Override
    public int getCurrentBottomNavigationFragment() {
        return SharedPreferencesWrapper.getInstance().getSelectedFragmentId();
    }

    @Override
    public List<Subject> getSchedulesByDayIdAndWeekId(int dayId, int weekId, String groupName) {
        return DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByDayIdAndWeekId(dayId,
                weekId, groupName);
    }

    @Override
    public void removeSubject(Subject subject) {
        DatabaseWrapper.getDatabase().getSubjectDao().removeSubject(subject);
        String selectedSchedule = SharedPreferencesWrapper.getInstance().getSelectedGroup();
        Schedule schedule = mCacheData.getCachedSchedule(selectedSchedule);
        for (Day day: schedule.getSchedule()) {
            if (day.getSubjects().remove(subject)) break;
        }
        ScheduleObservable.getInstance().notifySelectedScheduleChanged(schedule);
    }

    @Override
    public void removeDay(Day day) {
        String selectedSchedule = SharedPreferencesWrapper.getInstance().getSelectedGroup();
        Schedule schedule = mCacheData.getCachedSchedule(selectedSchedule);
        SubjectDao dao = DatabaseWrapper.getDatabase().getSubjectDao();
        for (Subject subject : day.getSubjects()) {
            dao.removeSubject(subject);
        }
        schedule.getSchedule().remove(day);
        ScheduleObservable.getInstance().notifySelectedScheduleChanged(schedule);
    }
}
