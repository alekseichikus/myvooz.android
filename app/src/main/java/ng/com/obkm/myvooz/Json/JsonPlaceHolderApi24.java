package ng.com.obkm.myvooz.Json;

import java.util.ArrayList;

import ng.com.obkm.myvooz.home.notification.model.Notification;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi24 {
    @GET("profile?type=user_notifications")
    Call<ArrayList<Notification>> getMyJSON(@Query("id_university") Integer id_university);
}
