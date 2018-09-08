package my.dzeko.timetable.presenters;

import java.util.Date;

import my.dzeko.timetable.activities.CalendarDetailActivity;
import my.dzeko.timetable.contracts.CalendarUnitContract;
import my.dzeko.timetable.utils.DateUtils;

public class CalendarUnitPresenter implements CalendarUnitContract.Presenter {
    private CalendarUnitContract.View mView;

    private final int MONTH;

    public CalendarUnitPresenter(CalendarUnitContract.View view, int month) {
        mView = view;
        MONTH = month;
    }

    @Override
    public void onCalendarInitialization() {
        mView.setMonthName(DateUtils.getMonthNameById(MONTH));

        int daysAmount = DateUtils.getDaysAmountInMonth(MONTH);
        int firstDayInWeek = DateUtils.getFirstDayInWeekNumber(MONTH);
        int dayCount = 1;
        int weekCount = 0;

        for (int i = firstDayInWeek; i < 7; i++) {
            mView.setDayNumber(i, weekCount, dayCount++);
        }

        while (dayCount <= daysAmount) {
            weekCount++;
            for (int i = 0; i < 7; i++){
                if(dayCount > daysAmount) {
                    break;
                }
                mView.setDayNumber(i, weekCount, dayCount++);
            }
        }

        weekCount++;
        while (weekCount < 6) {
            mView.hideWeekRow(weekCount++);
        }
        if(MONTH == DateUtils.getCurrentMonth()) {
            int dayNumber = DateUtils.getCurrentDay();
            int week = DateUtils.getCurrentWeek() - 1;
            int dayPositionInWeek = DateUtils.findDayPositionByDayNumber(dayNumber, MONTH);
            mView.setSelectedMonth(dayPositionInWeek, week);
        }
    }

    @Override
    public void onCalendarUserClick(int day) {
        Date date = DateUtils.getDateFromDayAndMonth(day, MONTH);
        mView.startActivity(CalendarDetailActivity.class, date);
    }

    @Override
    public boolean onUserClick(int itemId) {
        // Not used
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
    }
}
