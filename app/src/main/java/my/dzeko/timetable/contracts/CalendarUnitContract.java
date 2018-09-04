package my.dzeko.timetable.contracts;

import java.util.Date;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;

public class CalendarUnitContract {
    public interface View extends IView {
        void setDayNumber(int dayPositionInWeek, int week, int number);
        void setMonthName(String monthName);
        void hideWeekRow(int week);
        void setSelectedMonth(int dayPositionInWeek, int week);
        void startActivity(Class c, Date d);
    }

    public interface Presenter extends IPresenter {
        void onCalendarUserClick(int day);
        void onCalendarInitialization();
    }
}
