package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.CreateOrUpdateSubjectContract;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;

public class CreateOrUpdateSubjectPresenter implements CreateOrUpdateSubjectContract.Presenter {
    private CreateOrUpdateSubjectContract.View mView;
    private IModel mModel = Model.getInstance();

    private Disposable mGetScheduleFromDBDisposable;

    private Subject mSubject;
    private int mDayId;
    private int mWeekId;

    private boolean mIsStateRestored = false;

    private static final String SUBJECT_IN_PRESENTER = "subject in presenter";
    private static final String DAY_ID = "day id";
    private static final String WEEK_ID = "week id";

    public CreateOrUpdateSubjectPresenter(CreateOrUpdateSubjectContract.View view) {
        mView = view;
    }

    @Override
    public boolean onUserClick(int itemId) {
        switch (itemId) {
            case R.id.submit_button:
                mView.saveData();
                return true;
        }
        return false;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;

        mIsStateRestored = true;

        mSubject = savedInstanceState.getParcelable(SUBJECT_IN_PRESENTER);
        mDayId = savedInstanceState.getInt(DAY_ID);
        mWeekId = savedInstanceState.getInt(WEEK_ID);
    }

    @Override
    public Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUBJECT_IN_PRESENTER, (Parcelable) mSubject);
        bundle.putInt(DAY_ID, mDayId);
        bundle.putInt(WEEK_ID, mWeekId);
        return bundle;
    }

    @Override
    public void destroy() {
        mView = null;
        if (mGetScheduleFromDBDisposable != null) {
            mGetScheduleFromDBDisposable.dispose();
        }
    }

    @Override
    public void onDataSaving(String name, String fullName, String cabinet, String teacher,
                             int position, String type) {
        if (TextUtils.isEmpty(name)) return;

        if (mSubject == null) {
            createSubject(name, fullName, cabinet, teacher, position, type);
        } else {
            updateSubject(name, fullName, cabinet, teacher, position, type);
        }
    }

    @SuppressLint("CheckResult")
    private void updateSubject(final String name, final String fullName, final String cabinet,
                               final String teacher, final int position, final String type) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mSubject.setSubjectName(name);

                if (!TextUtils.isEmpty(fullName)) mSubject.setFullSubjectName(fullName);
                else mSubject.setFullSubjectName(name);

                if (!TextUtils.isEmpty(cabinet)) mSubject.setCabinet(cabinet);

                if (!TextUtils.isEmpty(teacher)) mSubject.setTeacher(teacher);

                mSubject.setType(type);

                mSubject.setPosition(position);
                mModel.saveSubject(mSubject);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new DBSavingObserver(){
                    @Override
                    public void onError(Throwable e) {
                        //TODO: Implement onError
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void createSubject(final String name, final String fullName, final String cabinet,
                               final String teacher, final int position, final String type) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Subject subject = new Subject(
                        mDayId,
                        mWeekId,
                        mModel.getSelectedScheduleGroupName(),
                        name,
                        (TextUtils.isEmpty(fullName) ? name : fullName),
                        cabinet,
                        teacher,
                        position,
                        type
                );
                mModel.saveSubject(subject);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new DBSavingObserver(){
                    @Override
                    public void onError(Throwable e) {
                        //TODO: Implement onError
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onDataReceived(int dayId, int weekId, long subjectId) {
        if (mIsStateRestored) return;
        mDayId = dayId;
        mWeekId = weekId;

        if (subjectId != -1) {
            mView.showLoading();
            loadSubjectFromDB(subjectId);
        }
    }

    private void loadSubjectFromDB(final long subjectId) {
        mGetScheduleFromDBDisposable = Single.just(mModel)
                .map(new Function<IModel, Subject>() {
                    @Override
                    public Subject apply(IModel model) throws Exception {
                        mSubject = model.getSubject(subjectId);
                        return mSubject;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Subject>() {
                    @Override
                    public void onSuccess(Subject subject) {
                        mView.setSubject(subject);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                    }
                });
    }

    private static abstract class DBSavingObserver implements CompletableObserver {

        @Override
        public void onSubscribe(Disposable d) {
            //not uses
        }

        @Override
        public void onComplete() {
            //not uses
        }
    }
}
