package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.model.NoteFull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi25 {
    @GET("profile?type=user_note")
    Call<NoteFull> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("type_n") Integer type);
}
