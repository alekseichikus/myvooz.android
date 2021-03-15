package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.Note;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi29 {

    @GET("profile?type=add_user_note")
    Call<Note> getMyJSON(@Query("access_token") String access_token, @Query("user_id") Integer user_id, @Query("id_object") Integer id_object
            , @Query("title") String title, @Query("text") String text, @Query("date") String date, @Query("mark_me") Integer mark_me
            , @Query("images[]") ArrayList<Integer> images);
}
