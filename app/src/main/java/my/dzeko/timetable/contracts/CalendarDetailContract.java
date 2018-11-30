package my.dzeko.timetable.contracts;

import java.util.Date;
import java.util.List;

import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IEmptyScreen;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;

public class CalendarDetailContract {
    public static final String CLICKED_DATE = "date";

    public interface View extends IView, IEmptyScreen {
        void setupSubjects(List<Subject> subjects);
        void setActionBarTitle(String title);

    }

    public interface Presenter extends IPresenter {
        void findRespectSubjects(Date d);
    }
}
