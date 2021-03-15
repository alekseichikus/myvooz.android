package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.ItemSearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi8 {

    @GET("profile?type=full_search")
    Call<ArrayList<ItemSearch>> getMyJSON(@Query("text") String text, @Query("id_university") Integer id_university);
}
