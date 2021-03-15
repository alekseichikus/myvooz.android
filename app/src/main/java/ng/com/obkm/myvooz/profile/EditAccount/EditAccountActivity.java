package ng.com.obkm.myvooz.profile.EditAccount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.DialogFragment.ChangeNameAccountDialogFragment;
import ng.com.obkm.myvooz.profile.DialogFragment.LogoutAccountDialogFragment;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.VolleyMultipartRequest;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditAccountActivity extends AppCompatActivity{

    CardView backButton;
    CardView imageBlock;
    CardView sendButton;
    CardView changeAccountNameButton;
    IActivityPresenter activityPresenter;
    SharedPreferences mSettings;
    ImageView userPhotoProfile;
    ImageView userPhotoProfile2;
    private RequestQueue rQueue;
    private String upload_URL = Constants.SITE_ADDRESS + "profile?";
    private MemoryOperation memoryOperation;
    private EditText firstNameEditText;
    private TextView accountNameTextView;
    private TextView changeAccountNameTextView;
    private EditText lastNameEditText;
    private CardView logoutAccountButton;
    CardView plusMarkCarvView;

    private String initFirstName = "";
    private String initLastName =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        memoryOperation = new MemoryOperation(this);

        initUI();
        setListeners();

        firstNameEditText.setText(memoryOperation.getFirstNameProfile());
        lastNameEditText.setText(memoryOperation.getLastNameProfile());

        initFirstName = memoryOperation.getFirstNameProfile();
        initLastName = memoryOperation.getLastNameProfile();

        if (mSettings.contains(Constants.APP_PREFERENCES_PHOTO_PROFILE)) {
            String image = mSettings.getString(Constants.APP_PREFERENCES_PHOTO_PROFILE, "");
            if (image.equals("")) {
            } else{
                Picasso.get().load(image).into(userPhotoProfile);
                Picasso.get().load(image).into(userPhotoProfile2);
            }
        }

        changeAccountNameTextView.setText(memoryOperation.getFirstNameProfile() + " " + memoryOperation.getLastNameProfile());
        accountNameTextView.setText(memoryOperation.getFirstNameProfile() + " " + memoryOperation.getLastNameProfile());

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(EditAccountActivity.this);
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        userPhotoProfile = findViewById(R.id.user_photo_profile);
        imageBlock = findViewById(R.id.image_block);
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        sendButton = findViewById(R.id.send_button);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        plusMarkCarvView = findViewById(R.id.plus_mark);
        logoutAccountButton = findViewById(R.id.logout_account_button);
        userPhotoProfile2 = findViewById(R.id.user_photo_profile_2);
        changeAccountNameButton = findViewById(R.id.change_account_name_button);
        accountNameTextView = findViewById(R.id.account_name);
        changeAccountNameTextView = findViewById(R.id.change_account_name);
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                EditAccountActivity.this.overridePendingTransition(R.anim.alpha_down, R.anim.diagonaltranslate_down);
            }
        });

        changeAccountNameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ChangeNameAccountDialogFragment bottomSheet = new ChangeNameAccountDialogFragment(EditAccountActivity.this);
                bottomSheet.show(getSupportFragmentManager(), "groupOfUserBottomSheet");
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

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editAccount(firstNameEditText.getText().toString(), lastNameEditText.getText().toString());
            }
        });

        logoutAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutAccountDialogFragment bottomSheet = new LogoutAccountDialogFragment(EditAccountActivity.this);
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
                                        memoryOperation.setUserPhoto(image);
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
                    params.put("type", "image_profile");
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

    public void logoutFromGroup(){
        finish();
        EditAccountActivity.this.overridePendingTransition(R.anim.alpha_down, R.anim.diagonaltranslate_down);
    }

    public void setName(String firstName, String lastName){
        changeAccountNameTextView.setText(firstName + " " + lastName);
        accountNameTextView.setText(firstName + " " + lastName);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void editAccount(String firstName, String lastName) {
        if(!initFirstName.equals(firstName) || !initLastName.equals(lastName)){
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
                            Toast.makeText(EditAccountActivity.this, "Успешно изменено", Toast.LENGTH_SHORT).show();
                            memoryOperation.setFirstNameProfile(firstName);
                            memoryOperation.setLastNameProfile(lastName);
                            memoryOperation.setFullNameProfile(firstName + " " + lastName);
                            finish();
                        }
                        else{
                            Toast.makeText(EditAccountActivity.this, "Произошла какая-то бяка 1", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(EditAccountActivity.this, "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(EditAccountActivity.this, "Заполните поля", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(EditAccountActivity.this, "Нет изменений", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
