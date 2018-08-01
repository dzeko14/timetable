package my.dzeko.timetable.contracts;

import java.util.List;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.models.Day;

public class ScheduleContract {
    public interface IScheduleView extends IView{
        void updateSchedule(List<Day> schedule);
    }

    public interface ISchedulePresenter extends IPresenter {
        void onScheduleReceived(List<Day> schedule);
        void onScheduleRequest();
    }
}
