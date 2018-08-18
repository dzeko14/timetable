package my.dzeko.timetable.contracts;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;

public class AddScheduleContract {
    public interface View extends IView {
        String getGroupName();
        void showEmptyGroupName();
        void close();
    }

    public interface Presenter extends IPresenter {

    }
}
