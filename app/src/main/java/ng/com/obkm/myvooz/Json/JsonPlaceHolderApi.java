package ng.com.obkm.myvooz.Json;



import java.util.ArrayList;

import ng.com.obkm.myvooz.model.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("profile?type=news")
    Call<ArrayList<News>> getPosts(@Query("id_group") Integer id_group);
}


