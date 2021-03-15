package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.model.UsersListData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi27 {
    @GET("profile?type=users_list")
    Call<UsersListData> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("sel_id") Integer sel_id);
}
