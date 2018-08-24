package my.dzeko.timetable.models;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.api.ApiBuilder;
import my.dzeko.timetable.cache.CacheData;
import my.dzeko.timetable.db.AppDatabase;
import my.dzeko.timetable.db.GroupDao;
import my.dzeko.timetable.db.SubjectDao;
import my.dzeko.timetable.entities.ApiRespond;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.observers.GroupObservable;
import my.dzeko.timetable.observers.ScheduleObservable;
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

        if(mCacheData.isScheduleCached(selectedSchedule)) {
            return mCacheData.getCachedSchedule(selectedSchedule);
        }

        return ScheduleUtils.fetchScheduleFromList(
                DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByGroupName(selectedSchedule),
                selectedSchedule
        );
    }

    @Override
    public void selectSchedule(String groupName) {
        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);
        ScheduleObservable observable = ScheduleObservable.getInstance();

        if(mCacheData.isScheduleCached(groupName)) {
            observable.notifySelectedScheduleChanged(mCacheData.getCachedSchedule(groupName));
        }

        SubjectDao subjectDao = DatabaseWrapper.getDatabase().getSubjectDao();

        Schedule schedule = ScheduleUtils.fetchScheduleFromList(
                subjectDao.getSubjectsByGroupName(groupName), groupName
        );
        observable.notifySelectedScheduleChanged(schedule);
    }

    @Override
    public String getSelectedScheduleGroupName() {
        return SharedPreferencesWrapper.getInstance().getSelectedGroup();
    }

    @Override
    public void parseSchedule(final String groupName) {
        Observable<ApiRespond> schedule = ApiBuilder.buildScheduleServiceObservable(groupName);
        final AppDatabase database = DatabaseWrapper.getDatabase();
        final SubjectDao subjectDao = database.getSubjectDao();


        schedule.subscribeOn(Schedulers.io())
                .map(new Function<ApiRespond, ApiRespond>() {
                    @Override
                    public ApiRespond apply(ApiRespond apiRespond) throws Exception {
                        //Saving group name to db and preferences
                        final GroupDao groupDao = database.getGroupDao();
                        groupDao.saveGroup(new Group(groupName));
                        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);

                        //Deleting old schedule of this group from db
                        subjectDao.deleteSubjectsByGroupName(groupName);

                        GroupObservable.getInstance().notifyGroupAdded(groupName);

                        return apiRespond;
                    }
                })
                .flatMap(new Function<ApiRespond, Observable<Subject>>() {
                    @Override
                    public Observable<Subject> apply(ApiRespond apiRespond) throws Exception {
                        return Observable.fromIterable(apiRespond.getData());
                    }
                })
                .map(new Function<Subject, Subject>() {
                    @Override
                    public Subject apply(Subject subject) throws Exception {
                        subject.setGroup(groupName);
                        long id = subjectDao.saveSubject(subject);
                        subject.setId(id);
                        return subject;
                    }
                })
                .toList()
                .observeOn(Schedulers.computation())
                .map(new Function<List<Subject>, Schedule>() {
                    @Override
                    public Schedule apply(List<Subject> subjects) throws Exception {
                        return ScheduleUtils.fetchScheduleFromList(subjects, groupName);
                    }
                })
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
                        //TODO Implement on error
                        e.printStackTrace();
                    }
                });
    }

}
