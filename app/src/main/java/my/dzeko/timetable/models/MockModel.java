package my.dzeko.timetable.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.api.ApiBuilder;
import my.dzeko.timetable.entities.ApiRespond;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.utils.ScheduleUtils;

// A mock model
public class MockModel implements IModel{
//  --------------------------------------------------------
    private static MockModel mInstance;
    private MockModel() { }
    public static MockModel getInstance() {
        if(mInstance == null) {
            mInstance = new MockModel();
        }
        return mInstance;
    }
//  --------------------------------------------------------

    private Schedule genereteFakeData() {
        List<Day> days = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Day d = new Day(i,1,"Понедылок", "22.05", "Перший тиждень", false);
            days.add(d);
            for (int j = 0; j < 5; j++) {
                d.attachSubject(new Subject(i, 1, "IT", "Test Subject", "Test Subject", "121", "Teacher", j));
            }
        }

        days.get(0).setIsFirstDayInTheWeek(true);

        for (int i = 0; i < 6; i++) {
            Day d = new Day(i,2,"Понедылок", "22.05", "Другий тиждень", false);
            days.add(d);
            for (int j = 0; j < 5; j++) {
                d.attachSubject(new Subject(i, 2, "IT", "Test Subject", "Test Subject", "121", "Teacher", j));
            }
        }
        days.get(6).setIsFirstDayInTheWeek(true);

        return new Schedule("IT", days);
    }

    private List<String> generateGroupList() {
        List<String> groups = new ArrayList<>();

        groups.add("It");
        groups.add("Ip");
        groups.add("Iq");
        return groups;
    }

    @Override
    public List<String> getGroupList() {
        return generateGroupList();
    }

    @Override
    public Schedule getSelectedSchedule() {
        return genereteFakeData();
    }

    @Override
    public void selectSchedule(String groupName) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScheduleObservable.getInstance().notifySelectedScheduleChanged(genereteFakeData());
    }

    @Override
    public String getSelectedScheduleGroupName() {
        return "It";
    }

    @Override
    public void parseSchedule(final String groupName) {
        Single<ApiRespond> schedule = ApiBuilder.buildScheduleServiceObservable(groupName);

        schedule.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ApiRespond, Schedule>() {
                    @Override
                    public Schedule apply(ApiRespond apiRespond) throws Exception {
                        return ScheduleUtils.fetchSchedule(apiRespond, groupName);
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Schedule, Schedule>() {
                    @Override
                    public Schedule apply(Schedule schedule) throws Exception {
                        //Writing to DataBase
                        TimeUnit.SECONDS.sleep(3);
                        //TODO: Create DataBase
                        return schedule;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Schedule>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onSuccess(Schedule schedule) {
                        ScheduleObservable.getInstance().notifySelectedScheduleChanged(schedule);
                        mDisposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO Implement on error
                    }
                });
    }

}
