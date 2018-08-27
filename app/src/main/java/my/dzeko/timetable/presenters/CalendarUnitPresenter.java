package my.dzeko.timetable.presenters;

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
        mView.setMonthName(DateUtils.getMonthName(MONTH));

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

    }

    @Override
    public void onCalendarUserClick(int day) {
        //TODO: Implement
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
