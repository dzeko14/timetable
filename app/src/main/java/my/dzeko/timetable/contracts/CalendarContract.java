package my.dzeko.timetable.contracts;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;

public class CalendarContract {
    public interface View extends IView {
        void setMonth(int month);
    }

    public interface Presenter extends IPresenter {
        void onViewInitialized();
    }
}
