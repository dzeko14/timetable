package my.dzeko.timetable.models;

import java.text.ParseException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import my.dzeko.timetable.cache.CacheData;
import my.dzeko.timetable.db.SubjectDao;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.utils.ApiUtils;
import my.dzeko.timetable.utils.DateUtils;
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
    public void parseSchedule(final String groupName) {
        //Parsing current week number
        ApiUtils.parseCurrentWeek().subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer weekNumber) {
                SharedPreferencesWrapper.getInstance().setKeyDate(
                        DateUtils.createKeyDate(weekNumber == 1)
                );
                //Start parsing the schedule
                startParsingSchedule(groupName);
            }

            @Override
            public void onError(Throwable e) {
                //TODO Implement on error handling
                e.printStackTrace();
            }
        });
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

    private void startParsingSchedule(final String groupName) {
        ApiUtils.parseSchedule(groupName)
                .subscribe(new SingleObserver<Schedule>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Schedule schedule) {
                        ScheduleObservable.getInstance().notifySelectedScheduleChanged(schedule);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO Implement on error handling
                        e.printStackTrace();
                    }
                });
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
