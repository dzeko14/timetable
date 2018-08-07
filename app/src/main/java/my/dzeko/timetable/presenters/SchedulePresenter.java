package my.dzeko.timetable.presenters;

import my.dzeko.timetable.contracts.ScheduleContract;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.models.Schedule;
import my.dzeko.timetable.observers.ScheduleObservable;

public class SchedulePresenter implements ScheduleContract.Presenter {
    private ScheduleContract.View mView;
    private IModel mModel;

    public SchedulePresenter(ScheduleContract.View view) {
        this.mView = view;
        mModel = Model.getInstance();
        ScheduleObservable.getInstance().registerObserver(this);
    }

    @Override
    public boolean onUserClick(int itemId) {
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
        ScheduleObservable.getInstance().unregisterObserver(this);
    }

    @Override
    public void onScheduleRequest() {
        mView.updateSchedule(mModel.getSelectedSchedule());
    }

    @Override
    public void onSelectedScheduleChanged(Schedule schedule) {
        mView.updateSchedule(schedule);
    }
}
