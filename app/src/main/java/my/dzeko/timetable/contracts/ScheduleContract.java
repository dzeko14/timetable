package my.dzeko.timetable.contracts;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.models.Schedule;
import my.dzeko.timetable.observers.interfaces.IScheduleObserver;

public class ScheduleContract {
    public interface View extends IView{
        void updateSchedule(Schedule schedule);
    }

    public interface Presenter extends IPresenter, IScheduleObserver {
        void onScheduleRequest();
    }
}
