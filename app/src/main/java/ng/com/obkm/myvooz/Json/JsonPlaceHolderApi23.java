package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.Classroom;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi23 {
    @GET("profile?type=classroom")
    Call<ArrayList<ArrayList<Classroom>>> getMyJSON(@Query("date") String date, @Query("id_corpus") Integer id_corpus, @Query("low_number") Integer low_number, @Query("upper_number") Integer upper_number);
}
