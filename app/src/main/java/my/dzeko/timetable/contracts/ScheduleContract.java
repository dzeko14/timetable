package my.dzeko.timetable.contracts;

import java.util.List;

import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.models.Day;

public class ScheduleContract {
    public interface IScheduleView{
        void updateSchedule(List<Day> schedule);
    }

    public interface ISchedulePresenter extends IPresenter {
        void onScheduleReceived(List<Day> schedule);
        void onScheduleRequest();

        void registerView(IScheduleView view);
        void registerModel(IModel model);
    }
}
