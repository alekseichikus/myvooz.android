package ng.com.obkm.myvooz.Json;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi35 {
    @GET("profile?type=edit_account")
    Call<Boolean> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("first_name") String first_name, @Query("last_name") String last_name);
}
