package ng.com.obkm.myvooz.profile.CreateGroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi44;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.SelectParamActivity;
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

public class CreateGroupActivity extends AppCompatActivity{

    CardView backButton;
    RelativeLayout selectGroup;
    LinearLayout privacyPolicyButton;
    EditText groupNameEditText;
    TextView selectGroupNameTextView;
    CardView createGroupButton;
    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;
    Integer id_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        memoryOperation = new MemoryOperation(this);

        initUI();
        setListeners();

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(CreateGroupActivity.this);

        id_group = memoryOperation.getIdGroup();

        groupNameEditText.setText("Группа " + memoryOperation.getNameGroupProfile());
        selectGroupNameTextView.setText(memoryOperation.getNameGroupProfile());
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        groupNameEditText = findViewById(R.id.group_name);
        selectGroupNameTextView = findViewById(R.id.select_university_group_select);
        selectGroup = findViewById(R.id.select_group_button);
        createGroupButton = findViewById(R.id.create_group_button);
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        selectGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupActivity.this, SelectParamActivity.class);
                startActivityForResult(intent, Constants.GROUP_SELECT_PARAM_RESULT);;
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createGroupButton.setEnabled(false);
                createAccount(groupNameEditText.getText().toString(), id_group);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.GROUP_SELECT_PARAM_RESULT){
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                if (data.getSerializableExtra("NAME_GROUP") != null) {
                    String name_group = data.getStringExtra("NAME_GROUP");
                    this.id_group = data.getIntExtra("ID_GROUP", 0);
                    selectGroupNameTextView.setText(name_group);
                }
            }
        }
    }

    public void createAccount(String nameGroup, Integer idGroup) {
        if(nameGroup.trim().length() > 0){
            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(CreateGroupActivity.this);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SITE_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi44 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi44.class);

            Call<IniviteData> call = jsonPlaceHolderApi.getMyJSON(memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), nameGroup, idGroup);
            Log.d("gergerg", call.request().url().toString());
            Log.d("request", call.request().url().toString());
            call.enqueue(new Callback<IniviteData>() {
                @Override
                public void onResponse(Call<IniviteData> call, Response<IniviteData> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    IniviteData posts = response.body();

                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_NAME_PROFILE,  posts.getGroupOfUser().getName());
                    editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_NAME_CREATOR_PROFILE,  posts.getGroupOfUser().getUserVeryShort().getName());
                    editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_CREATOR_PROFILE,  posts.getGroupOfUser().getUserVeryShort().getPhoto());
                    editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_CREATOR_PROFILE, posts.getGroupOfUser().getIdCreator());
                    editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_VALUE, posts.getGroupOfUser().getId());
                    editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE, posts.getGroupOfUser().getIdOlder());
                    editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_PROFILE, posts.getGroupOfUser().getImage());

                    memoryOperation.setIdGroupOfUser(posts.getGroupOfUser().getId());

                    editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, posts.getInfoGroup().getIdUniversity());
                    editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, posts.getInfoGroup().getIdGroup());
                    editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, posts.getInfoGroup().getNameGroup());
                    editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, posts.getInfoGroup().getNameUniversity());
                    editor.commit();

                    finish();
                }
                @Override
                public void onFailure(Call<IniviteData> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(CreateGroupActivity.this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(CreateGroupActivity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
