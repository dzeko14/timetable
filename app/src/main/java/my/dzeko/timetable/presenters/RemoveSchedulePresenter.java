package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.contracts.RemoveScheduleContract;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.AbstractModel;

public class RemoveSchedulePresenter implements RemoveScheduleContract.Presenter {
    private RemoveScheduleContract.View mView;
    private IModel mModel = AbstractModel.getModel();

    private CompositeDisposable mCD = new CompositeDisposable();

    public RemoveSchedulePresenter(RemoveScheduleContract.View view) {
        mView = view;
    }

    @Override
    public boolean onUserClick(int itemId) {
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
        mCD.dispose();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupListRequest() {
        mView.showLoading();
        mCD.add(
        Single.just(mModel)
                .map(new Function<IModel, List<Group>>() {
                    @Override
                    public List<Group> apply(IModel mModel) throws Exception {
                        return mModel.getGroupList();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Group>>(){
                    @Override
                    public void onSuccess(List<Group> groups) {
                        mView.setGroupList(groups);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    @Override
    public void onRemoveScheduleClick(final String groupName) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.removeSchedule(groupName);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();

    }
}
