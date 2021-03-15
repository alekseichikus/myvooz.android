package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.model.AuthUser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi34 {
    @GET("profile?type=auth_2_ya")
    Call<AuthUser> getMyJSON(@Query("access_token") String access_token, @Query("id_university") Integer id_university, @Query("id_group") Integer id_group, @Query("key_notif") String key_notif);
}
