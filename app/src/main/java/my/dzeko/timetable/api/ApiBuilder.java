package my.dzeko.timetable.api;


import my.dzeko.timetable.entities.ScheduleApiRespond;
import my.dzeko.timetable.entities.WeekApiRespond;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiBuilder {
    public static final String API_URL = "http://api.rozklad.org.ua/v2/";

    public static Call<ScheduleApiRespond> buildGetGroupScheduleServiceObservable(final String groupName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KpiScheduleService service = retrofit.create(KpiScheduleService.class);

        return service.getGroupSchedule(groupName);
    }

    public static Call<WeekApiRespond> buildGetCurrentWeekServiceObservable(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KpiScheduleService service = retrofit.create(KpiScheduleService.class);

        return service.getCurrentWeek();
    }
}
