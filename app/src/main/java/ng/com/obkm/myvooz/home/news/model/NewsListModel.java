package ng.com.obkm.myvooz.home.news.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi;
import ng.com.obkm.myvooz.model.News;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsListModel implements INewsListModel {
    @Override
    public void getNewsList(OnFinishedListener onFinishedListener, Integer id_group) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<ArrayList<News>> call = jsonPlaceHolderApi.getPosts(id_group);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<ArrayList<News>>() {
            @Override
            public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<News> posts = response.body();
                onFinishedListener.onFinished(posts);
            }
            @Override
            public void onFailure(Call<ArrayList<News>> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
            }
        });
    }
}
