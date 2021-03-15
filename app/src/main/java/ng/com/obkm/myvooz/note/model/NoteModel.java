package ng.com.obkm.myvooz.note.model;

import android.util.Log;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi25;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi26;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi30;
import ng.com.obkm.myvooz.model.NoteFull;
import ng.com.obkm.myvooz.model.ResponseStatus;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class NoteModel implements INoteModel {

    @Override
    public void getItems(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi25 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi25.class);

        Call<NoteFull> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, type);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<NoteFull>() {
            @Override
            public void onResponse(Call<NoteFull> call, Response<NoteFull> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                NoteFull posts = response.body();
                onFinishedListener.onFinished(posts, type);
            }
            @Override
            public void onFailure(Call<NoteFull> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
            }
        });
    }

    @Override
    public void completedItems(OnFinishedSendData onFinishedSendData, String access_token, Integer id_user, ArrayList<Integer> notes) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi30 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi30.class);

        Call<ResponseStatus> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, notes);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ResponseStatus posts = response.body();
                onFinishedSendData.onFinishedSend(posts.getState(), notes);
            }
            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                t.printStackTrace();
                onFinishedSendData.onFailure(t);
            }
        });
    }

    @Override
    public void deleteItems(OnFinishedDeleteSendData onFinishedSendData, String access_token, Integer id_user, ArrayList<Integer> notes) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi26 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi26.class);

        Call<ResponseStatus> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, notes);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ResponseStatus posts = response.body();
                onFinishedSendData.onFinishedDeleteSend(posts.getState(), notes);
            }
            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                t.printStackTrace();
                onFinishedSendData.onFailure(t);
            }
        });
    }
}
