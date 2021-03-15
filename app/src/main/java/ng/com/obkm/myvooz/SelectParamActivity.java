package ng.com.obkm.myvooz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi39;
import ng.com.obkm.myvooz.R;
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

public class SelectParamActivity extends AppCompatActivity {
    SharedPreferences mSettings;

    int id_group = 0;
    int id_university = 0;
    String name_group = "";
    String name_university = "";
    TextView groupName;
    TextView universityName;
    RelativeLayout groupSelect;
    MemoryOperation memoryOperation;
    IActivityPresenter activityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_param_activity);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));

        activityPresenter = new ActivityPresenter(this);

        memoryOperation = new MemoryOperation(this);

        groupName = findViewById(R.id.group_name);
        universityName = findViewById(R.id.university_name);

        CardView backButton = findViewById(R.id.back_button);
        RelativeLayout universitySelect = findViewById(R.id.university_select);
        groupSelect = findViewById(R.id.group_select);
        CardView sendConfirm = findViewById(R.id.send_confirm);

        universitySelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectParamActivity.this, SearchActivity.class);
                intent.putExtra("type", "university_intermediate_value");
                startActivity(intent);
            }
        });

        groupSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectParamActivity.this, SearchActivity.class);
                intent.putExtra("type", "group_intermediate_value");
                intent.putExtra("id_university", mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, 0));
                startActivity(intent);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!memoryOperation.getAccessToken().equals("")){
                    sendData();
                }
                else{
                    if(mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0) != 0) {
                        if (mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0) != mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0)
                                || mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, 0) != mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0)) {
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE,  mSettings.getString(Constants.APP_PREFERENCES_GROUP_NAME_INTERMEDIATE_VALUE, ""));
                            editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0));
                            editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE,  mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE, ""));
                            editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, 0));
                            editor.commit();
                            finish();
                        }
                        else{
                            Toast.makeText(SelectParamActivity.this, "Данные не изменились", Toast.LENGTH_SHORT).show();
                            final Animation animShake = AnimationUtils.loadAnimation(SelectParamActivity.this, R.anim.anim_shake);
                            groupSelect.startAnimation(animShake);
                        }
                    }
                    else{
                        Toast.makeText(SelectParamActivity.this, "Выберите группу", Toast.LENGTH_SHORT).show();
                        final Animation animShake = AnimationUtils.loadAnimation(SelectParamActivity.this, R.anim.anim_shake);
                        groupSelect.startAnimation(animShake);
                    }
                }
            }
        });

        mSettings = PreferenceManager.getDefaultSharedPreferences(SelectParamActivity.this);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0));
        editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0));
        editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_INTERMEDIATE_VALUE, mSettings.getString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, ""));
        editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE, mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, ""));
        editor.commit();

        id_university = mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0);
        name_university = mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, "");
        id_group = mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0);
        name_group = mSettings.getString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = mSettings.edit();
        if(id_university != mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, 0)){
            editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0);
            groupName.setText("Не выбрано");
            editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_INTERMEDIATE_VALUE, "Не выбрано");
            universityName.setText(mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE, ""));

            id_university = mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, 0);
            name_university = mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE, "");
            id_group = 0;
        }
        else{
            groupName.setText(mSettings.getString(Constants.APP_PREFERENCES_GROUP_NAME_INTERMEDIATE_VALUE, ""));
            universityName.setText(mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE, ""));
            name_university = mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE, "");
            name_group = mSettings.getString(Constants.APP_PREFERENCES_GROUP_NAME_INTERMEDIATE_VALUE, "");
            id_group = mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0);
        }
        editor.commit();
    }

    public void sendData(){
        if(mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0) != 0){
            if(mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0) != mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0)
                    || mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, 0) != mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0)){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.SITE_ADDRESS)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi39 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi39.class);
                Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, ""), mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0), mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, 0));
                Log.d("fedwedwe", call.request().url().toString());
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        Boolean data = response.body();

                        if(data){
                            Intent intent = new Intent();
                            intent.putExtra("NAME_UNIVERSITY", name_university);
                            intent.putExtra("NAME_GROUP", name_group);
                            intent.putExtra("ID_GROUP", id_group);
                            intent.putExtra("ID_UNIVERSITY", id_university);

                            memoryOperation.setVersionSchedule(-1);
                            DBScheduleGroup dbScheduleGroup = new DBScheduleGroup(SelectParamActivity.this);
                            dbScheduleGroup.deleteAllSchedules();

                            setResult(RESULT_OK, intent);
                            onBackPressed();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            else{
                Toast.makeText(SelectParamActivity.this, "Данные не изменились", Toast.LENGTH_SHORT).show();
                final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
                groupSelect.startAnimation(animShake);
            }
        }
        else{
            Toast.makeText(SelectParamActivity.this, "Выберите группу", Toast.LENGTH_SHORT).show();
            final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
            groupSelect.startAnimation(animShake);
        }
    }
}
