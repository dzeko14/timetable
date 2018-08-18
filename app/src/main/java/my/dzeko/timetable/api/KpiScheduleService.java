package my.dzeko.timetable.api;

import io.reactivex.Observable;
import my.dzeko.timetable.models.ApiRespond;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface KpiScheduleService {
    @GET("{group_name}/lessons")
    Observable<ApiRespond> getGroupSchedule(@Path("group_name") String groupName);
}
