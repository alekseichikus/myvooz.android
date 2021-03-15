package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.model.IniviteData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi42 {
    @GET("profile?type=get_group_of_user")
    Call<IniviteData> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id);
}
