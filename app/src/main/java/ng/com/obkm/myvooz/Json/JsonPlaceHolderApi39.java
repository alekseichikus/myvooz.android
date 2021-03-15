package ng.com.obkm.myvooz.Json;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi39 {
    @GET("profile?type=change_group_of_user_id")
    Call<Boolean> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("id_group") Integer id_group);
}
