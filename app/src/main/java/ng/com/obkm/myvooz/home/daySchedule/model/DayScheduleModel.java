package ng.com.obkm.myvooz.home.daySchedule.model;

import android.util.Log;

import java.util.Calendar;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi2;
import ng.com.obkm.myvooz.Adapter.WeekSchedule;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DayScheduleModel implements IDayScheduleModel {
    @Override
    public void getScheduleDay(OnFinishedListener onFinishedListener, Integer id_group, Calendar calendar) {
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi2 jsonPlaceHolderApi1 = retrofit1.create(JsonPlaceHolderApi2.class);
        Call<WeekSchedule> call1 = jsonPlaceHolderApi1.getMyJSON(id_group.toString(), calendar.get(Calendar.WEEK_OF_YEAR), getNumberOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
        Log.d("request", call1.request().url().toString());
        call1.enqueue(new Callback<WeekSchedule>() {
            @Override
            public void onResponse(Call<WeekSchedule> call, Response<WeekSchedule> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                WeekSchedule posts = response.body();
                WeekSchedule weekSchedule = posts;
                onFinishedListener.onFinished(weekSchedule);
            }
            @Override
            public void onFailure(Call<WeekSchedule> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public Integer getNumberOfWeek(Integer now){
        if(now == 1)
            return 6;
        else if(now == 2){
            return 0;
        }
        else if(now == 3){
            return 1;
        }
        else if(now == 4){
            return 2;
        }
        else if(now == 5){
            return 3;
        }
        else if(now == 6){
            return 4;
        }
        else return 5;
    }
}
