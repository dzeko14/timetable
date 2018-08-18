package my.dzeko.timetable.api;

import android.annotation.SuppressLint;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.models.ApiRespond;
import my.dzeko.timetable.models.Schedule;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.utils.ScheduleUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiBuilder {
    public static final String API_URL = "http://api.rozklad.org.ua/v2/groups/";

    @SuppressLint("CheckResult")
    public static Observable<Schedule> buildScheduleServiceCompletable(final String groupName) {
        Retrofit retrofit = buildRetrofit();
        KpiScheduleService service = retrofit.create(KpiScheduleService.class);
        Observable<ApiRespond> schedule = service.getGroupSchedule(groupName);

        Observable<Schedule> observable = schedule.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ApiRespond, Schedule>() {
                    @Override
                    public Schedule apply(ApiRespond apiRespond) throws Exception {
                        return ScheduleUtils.fetchSchedule(apiRespond, groupName);
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Schedule, Schedule>() {
                    @Override
                    public Schedule apply(Schedule schedule) throws Exception {
                        ScheduleObservable.getInstance().notifySelectedScheduleChanged(schedule);
                        return schedule;
                    }
                });
        return observable;
    }

    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
