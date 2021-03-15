package ng.com.obkm.myvooz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.yandex.authsdk.YandexAuthException;
import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;
import com.yandex.authsdk.YandexAuthToken;

import java.util.HashSet;
import java.util.Set;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi12;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi33;
import ng.com.obkm.myvooz.Load.LoadActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.AuthUser;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckForFullnessActivity extends AppCompatActivity {
    private String[] scope = new String[]{VKScope.PAGES, VKScope.EMAIL};
    private Set<String> scope2 = new HashSet<String>();;
    public static Integer REQUEST_CODE_YA_LOGIN = 1;
    SharedPreferences mSettings;
    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;
    YandexAuthSdk sdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        CardView selectGroupButton = findViewById(R.id.select_group_button);
        CardView vkAuthButton = findViewById(R.id.vk_auth_button);
        activityPresenter = new ActivityPresenter(this);

        sdk = new YandexAuthSdk( new YandexAuthOptions(CheckForFullnessActivity.this, true));

        memoryOperation = new MemoryOperation(this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));

        mSettings = PreferenceManager.getDefaultSharedPreferences(CheckForFullnessActivity.this);
        selectGroupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckForFullnessActivity.this, SearchActivity.class);
                intent.putExtra("type", "universityCheck");
                startActivity(intent);
                Animatoo.animateSlideLeft(CheckForFullnessActivity.this);
            }
        });

        vkAuthButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                VKSdk.login(CheckForFullnessActivity.this, scope);
            }
        });

        CardView yaAuthButton = findViewById(R.id.ya_auth_button);
        yaAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(sdk.createLoginIntent(CheckForFullnessActivity.this, scope2), REQUEST_CODE_YA_LOGIN);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0) != 0){
            if(mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0) != 0){
                if(activityPresenter.isInternetConnection()){
                    if(!memoryOperation.getAccessToken().equals("")){
                        Intent intent = new Intent(CheckForFullnessActivity.this, LoadActivity.class);
                        startActivity(intent);
                        this.overridePendingTransition(0, 0);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(CheckForFullnessActivity.this, MainActivity.class);
                        startActivity(intent);
                        CheckForFullnessActivity.this.overridePendingTransition(0, 0);
                        finish();
                    }
                }
                else{
                    Intent intent = new Intent(CheckForFullnessActivity.this, MainActivity.class);
                    startActivity(intent);
                    CheckForFullnessActivity.this.overridePendingTransition(0, 0);
                    finish();
                }

                return;
            }
            Intent intent = new Intent(CheckForFullnessActivity.this, SearchActivity.class);
            intent.putExtra("type", "groupProfile");
            intent.putExtra("id_university", mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0));
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_YA_LOGIN) {
            try {
                final YandexAuthToken yandexAuthToken = sdk.extractToken(resultCode, data);
                if (yandexAuthToken != null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.SITE_ADDRESS)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JsonPlaceHolderApi33 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi33.class);
                    Call<AuthUser> call = jsonPlaceHolderApi.getMyJSON(yandexAuthToken.getValue(), FirebaseInstanceId.getInstance().getToken());
                    Log.d("berber", call.request().url().toString());
                    call.enqueue(new Callback<AuthUser>() {
                        @Override
                        public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                            if (!response.isSuccessful()) {
                                return;
                            }
                            AuthUser data = response.body();
                            activityPresenter.setAuthInfo(data.getFirstName(), data.getLastName(), data.getId(), data.getPhoto()
                                    , data.getRankName(), data.getRankId(), data.getStateProfile(), data.getAccessToken(), data.getUniversityId()
                                    , data.getUniversityName(), data.getGroupId(), data.getGroupName(), data.getWeek(), data.getIdGroupOfUser(),
                                    data.getInfoGroupOfUser(), mSettings);
                            if (data.getGroupId().equals(0)) {
                                Intent intent = new Intent(CheckForFullnessActivity.this, SearchActivity.class);
                                intent.putExtra("type", "universityCheck");
                                CheckForFullnessActivity.this.overridePendingTransition(0, 0);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(CheckForFullnessActivity.this, LoadActivity.class);
                                CheckForFullnessActivity.this.overridePendingTransition(0, 0);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onFailure(Call<AuthUser> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            } catch (YandexAuthException e) {
                // Process error
            }
            return;
        }
        else{
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    //Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_LONG).show();
                    VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "first_name,last_name"));
                    vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            VKApiUser user = ((VKList<VKApiUser>)response.parsedModel).get(0);

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Constants.SITE_ADDRESS)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            JsonPlaceHolderApi12 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi12.class);
                            Call<AuthUser> call = jsonPlaceHolderApi.getMyJSON(VKSdk.getAccessToken().accessToken, user.id, FirebaseInstanceId.getInstance().getToken());
                            Log.d("request", call.request().url().toString());
                            call.enqueue(new Callback<AuthUser>() {
                                @Override
                                public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                                    if (!response.isSuccessful()) {
                                        return;
                                    }
                                    AuthUser data = response.body();
                                    activityPresenter.setAuthInfo(data.getFirstName(), data.getLastName(), data.getId(), data.getPhoto()
                                            , data.getRankName(), data.getRankId(), data.getStateProfile(), data.getAccessToken(), data.getUniversityId()
                                            , data.getUniversityName(), data.getGroupId(), data.getGroupName(), data.getWeek()
                                            , data.getIdGroupOfUser(), data.getInfoGroupOfUser(), mSettings);
                                    if (data.getGroupId().equals(0)) {
                                        Intent intent = new Intent(CheckForFullnessActivity.this, SearchActivity.class);
                                        intent.putExtra("type", "universityCheck");
                                        CheckForFullnessActivity.this.overridePendingTransition(0, 0);
                                        startActivity(intent);
                                    }
                                    else{
                                        Intent intent = new Intent(CheckForFullnessActivity.this, LoadActivity.class);
                                        CheckForFullnessActivity.this.overridePendingTransition(0, 0);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                @Override
                                public void onFailure(Call<AuthUser> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                    });
                }
                @Override
                public void onError(VKError error) {
                    //Toast.makeText(getApplicationContext(), "Jib,rf", Toast.LENGTH_LONG).show();
                }
            })) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}