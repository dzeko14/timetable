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
import my.dzeko.timetable.entities.ScheduleApiRespond;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.entities.WeekApiRespond;
import my.dzeko.timetable.observers.GroupObservable;
import my.dzeko.timetable.wrappers.DatabaseWrapper;
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper;

public abstract class ApiUtils {
    public static Single<Schedule> parseSchedule(final String groupName) {
        Observable<ScheduleApiRespond> schedule = ApiBuilder.buildGetGroupScheduleServiceObservable(groupName);
        final AppDatabase database = DatabaseWrapper.getDatabase();
        final SubjectDao subjectDao = database.getSubjectDao();


        return schedule.subscribeOn(Schedulers.io())
                .map(new Function<ScheduleApiRespond, ScheduleApiRespond>() {
                    @Override
                    public ScheduleApiRespond apply(ScheduleApiRespond apiRespond) throws Exception {
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
                .flatMap(new Function<ScheduleApiRespond, Observable<Subject>>() {
                    @Override
                    public Observable<Subject> apply(ScheduleApiRespond apiRespond) throws Exception {
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

    public static Single<Integer> parseCurrentWeek(){
        Single<WeekApiRespond> currentWeek = ApiBuilder.buildGetCurrentWeekServiceObservable();

        return currentWeek.subscribeOn(Schedulers.io())
                .map(new Function<WeekApiRespond, Integer>() {
                    @Override
                    public Integer apply(WeekApiRespond apiRespond) throws Exception {
                        return apiRespond.getData();
                    }
                });
    }
}
