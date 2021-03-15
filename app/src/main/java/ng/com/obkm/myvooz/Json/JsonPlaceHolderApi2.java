package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.Adapter.WeekSchedule;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi2 {

    @GET("profile?type=day_schedule")
    Call<WeekSchedule> getMyJSON(@Query("id_group") String id_group, @Query("week") Integer week, @Query("day") Integer day);
}
