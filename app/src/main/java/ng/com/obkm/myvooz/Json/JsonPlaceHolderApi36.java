package ng.com.obkm.myvooz.Json;

import ng.com.obkm.myvooz.model.EntryLink;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi36 {
    @GET("profile?type=get_entry_link")
    Call<EntryLink> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id);
}
