package ng.com.obkm.myvooz.search.model;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi8;
import ng.com.obkm.myvooz.model.ItemSearch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class SearchModel implements ISearchModel {

    @Override
    public void getItems(OnFinishedListener onFinishedListener, Integer id_university, String text) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi8 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi8.class);
        Call<ArrayList<ItemSearch>> call = jsonPlaceHolderApi.getMyJSON(text, id_university);
        call.enqueue(new Callback<ArrayList<ItemSearch>>() {
            @Override
            public void onResponse(Call<ArrayList<ItemSearch>> call, Response<ArrayList<ItemSearch>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ArrayList<ItemSearch> posts = response.body();
                onFinishedListener.onFinished(posts);
            }
            @Override
            public void onFailure(Call<ArrayList<ItemSearch>> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
            }
        });
    }
}
