package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.adapters.EditWeekExpandableListAdapter;
import my.dzeko.timetable.contracts.EditWeekContract;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;

public class EditWeekPresenter implements EditWeekContract.Presenter {
    private EditWeekContract.View mView;
    private IModel mModel = Model.getInstance();
    private List<Day> mWeek;

    public EditWeekPresenter(EditWeekContract.View view) {
        mView = view;
    }

    @Override
    public boolean onUserClick(int itemId) {
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void onEditChildItemClick(Subject subject) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void onRemoveChildItemClick(final Subject subject) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.removeSubject(subject);
                for (int i = 0; i < mWeek.size(); i++) {
                    Day day = mWeek.get(i);

                    if (day.getId() == subject.getDayId()){
                        day.getSubjects().remove(subject);
                    }

                    if (day.getSubjects().size() == 0) {
                        mWeek.remove(day);
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
                mWeek.remove(day);
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
    public void setWeek(List<Day> week) {
        mWeek = week;

    }

    private EditWeekExpandableListAdapter getAdapterFromSchedule(List<Day> schedule) {
        return new EditWeekExpandableListAdapter(mView.getContext(), schedule);
    }

    @Override
    public void onViewInitialized() {
        EditWeekExpandableListAdapter adapter = getAdapterFromSchedule(mWeek);
        adapter.setEditChildItemListener(this);
        adapter.setRemoveGroupItemListener(this);
        adapter.setRemoveChildItemListener(this);
        mView.setAdapter(adapter);
    }
}
