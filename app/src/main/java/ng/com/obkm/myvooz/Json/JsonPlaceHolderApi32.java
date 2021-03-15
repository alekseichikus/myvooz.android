package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.DaySchedule;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi32 {

    @GET("profile?type=get_group_schedule_list")
    Call<ArrayList<DaySchedule>> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id);
}
