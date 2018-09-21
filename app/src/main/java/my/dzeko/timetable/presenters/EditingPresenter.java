package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.EditingContract;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;

public class EditingPresenter implements EditingContract.Presenter {
    private EditingContract.View mView;
    private IModel mModel;

    private CompositeDisposable mCD = new CompositeDisposable();

    public EditingPresenter(EditingContract.View view) {
        mView = view;
        mModel = Model.getInstance();
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

    @Override
    public void onViewCreated() {
        mCD.add(
          getSchedule().subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(new DisposableSingleObserver<Schedule>() {
              @Override
              public void onSuccess(Schedule schedule) {
                  setupSchedule(schedule);
                  mView.hideLoading();
              }

              @Override
              public void onError(Throwable e) {

              }
          })
        );
    }

    private void setupSchedule(Schedule schedule) {
        Week firstWeek = schedule.getFirstWeek();
        Week secondWeek = schedule.getSecondWeek();

        String firstWeekTitle = mView.getContext().getString(R.string.first_week);
        String secondWeekTitle = mView.getContext().getString(R.string.second_week);

        if(firstWeek == null) firstWeek = new Week(new ArrayList<Day>(), 1);
        if(secondWeek == null) secondWeek = new Week(new ArrayList<Day>(), 2);

        mView.addWeekToAdapter(firstWeek, firstWeekTitle);
        mView.addWeekToAdapter(secondWeek, secondWeekTitle);

        mView.setupAdapter();
    }

    @SuppressLint("CheckResult")
    private Single<Schedule> getSchedule(){
        return Single.just(mModel).map(new Function<IModel, Schedule>() {
            @Override
            public Schedule apply(IModel model) throws Exception {
                return model.getSelectedSchedule();
            }
        });
    }


}
