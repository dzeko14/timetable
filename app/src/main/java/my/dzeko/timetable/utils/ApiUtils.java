package my.dzeko.timetable.utils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.api.ApiBuilder;
import my.dzeko.timetable.db.AppDatabase;
import my.dzeko.timetable.db.GroupDao;
import my.dzeko.timetable.db.SubjectDao;
import my.dzeko.timetable.entities.ApiRespond;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.observers.GroupObservable;
import my.dzeko.timetable.wrappers.DatabaseWrapper;
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper;

public abstract class ApiUtils {
    public static Single<Schedule> parse(final String groupName) {
        Observable<ApiRespond> schedule = ApiBuilder.buildScheduleServiceObservable(groupName);
        final AppDatabase database = DatabaseWrapper.getDatabase();
        final SubjectDao subjectDao = database.getSubjectDao();


        return schedule.subscribeOn(Schedulers.io())
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
                });
    }
}
