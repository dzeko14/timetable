package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.observers.ScheduleObservable;

public class EditingPresenter implements EditingContract.Presenter {
    private EditingContract.View mView;
    private IModel mModel;

    private CompositeDisposable mCD = new CompositeDisposable();

    public EditingPresenter(EditingContract.View view) {
        mView = view;
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
        mCD.dispose();
    }

    @Override
    public void onSelectedScheduleChanged(Schedule schedule) {

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
        List<Day> firstWeek = schedule.getFirstWeek();
        List<Day> secondWeek = schedule.getSecondWeek();

        if(firstWeek != null) mView.addWeekToAdapter(firstWeek);
        if(secondWeek != null) mView.addWeekToAdapter(secondWeek);

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
