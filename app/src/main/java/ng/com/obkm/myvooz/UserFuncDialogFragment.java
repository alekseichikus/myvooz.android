package ng.com.obkm.myvooz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi17;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi18;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFuncDialogFragment extends BottomSheetDialogFragment {

    UserShort idUser;
    UsersListActivity usersListActivity;
    Integer position;
    MemoryOperation memoryOperation;

    public UserFuncDialogFragment(UserShort idUser, UsersListActivity usersListActivity, Integer position){
        this.idUser = idUser;
        this.usersListActivity = usersListActivity;
        this.position = position;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        memoryOperation = new MemoryOperation(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_func_dialog_fragment, container,
                false);

        LinearLayout makeTheHeadButton = view.findViewById(R.id.make_the_head_button);
        LinearLayout excludeButton = view.findViewById(R.id.exclude_button);
        excludeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                excludeData(idUser);
            }
        });
        makeTheHeadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                makeTheHeadData(idUser);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void makeTheHeadData(UserShort user){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi18 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi18.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, ""), mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0), user.getId());
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Boolean posts = response.body();
                if(posts){
                    usersListActivity.makeTheHead(user);

                    dismiss();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void excludeData(UserShort selUser){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi17 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi17.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "")
                , mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0), selUser.getId());
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Boolean posts = response.body();
                if(posts){
                    Toast.makeText(getContext(), "Исключен!", Toast.LENGTH_SHORT).show();
                    usersListActivity.deleteItem(idUser.getId());
                    memoryOperation.setGroupOfUserCountUsersProfile(memoryOperation.getGroupOfUserCountUsersProfile()-1);
                    dismiss();
                }

            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
