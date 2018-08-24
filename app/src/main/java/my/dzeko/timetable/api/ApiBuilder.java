package my.dzeko.timetable.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import my.dzeko.timetable.entities.ApiRespond;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiBuilder {
    public static final String API_URL = "http://api.rozklad.org.ua/v2/groups/";

    public static Observable<ApiRespond> buildScheduleServiceObservable(final String groupName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        KpiScheduleService service = retrofit.create(KpiScheduleService.class);

        return service.getGroupSchedule(groupName);
    }
}
