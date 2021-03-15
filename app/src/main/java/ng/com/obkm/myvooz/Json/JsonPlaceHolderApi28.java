package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.ItemSearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi28 {

    @GET("profile?type=objects")
    Call<ArrayList<ItemSearch>> getMyJSON(@Query("text") String text, @Query("id_university") Integer id_university);
}
