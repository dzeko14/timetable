package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.contracts.CalendarDetailContract;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.utils.DateUtils;
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper;

public class CalendarDetailPresenter implements CalendarDetailContract.Presenter {
    private CalendarDetailContract.View mView;
    private IModel mModel;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public CalendarDetailPresenter(CalendarDetailContract.View view) {
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
        mCompositeDisposable.dispose();
    }

    @SuppressLint("CheckResult")
    @Override
    public void findRespectSubjects(final Date date) {
        mCompositeDisposable.add(
        getSubjects(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Subject>>() {
                    @Override
                    public void onSuccess(List<Subject> subjects) {
                        mView.setupSubjects(subjects);
                        mView.setActionBarTitle(DateUtils.getdateInString(date));
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    private Single<List<Subject>> getSubjects(final Date date) {
        return Single.just(mModel).map(new Function<IModel, List<Subject>>() {
                    @Override
                    public List<Subject> apply(IModel model) throws Exception {
                        boolean isSingleWeekSchedule = mModel.getIsSelectedScheduleSingleWeek();
                        if (!isSingleWeekSchedule) {
                            int day = DateUtils.getDayOfWeekFromDate(date);
                            int week = DateUtils.getWeekNumberFromDate(date,
                                    SharedPreferencesWrapper.getInstance().getKeyDate());
                            return mModel.getSubjectsByDayIdAndWeekId(day,
                                    week,
                                    mModel.getSelectedScheduleGroupName());
                        } else {
                            int day = DateUtils.getDayOfWeekFromDate(date);
                            return mModel.getSubjectsByDayId(day, mModel.getSelectedScheduleGroupName());
                        }
                    }
                });
    }
}
