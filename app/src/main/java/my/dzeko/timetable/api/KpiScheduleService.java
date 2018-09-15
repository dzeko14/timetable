package my.dzeko.timetable.api;

import io.reactivex.Observable;
import io.reactivex.Single;
import my.dzeko.timetable.entities.ScheduleApiRespond;
import my.dzeko.timetable.entities.WeekApiRespond;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface KpiScheduleService {
    @GET("groups/{group_name}/lessons")
    Call<ScheduleApiRespond> getGroupSchedule(@Path("group_name") String groupName);

    @GET("weeks")
    Call<WeekApiRespond> getCurrentWeek();
}
