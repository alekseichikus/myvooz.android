package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.ResponseStatus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi30 {
    @GET("profile?type=completed_user_note")
    Call<ResponseStatus> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("notes[]") ArrayList<Integer> notes);
}
