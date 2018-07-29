package my.dzeko.timetable.presenters;

import java.util.List;

import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.contracts.ScheduleContract;
import my.dzeko.timetable.models.Day;
import my.dzeko.timetable.models.Model;

public class SchedulePresenter implements ScheduleContract.ISchedulePresenter {
    private ScheduleContract.IScheduleView mView;
    private IModel mModel;

    public SchedulePresenter(ScheduleContract.IScheduleView view) {
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
    public void registerView(ScheduleContract.IScheduleView view) {
        mView = view;
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
}
