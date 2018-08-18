package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import my.dzeko.timetable.R;
import my.dzeko.timetable.api.ApiBuilder;
import my.dzeko.timetable.contracts.AddScheduleContract;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.MockModel;

public class AddSchedulePresenter implements AddScheduleContract.Presenter {
    private AddScheduleContract.View mView;
    private IModel mModel;

    public AddSchedulePresenter(AddScheduleContract.View view) {
        this.mView = view;
        mModel = MockModel.getInstance();
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

    @SuppressLint("CheckResult")
    private void parseGroupSchedule() {
        String groupName = mView.getGroupName();
        if(TextUtils.isEmpty(groupName)) {
            mView.showEmptyGroupName();
            return;
        }

        mView.showLoading();
        String mGroupName = groupName.toUpperCase();

        //TODO: Вынести парсинг в модель
        mModel.parseSchedule(mGroupName);

        ApiBuilder.buildScheduleServiceCompletable(mGroupName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mView.hideLoading();
                mView.close();
            }
        });
    }



    private void createGroupSchedule() {

    }

    @Override
    public void destroy() {
        mView = null;
    }
}
