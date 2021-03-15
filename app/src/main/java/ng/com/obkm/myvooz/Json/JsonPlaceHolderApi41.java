package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.model.IniviteData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi41 {
    @GET("profile?type=invite_group_of_user_code")
    Call<IniviteData> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("text") String text);
}
