package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.model.AuthUser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi33 {
    @GET("profile?type=auth_ya")
    Call<AuthUser> getMyJSON(@Query("access_token") String access_token, @Query("key_notif") String key_notif);
}
