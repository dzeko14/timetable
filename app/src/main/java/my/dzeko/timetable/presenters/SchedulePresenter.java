package my.dzeko.timetable.presenters;

import java.util.List;

import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.contracts.ScheduleContract;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.models.Day;
import my.dzeko.timetable.models.Model;

public class SchedulePresenter implements ScheduleContract.ISchedulePresenter {
    private ScheduleContract.IScheduleView mView;
    private IModel mModel;

    public SchedulePresenter(IView view) {
        registerView(view);
        registerModel(Model.getInstance());
    }

    @Override
    public void onScheduleReceived(List<Day> schedule) {
        mView.updateSchedule(schedule);
    }

    @Override
    public void onScheduleRequest() {
        mModel.getCurrentSchedule(this);
    }

    @Override
    public void registerView(IView view) {
        if(view instanceof ScheduleContract.IScheduleView) {
            mView = (ScheduleContract.IScheduleView)view;
        }
    }

    @Override
    public void registerModel(IModel model) {
        mModel = model;
    }


    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public void onUserClick() {

    }
}
