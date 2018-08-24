package my.dzeko.timetable.models;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import my.dzeko.timetable.cache.CacheData;
import my.dzeko.timetable.db.SubjectDao;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
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
        return ScheduleUtils.fetchScheduleFromList(
                DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByGroupName(selectedSchedule),
                selectedSchedule
        );
    }

    @Override
    public void selectSchedule(String groupName) {
        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);
        ScheduleObservable observable = ScheduleObservable.getInstance();

        Schedule schedule;

        if (mCacheData.isScheduleCached(groupName)) {
            //Get selected schedule from cache if its there
           schedule = mCacheData.getCachedSchedule(groupName);
        } else {
            //Else get selected schedule from DB
            SubjectDao subjectDao = DatabaseWrapper.getDatabase().getSubjectDao();
            schedule = ScheduleUtils.fetchScheduleFromList(
                    subjectDao.getSubjectsByGroupName(groupName), groupName);
        }

        observable.notifySelectedScheduleChanged(schedule);
    }

    @Override
    public String getSelectedScheduleGroupName() {
        return SharedPreferencesWrapper.getInstance().getSelectedGroup();
    }

    @Override
    public void parseSchedule(final String groupName) {
        ApiUtils.parse(groupName)
                .subscribe(new SingleObserver<Schedule>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Schedule schedule) {
                ScheduleObservable.getInstance().notifySelectedScheduleChanged(schedule);
                mCacheData.setCachedSchedule(groupName, schedule);
            }

            @Override
            public void onError(Throwable e) {
                //TODO Implement on error handling
                e.printStackTrace();
            }
        });
    }

}
