package ng.com.obkm.myvooz.profile.JoinGroup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi41;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.IniviteData;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinGroupActivity extends AppCompatActivity{

    CardView backButton;
    CardView inviteButton;
    LinearLayout privacyPolicyButton;
    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;
    EditText inviteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        initUI();
        setListeners();

        activityPresenter = new ActivityPresenter(this);
        memoryOperation = new MemoryOperation(this);
        activityPresenter.setLightStatusBar(JoinGroupActivity.this);
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        inviteEditText = findViewById(R.id.invite_text);
        inviteButton = findViewById(R.id.invite_button);
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                inviteData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void inviteData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi41 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi41.class);
        Call<IniviteData> call = jsonPlaceHolderApi.getMyJSON(memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), inviteEditText.getText().toString());
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<IniviteData>() {
            @Override
            public void onResponse(Call<IniviteData> call, Response<IniviteData> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                IniviteData posts = response.body();

                if(posts.getErrorType() == IniviteData.Errors.ERROR_IS_NOT_EXIST){
                    Toast.makeText(JoinGroupActivity.this, "Вы вступили в группу!", Toast.LENGTH_LONG).show();
                    memoryOperation.setGroupOfUserNameProfile(posts.getGroupOfUser().getName());
                    memoryOperation.setGroupPhotoProfile(posts.getGroupOfUser().getImage());
                    memoryOperation.setIdGroupOfUser(posts.getGroupOfUser().getId());
                    memoryOperation.setGroupOfUserCountUsersProfile(posts.getGroupOfUser().getCountUsers());

                    SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(JoinGroupActivity.this);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, posts.getInfoGroup().getIdUniversity());
                    editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, posts.getInfoGroup().getIdGroup());
                    editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, posts.getInfoGroup().getNameGroup());
                    editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, posts.getInfoGroup().getNameUniversity());
                    editor.commit();
                    finish();
                }
                else if(posts.getErrorType() == IniviteData.Errors.ERROR_LINK_IS_NOT_VALID){
                    Toast.makeText(JoinGroupActivity.this, "Срок действия ссылки-приглашения истекло", Toast.LENGTH_LONG).show();
                }
                else if(posts.getErrorType() == IniviteData.Errors.ERROE_LINK_DOES_NOT_EXIST){
                    Toast.makeText(JoinGroupActivity.this, "Такой ссылки-приглашения не существует", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<IniviteData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
