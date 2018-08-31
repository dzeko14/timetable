package my.dzeko.timetable.contracts;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.observers.interfaces.IScheduleObserver;

public class ScheduleContract {
    public interface View extends IView{
        void updateSchedule(Schedule schedule);
        void scrollToCurrentDay();
    }

    public interface Presenter extends IPresenter, IScheduleObserver {
        void onScheduleRequest();
    }
}
