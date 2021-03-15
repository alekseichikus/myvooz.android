package ng.com.obkm.myvooz.profile.EditGroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi35;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi42;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.SelectParamActivity;
import ng.com.obkm.myvooz.UsersListActivity;
import ng.com.obkm.myvooz.model.IniviteData;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.AddNotification.view.AddNotificationActivity;
import ng.com.obkm.myvooz.profile.DialogFragment.ChangeNameGroupsOfUserDialogFragment;
import ng.com.obkm.myvooz.profile.DialogFragment.InviteUsersDialogFragment;
import ng.com.obkm.myvooz.profile.DialogFragment.LogoutGroupsOfUserDialogFragment;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.VolleyMultipartRequest;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditGroupActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    CardView backButton;
    CardView imageBlock;
    CardView plusMarkCarvView;
    IActivityPresenter activityPresenter;
    SharedPreferences mSettings;
    ImageView userPhotoProfile;
    ImageView userPhotoProfile2;
    ImageView userCreatorPhotoProfile;
    TextView groupNameTextView;
    LinearLayout usersButton;
    LinearLayout inviteUsersButton;
    RelativeLayout changeGroupNameButton;
    CardView logoutGroupsOfUserButton;
    LinearLayout notificationButton;
    TextView changeGroupOfUserNameTextView;
    TextView groupName2TextView;
    TextView groupName3TextView;
    TextView universityName3TextView;
    TextView creatorNameTextView;
    TextView countOfGroupTextView;
    private RequestQueue rQueue;
    private String upload_URL = Constants.SITE_ADDRESS + "profile?";
    private MemoryOperation memoryOperation;
    private EditText firstNameEditText;
    private RelativeLayout changeGroupButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView idGroupOfUserTextView;

    private String initName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        memoryOperation = new MemoryOperation(this);

        initUI();
        setListeners();

        initName = memoryOperation.getFirstNameProfile();

        if (!memoryOperation.getGroupPhotoProfile().equals("")) {
            String image = memoryOperation.getGroupPhotoProfile();
            Picasso.get().load(image).into(userPhotoProfile);
            Picasso.get().load(image).into(userPhotoProfile2);
        }

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(EditGroupActivity.this);

        dataUpdate();

        swipeRefreshLayout.setRefreshing(true);
        getData();
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        userPhotoProfile = findViewById(R.id.user_photo_profile);
        idGroupOfUserTextView = findViewById(R.id.id_group_of_user);
        userPhotoProfile2 = findViewById(R.id.user_photo_profile_2);
        imageBlock = findViewById(R.id.image_block);
        countOfGroupTextView = findViewById(R.id.count_of_group);
        plusMarkCarvView = findViewById(R.id.plus_mark);
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        swipeRefreshLayout.setOnRefreshListener(this);
        logoutGroupsOfUserButton = findViewById(R.id.logout_group_button);
        creatorNameTextView = findViewById(R.id.creator_name);
        notificationButton = findViewById(R.id.bell_button);
        userCreatorPhotoProfile = findViewById(R.id.user_creator_photo_profile);
        usersButton = findViewById(R.id.users_button);
        groupNameTextView = findViewById(R.id.group_name);
        groupName2TextView = findViewById(R.id.group_name_2);
        groupName3TextView = findViewById(R.id.group_name_3);
        universityName3TextView = findViewById(R.id.university_name_3);
        changeGroupOfUserNameTextView = findViewById(R.id.change_group_of_user_name);
        inviteUsersButton = findViewById(R.id.invite_users_button);
        changeGroupNameButton = findViewById(R.id.change_group_name);

    }

    public void dataUpdate(){
        groupNameTextView.setText(memoryOperation.getGroupOfUserNameProfile());
        changeGroupOfUserNameTextView.setText(memoryOperation.getGroupOfUserNameProfile());

        initName = memoryOperation.getFirstNameProfile();

        if (!memoryOperation.getGroupPhotoProfile().equals("")) {
            String image = memoryOperation.getGroupPhotoProfile();
            Picasso.get().load(image).into(userPhotoProfile);
            Picasso.get().load(image).into(userPhotoProfile2);
        }

        if (!memoryOperation.getGroupOfUserPhotoOlderProfile().equals("")) {
            String image = memoryOperation.getGroupOfUserPhotoOlderProfile();
            Picasso.get().load(image).into(userCreatorPhotoProfile);
        }

        creatorNameTextView.setText(memoryOperation.getGroupOfUserNameOlderProfile());

        groupName2TextView.setText(memoryOperation.getNameGroupProfile());
        groupName3TextView.setText(memoryOperation.getNameGroupProfile());

        countOfGroupTextView.setText(String.valueOf(memoryOperation.getGroupOfUserCountUsersProfile()));

        idGroupOfUserTextView.setText("ID" + memoryOperation.getIdGroupOfUser());

        universityName3TextView.setText(memoryOperation.getNameUniversityProfile());

        if(memoryOperation.getGroupOfUserIdOlderProfile() == memoryOperation.getIdUserProfile()){
            plusMarkCarvView.setVisibility(View.VISIBLE);
            imageBlock.setEnabled(true);
            changeGroupButton.setVisibility(View.VISIBLE);
            changeGroupNameButton.setVisibility(View.VISIBLE);
            notificationButton.setVisibility(View.VISIBLE);
        }
        else{
            notificationButton.setVisibility(View.GONE);
            changeGroupButton.setVisibility(View.GONE);
            changeGroupNameButton.setVisibility(View.GONE);
            plusMarkCarvView.setVisibility(View.GONE);
            imageBlock.setEnabled(false);
        }

        if (!memoryOperation.getGroupOfUserPhotoOlderProfile().equals("")) {
            String image = memoryOperation.getGroupOfUserPhotoOlderProfile();
            Picasso.get().load(image).into(userCreatorPhotoProfile);
        }

        creatorNameTextView.setText(memoryOperation.getGroupOfUserNameOlderProfile());
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                EditGroupActivity.this.overridePendingTransition(R.anim.alpha_down, R.anim.diagonaltranslate_down);
            }
        });

        plusMarkCarvView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditGroupActivity.this, UsersListActivity.class);
                startActivity(intent);
            }
        });

        inviteUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InviteUsersDialogFragment bottomSheet = new InviteUsersDialogFragment(EditGroupActivity.this);
                bottomSheet.show(getSupportFragmentManager(), "groupOfUserBottomSheet");
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditGroupActivity.this, AddNotificationActivity.class);
                startActivity(intent);
            }
        });

        changeGroupButton = findViewById(R.id.change_group);
        changeGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(memoryOperation.getGroupOfUserIdOlderProfile() == memoryOperation.getIdUserProfile()){
                    Intent intent = new Intent(EditGroupActivity.this, SelectParamActivity.class);
                    startActivityForResult(intent, Constants.GROUP_SELECT_PARAM_RESULT);
                }
                else{
                    Toast.makeText(EditGroupActivity.this, "Нет доступа к этой функции.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeGroupNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(memoryOperation.getGroupOfUserIdOlderProfile() == memoryOperation.getIdUserProfile()){
                    ChangeNameGroupsOfUserDialogFragment bottomSheet = new ChangeNameGroupsOfUserDialogFragment(EditGroupActivity.this);
                    bottomSheet.show(getSupportFragmentManager(), "groupOfUserBottomSheet");
                }
                else{
                    Toast.makeText(EditGroupActivity.this, "Нет доступа к этой функции.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutGroupsOfUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutGroupsOfUserDialogFragment bottomSheet = new LogoutGroupsOfUserDialogFragment(EditGroupActivity.this);
                bottomSheet.show(getSupportFragmentManager(), "groupOfUserBottomSheet");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // imageView.setImageBitmap(bitmap);
                    //Log.d("rgerhgerg", getFileName(contentURI));
                    //model.uploadFile(getRealPathFromUri(contentURI));

                    uploadImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode == Constants.GROUP_SELECT_PARAM_RESULT){
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                if (data.getSerializableExtra("NAME_GROUP") != null) {
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

                    groupName3TextView.setText(name_group);
                    universityName3TextView.setText(name_university);
                    groupName2TextView.setText(name_group);
                }
            }
        }
    }
    private void uploadImage(final Bitmap bitmap){
        Bitmap scaledBitmap = scaleDown(bitmap, 1000, true);
        if(scaledBitmap.getByteCount() <= 5242880){

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new com.android.volley.Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));

                                jsonObject.toString().replace("\\\\","");

                                if (jsonObject.getString("status").equals("true")) {

                                    String url = jsonObject.getString("path");

                                    String image = url;
                                    if (image.equals("")) {
                                    } else{
                                        Picasso.get().load(image).into(userPhotoProfile);
                                        Picasso.get().load(image).into(userPhotoProfile2);
                                        memoryOperation.setGroupPhotoProfile(image);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("type", "group_profile");
                    params.put("access_token", memoryOperation.getAccessToken());
                    params.put("user_id", String.valueOf(memoryOperation.getIdUserProfile()));
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("filename", new DataPart(imagename + ".png", getFileDataFromDrawable(scaledBitmap)));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(this);
            rQueue.add(volleyMultipartRequest);
        }
        else{
            Toast.makeText(getApplicationContext(), "Файл должен быть не более 5 мб", Toast.LENGTH_SHORT).show();
        }
    }
    private static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void editAccount(String firstName, String lastName) {
        if(!initName.equals(firstName)){
            if(firstName.trim().length() > 0 && lastName.trim().length() > 0){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.SITE_ADDRESS)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi35 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi35.class);

                Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), firstName, lastName);
                Log.d("request", call.request().url().toString());
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        Boolean state = response.body();
                        if(state){
                            Toast.makeText(EditGroupActivity.this, "Успешно изменено", Toast.LENGTH_SHORT).show();
                            memoryOperation.setFirstNameProfile(firstName);
                            memoryOperation.setLastNameProfile(lastName);
                            memoryOperation.setFullNameProfile(firstName + " " + lastName);
                            finish();
                            EditGroupActivity.this.overridePendingTransition(R.anim.diagonaltranslate,R.anim.alpha);
                        }
                        else{
                            Toast.makeText(EditGroupActivity.this, "Произошла какая-то бяка 1", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(EditGroupActivity.this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(EditGroupActivity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(EditGroupActivity.this, "Нет изменений", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi42 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi42.class);
        Call<IniviteData> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, ""), mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0));
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
                editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_NAME_OLDER_PROFILE,  posts.getGroupOfUser().getUserVeryShort().getName());
                editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_OLDER_PROFILE,  posts.getGroupOfUser().getUserVeryShort().getPhoto());
                editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE, posts.getGroupOfUser().getIdOlder());
                editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_PROFILE, posts.getGroupOfUser().getImage());
                editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_COUNT_USERS_PROFILE, posts.getGroupOfUser().getCountUsers());

                editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, posts.getInfoGroup().getIdUniversity());
                editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, posts.getInfoGroup().getIdGroup());
                editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, posts.getInfoGroup().getNameGroup());
                editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, posts.getInfoGroup().getNameUniversity());
                editor.commit();

                dataUpdate();
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<IniviteData> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    public void setGroupName(String nameGroup){
        groupNameTextView.setText(nameGroup);
        changeGroupOfUserNameTextView.setText(nameGroup);
    }

    public void logoutFromGroup(){
        finish();
        EditGroupActivity.this.overridePendingTransition(R.anim.alpha_down, R.anim.diagonaltranslate_down);
    }


    @Override
    protected void onResume() {
        super.onResume();
        dataUpdate();
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
