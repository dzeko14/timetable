package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.R;
import my.dzeko.timetable.activities.CreateOrUpdateSubjectActivity;
import my.dzeko.timetable.adapters.EditWeekExpandableListAdapter;
import my.dzeko.timetable.contracts.CreateOrUpdateSubjectContract;
import my.dzeko.timetable.contracts.EditWeekContract;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.observers.ScheduleObservable;

public class EditWeekPresenter implements EditWeekContract.Presenter {
    private EditWeekContract.View mView;
    private IModel mModel = Model.getInstance();
    private Week mWeek;
    private CompositeDisposable mCD = new CompositeDisposable();

    public EditWeekPresenter(EditWeekContract.View view) {
        mView = view;
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
    public void onEditChildItemClick(Subject subject) {
        Bundle bundle = new Bundle();
        bundle.putLong(CreateOrUpdateSubjectContract.SUBJECT_ID, subject.getId());
        bundle.putInt(CreateOrUpdateSubjectContract.DAY_ID, subject.getDayId());
        bundle.putInt(CreateOrUpdateSubjectContract.WEEK_ID, subject.getWeekId());
        mView.startActivity(bundle, CreateOrUpdateSubjectActivity.class);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRemoveChildItemClick(final Subject subject) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.removeSubject(subject);
                for (int i = 0; i < mWeek.getDaysList().size(); i++) {
                    Day day = mWeek.getDaysList().get(i);

                    if (day.getId() == subject.getDayId()){
                        day.getSubjects().remove(subject);
                    }

                    if (day.getSubjects().size() == 0) {
                        mWeek.getDaysList().remove(day);
                        break;
                    }
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mView.updateAdapter();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO: Implement onError
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void onRemoveGroupItemClick(final Day day) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.removeDay(day);
                mWeek.getDaysList().remove(day);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mView.updateAdapter();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO: Implement onError
                    }
                });
    }

    @Override
    public void setWeek(Week week) {
        mWeek = week;
    }

    private EditWeekExpandableListAdapter getAdapterFromSchedule(List<Day> schedule) {
        return new EditWeekExpandableListAdapter(mView.getContext(), new Week(schedule, mWeek.getId()));
    }

    @Override
    public void onViewInitialized() {
        EditWeekExpandableListAdapter adapter = getAdapterFromSchedule(mWeek.getDaysList());
        adapter.setEditChildItemListener(this);
        adapter.setRemoveGroupItemListener(this);
        adapter.setRemoveChildItemListener(this);
        adapter.setAddGroupItemListener(this);
        mView.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSelectedScheduleChanged(final Schedule schedule) {
        mCD.add(
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (mWeek.getId() == 1) {
                    mWeek = schedule.getFirstWeek();
                } else if (mWeek.getId() == 2) {
                    mWeek = schedule.getSecondWeek();
                }
                mView.updateAdapter(mWeek);
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }

    @Override
    public void onAddGroupItemClick(Day day) {
        Bundle bundle = new Bundle();
        bundle.putInt(CreateOrUpdateSubjectContract.DAY_ID, day.getId());
        bundle.putInt(CreateOrUpdateSubjectContract.WEEK_ID, day.getWeekId());
        mView.startActivity(bundle, CreateOrUpdateSubjectActivity.class);
    }
}
