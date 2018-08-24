package my.dzeko.timetable.presenters;

import android.text.TextUtils;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.AddScheduleContract;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.services.ParseScheduleService;

public class AddSchedulePresenter implements AddScheduleContract.Presenter {
    private AddScheduleContract.View mView;
    private IModel mModel;

    public AddSchedulePresenter(AddScheduleContract.View view) {
        this.mView = view;
        mModel = Model.getInstance();
        ScheduleObservable.getInstance().registerObserver(this);
    }

    @Override
    public boolean onUserClick(int itemId) {
        switch (itemId) {
            case R.id.confirmGroupName_button_addGroupActivity:
                parseGroupSchedule();
                return true;
            case R.id.createGroup_button_addGroupActivity:
                createGroupSchedule();
                return true;
        }
        return false;
    }

    private void parseGroupSchedule() {
        String groupName = mView.getGroupName().toUpperCase();
        if(TextUtils.isEmpty(groupName)) {
            mView.showEmptyGroupName();
            return;
        }
        mView.showLoading();
        mView.startService(groupName, ParseScheduleService.class);
    }



    private void createGroupSchedule() {
        //TODO: Implement schedule creating manually
    }

    @Override
    public void destroy() {
        mView = null;
        ScheduleObservable.getInstance().unregisterObserver(this);
    }

    @Override
    public void onSelectedScheduleChanged(Schedule schedule) {
        mView.close();
    }
}
