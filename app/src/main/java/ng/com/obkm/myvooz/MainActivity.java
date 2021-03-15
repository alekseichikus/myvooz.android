package ng.com.obkm.myvooz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;
import com.yandex.authsdk.YandexAuthException;
import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;
import com.yandex.authsdk.YandexAuthToken;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi34;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi4;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.home.view.HomeFragment;
import ng.com.obkm.myvooz.model.AuthUser;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.note.NoteFragment;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.Auth.AuthDialogFragment;
import ng.com.obkm.myvooz.profile.NotificationsFragment;
import ng.com.obkm.myvooz.search.view.SettingsFragment;
import ng.com.obkm.myvooz.utils.DataBase.DBHelper;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AuthDialogFragment.BottomSheetListener {

    Handler handler = new Handler();
    HomeFragment fragment1 = new HomeFragment(this);
    SettingsFragment fragment2 = new SettingsFragment(this);
    NotificationsFragment fragment3 = new NotificationsFragment(this);
    NoteFragment fragment4;
    final FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
    Fragment active = fragment1;

    public static final String APP_PREFERENCES = "mysettings";
    public SharedPreferences mSettings;
    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;

    Integer id_older = 0;
    Integer id_group_of_user = 0;

    YandexAuthSdk sdk;

    Runnable showInfo = new Runnable() {
        public void run() {
            if(id_older != memoryOperation.getGroupOfUserIdOlderProfile() || id_group_of_user != memoryOperation.getIdGroupOfUser()){
                fragment3.isStateView();
                id_older = memoryOperation.getGroupOfUserIdOlderProfile();
                id_group_of_user = memoryOperation.getIdGroupOfUser();
            }
            handler.postDelayed(showInfo, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        sdk = new YandexAuthSdk( new YandexAuthOptions(this, true));

        memoryOperation = new MemoryOperation(this);

        createNotificationChannel();
        activityPresenter = new ActivityPresenter(this);

        DBHelper dbHelper = new DBHelper(this);


        for(String s : fingerprints){
            Log.d("grgr", s);
        }

        id_older = memoryOperation.getGroupOfUserIdOlderProfile();
        id_group_of_user = memoryOperation.getIdGroupOfUser();

        ArrayList<Note> notes = dbHelper.getData();
        fragment4 = new NoteFragment(this, notes, dbHelper);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(fragment1);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("aleks", "aleks", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        mSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            //msg = "Failed";
                        }
                        //Log.d("asasas", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        handler.postDelayed(showInfo, 1000);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "цуацуа";
            String description = "цуацуацуа";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("noti", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = fragment1;
                    break;
                case R.id.navigation_notif:
                    fragment = fragment2;
                    //fm.commit();
                    //fm.beginTransaction().replace(R.id.main_container, fragment2, "2");
                    //fm.beginTransaction().hide(active).show(fragment2).commit();
                    //active = fragment2;
                    break;
                case R.id.navigation_notifications:
                    fragment = fragment3;
                    break;
                case R.id.navigation_note:
                    fragment = fragment4;
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CheckForFullnessActivity.REQUEST_CODE_YA_LOGIN) {
            try {
                final YandexAuthToken yandexAuthToken = sdk.extractToken(resultCode, data);
                if (yandexAuthToken != null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.SITE_ADDRESS)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JsonPlaceHolderApi34 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi34.class);
                    Call<AuthUser> call = jsonPlaceHolderApi.getMyJSON(yandexAuthToken.getValue(), mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0)
                            , mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0)
                            , FirebaseInstanceId.getInstance().getToken());
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
                                    , data.getUniversityName(), data.getGroupId(), data.getGroupName(), data.getWeek(), data.getIdGroupOfUser()
                                    , data.getInfoGroupOfUser(), mSettings);
                            fragment3.isStateView();
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
        else {
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
                            JsonPlaceHolderApi4 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi4.class);
                            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                            Call<AuthUser> call = jsonPlaceHolderApi.getMyJSON(VKSdk.getAccessToken().accessToken,
                                    user.id, mSettings.getInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0)
                                    , mSettings.getInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, 0)
                                    , FirebaseInstanceId.getInstance().getToken());
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
                                            , data.getUniversityName(), data.getGroupId(), data.getGroupName(), data.getWeek(), data.getIdGroupOfUser()
                                            , data.getInfoGroupOfUser(), mSettings);
                                    fragment3.isStateView();
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

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10) {
            if(resultCode==RESULT_OK) {
                Note note = (Note) data.getSerializableExtra(Note.class.getSimpleName());
                fragment4.addNotes(note);
            }
        }

        if (resultCode == RESULT_OK) {
            if (data == null){
                return;
            }
            if(data.getSerializableExtra("CHANGE_USER_NOTE") != null){
                Note note = (Note) data.getSerializableExtra("CHANGE_USER_NOTE");
                fragment4.changeNote(note);
            }
            else if(requestCode == Constants.CHANGE_GROUP_DONT_GOU_PARAM_RESULT){
                String name_group = data.getStringExtra("NAME_GROUP");
                String name_university = data.getStringExtra("NAME_UNIVERSITY");
                Integer id_group = data.getIntExtra("ID_GROUP", 0);
                Integer id_university = data.getIntExtra("ID_UNIVERSITY", 0);

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, id_university);
                editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, id_group);
                editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, name_group);
                editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, name_university);
                editor.commit();
            }
            else if (data.getSerializableExtra("NAME_GROUP") != null) {
                String name_group = data.getStringExtra("NAME_GROUP");
                String name_university = data.getStringExtra("NAME_UNIVERSITY");
                Integer id_group = data.getIntExtra("ID_GROUP", 0);
                Integer id_university = data.getIntExtra("ID_UNIVERSITY", 0);

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, id_university);
                editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, id_group);
                editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, name_group);
                editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, name_university);
                editor.commit();
            }
        }
    }
}
