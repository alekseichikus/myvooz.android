package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.schedule.WeeksSchedule;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi3 {

    @GET("profile?type=week")
    Call<WeeksSchedule> getMyJSON(@Query("id_group") String id_group, @Query("week") Integer week);
}
