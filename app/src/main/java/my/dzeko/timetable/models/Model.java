package my.dzeko.timetable.models;

import java.util.ArrayList;
import java.util.List;

import my.dzeko.timetable.contracts.ScheduleContract;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.interfaces.IPresenter;

public class Model implements IModel {
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

    @Override
    public void getCurrentSchedule(IPresenter presenter) {
        List<Day> schedule = generetaFakeData();

        ScheduleContract.ISchedulePresenter pres = (ScheduleContract.ISchedulePresenter) presenter;
        pres.onScheduleReceived(schedule);
    }

    private List<Day> generetaFakeData() {
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

        return days;
    }
}
