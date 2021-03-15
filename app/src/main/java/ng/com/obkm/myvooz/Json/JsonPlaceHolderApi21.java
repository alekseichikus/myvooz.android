package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi21 {
    @GET("profile?type=add_user_notification")
    Call<Boolean> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("type_n") Integer type, @Query("text") String text, @Query("images[]") ArrayList<Integer> images, @Query("users[]") ArrayList<Integer> users, @Query("important_state") Integer important_state);
}
