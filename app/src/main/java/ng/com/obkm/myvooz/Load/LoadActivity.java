package ng.com.obkm.myvooz.Load;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi32;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi4;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.AuthUser;
import ng.com.obkm.myvooz.model.DaySchedule;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.DataBase.DBScheduleGroup;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadActivity extends AppCompatActivity {
    SharedPreferences mSettings;

    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        mSettings = PreferenceManager.getDefaultSharedPreferences(LoadActivity.this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));
        memoryOperation = new MemoryOperation(this);

        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        ImageView rotateImage = findViewById(R.id.rotate_image);
        rotateImage.startAnimation(rotate);

        activityPresenter = new ActivityPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi4 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi4.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(LoadActivity.this);
        Call<AuthUser> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "")
                , mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0), mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0)
                , mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0), FirebaseInstanceId.getInstance().getToken());
        Log.d("revererg", call.request().url().toString());
        call.enqueue(new Callback<AuthUser>() {
            @Override
            public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                AuthUser data = response.body();

                if(data.getError().equals(1)){
                    activityPresenter.deleteAuthData(mSettings);
                    Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoadActivity.this.overridePendingTransition(0, 0);
                    finish();
                }
                else{
                    if(!memoryOperation.getAccessToken().equals("") && !memoryOperation.getIdGroupOfUser().equals(0)){
                        if(data.getVersionSchedule() != memoryOperation.getVersionSchedule()){

                            memoryOperation.setVersionSchedule(data.getVersionSchedule());

                            TextView updateText = findViewById(R.id.update_schedule);
                            updateText.setText("Скачиваю расписание");

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Constants.SITE_ADDRESS)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            JsonPlaceHolderApi32 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi32.class);
                            Call<ArrayList<DaySchedule>> call2 = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "")
                                    , mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0));
                            Log.d("request", call2.request().url().toString());
                            call2.enqueue(new Callback<ArrayList<DaySchedule>>() {
                                @Override
                                public void onResponse(Call<ArrayList<DaySchedule>> call2, Response<ArrayList<DaySchedule>> response2) {
                                    if (!response2.isSuccessful()) {
                                        return;
                                    }

                                    updateText.setText("Обновляю расписание");
                                    ArrayList<DaySchedule> posts = response2.body();
                                    DBScheduleGroup dbScheduleGroup = new DBScheduleGroup(LoadActivity.this);
                                    dbScheduleGroup.deleteAllSchedules();
                                    for(DaySchedule schedule : posts){
                                        dbScheduleGroup.addSchedule(schedule);
                                    }

                                    Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                                    LoadActivity.this.overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    finish();
                                }
                                @Override
                                public void onFailure(Call<ArrayList<DaySchedule>> call2, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                        else{
                            activityPresenter.setAuthInfo(data.getFirstName(), data.getLastName(), data.getId(), data.getPhoto()
                                    , data.getRankName(), data.getRankId(), data.getStateProfile(), data.getAccessToken(), data.getUniversityId()
                                    , data.getUniversityName(), data.getGroupId(), data.getGroupName(), data.getWeek(), data.getIdGroupOfUser()
                                    , data.getInfoGroupOfUser(), mSettings);
                            Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoadActivity.this.overridePendingTransition(0, 0);
                            finish();
                        }
                    }
                    else{
                        activityPresenter.setAuthInfo(data.getFirstName(), data.getLastName(), data.getId(), data.getPhoto()
                                , data.getRankName(), data.getRankId(), data.getStateProfile(), data.getAccessToken(), data.getUniversityId()
                                , data.getUniversityName(), data.getGroupId(), data.getGroupName(), data.getWeek(), data.getIdGroupOfUser()
                                , data.getInfoGroupOfUser(), mSettings);
                        Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoadActivity.this.overridePendingTransition(0, 0);
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<AuthUser> call, Throwable t) {
                activityPresenter.deleteAuthData(mSettings);
                t.printStackTrace();
            }
        });
    }
}
