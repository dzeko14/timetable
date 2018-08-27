package my.dzeko.timetable.presenters;

import my.dzeko.timetable.contracts.CalendarContract;
import my.dzeko.timetable.utils.DateUtils;

public class CalendarPresenter implements CalendarContract.Presenter {
    private CalendarContract.View mView;

    public CalendarPresenter(CalendarContract.View view) {
        this.mView = view;
    }

    @Override
    public boolean onUserClick(int itemId) {
        //Not used
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void onViewInitialized() {
        mView.setMonth(DateUtils.getCurrentMonth());
    }
}
