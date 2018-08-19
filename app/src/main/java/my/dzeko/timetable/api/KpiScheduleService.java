package my.dzeko.timetable.api;

import io.reactivex.Single;
import my.dzeko.timetable.entities.ApiRespond;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface KpiScheduleService {
    @GET("{group_name}/lessons")
    Single<ApiRespond> getGroupSchedule(@Path("group_name") String groupName);
}
