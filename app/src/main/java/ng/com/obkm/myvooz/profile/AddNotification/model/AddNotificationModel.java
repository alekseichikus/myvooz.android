package ng.com.obkm.myvooz.profile.AddNotification.model;

import android.util.Log;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi21;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNotificationModel implements IAddNotificationModel {

    @Override
    public void addNotification(OnFinishedListener onFinishedListener, String access_token, Integer id_user, String text, ArrayList<Integer> images, ArrayList<Integer> users, Boolean important) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi21 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi21.class);

        Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, 3, text, images, users, important ? 1 : 0);
        Log.d("asas", call.request().url().toString());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Boolean state = response.body();
                onFinishedListener.onFinished(state);
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
            }
        });
    }
    @Override
    public void uploadFile(String filepath) {

    }


}

