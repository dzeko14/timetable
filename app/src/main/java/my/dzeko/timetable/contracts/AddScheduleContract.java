package my.dzeko.timetable.contracts;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.observers.interfaces.IScheduleObserver;

public class AddScheduleContract {
    public interface View extends IView {
        String getGroupName();
        void showEmptyGroupName();
        void close();
        void startService(String groupName, Class c);
        void notifyScheduleCreated();
        void showChoseWeekNumberDialog();

    }

    public interface Presenter extends IPresenter, IScheduleObserver {
        void createGroupSchedule();

        void setCurrentWeek(int which);
    }
}
