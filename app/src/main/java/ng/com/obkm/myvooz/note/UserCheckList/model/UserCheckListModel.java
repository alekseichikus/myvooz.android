package ng.com.obkm.myvooz.note.UserCheckList.model;

import android.util.Log;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi27;
import ng.com.obkm.myvooz.model.UsersListData;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserCheckListModel implements IUserCheckListModel {

    @Override
    public void getItems(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer id_after_user_select) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi27 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi27.class);
        Call<UsersListData> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, id_after_user_select);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<UsersListData>() {
            @Override
            public void onResponse(Call<UsersListData> call, Response<UsersListData> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                UsersListData posts = response.body();
                onFinishedListener.onFinished(posts);
            }
            @Override
            public void onFailure(Call<UsersListData> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
            }
        });
    }
}
