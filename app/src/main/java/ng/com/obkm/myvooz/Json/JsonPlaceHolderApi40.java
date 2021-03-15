package ng.com.obkm.myvooz.Json;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi40 {
    @GET("profile?type=logout_group_of_user")
    Call<Boolean> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id);
}
