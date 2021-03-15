package ng.com.obkm.myvooz.home.notification.model;

import android.util.Log;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi20;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi24;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi46;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class UserNotificationModel implements IUserNotificationModel {

    @Override
    public void getUserNotificationList(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi20 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi20.class);
        Call<ArrayList<Notification>> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, type);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ArrayList<Notification> posts = response.body();
                onFinishedListener.onFinished(posts);
            }
            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
                Log.d("bevecve", call.request().url().toString());
            }
        });
    }

    @Override
    public void getUserWithUniversityNotificationList(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer type, Integer id_group) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi46 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi46.class);
        Call<ArrayList<Notification>> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, type, id_group);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ArrayList<Notification> posts = response.body();
                onFinishedListener.onFinished(posts);
            }
            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
                Log.d("bevecve", call.request().url().toString());
            }
        });
    }

    @Override
    public void getUniversityNotificationList(OnFinishedListener onFinishedListener, Integer id_university) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi24 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi24.class);
        Call<ArrayList<Notification>> call = jsonPlaceHolderApi.getMyJSON(id_university);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ArrayList<Notification> posts = response.body();
                onFinishedListener.onFinished(posts);
            }
            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
            }
        });
    }
}
