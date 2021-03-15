package ng.com.obkm.myvooz.profile.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi40;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.EditGroup.EditGroupActivity;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogoutGroupsOfUserDialogFragment extends BottomSheetDialogFragment {

    Context mContext;
    CardView cancelButton;
    CardView logoutButton;
    EditGroupActivity editGroupActivity;
    SharedPreferences mSettings;
    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;

    public LogoutGroupsOfUserDialogFragment(EditGroupActivity editGroupActivity){
        this.editGroupActivity = editGroupActivity;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        activityPresenter = new ActivityPresenter(context);
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

        View view = inflater.inflate(R.layout.dialog_fragment_logout_groups_of_user, container,
                false);
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);

        initUI(view);
        setListeners();

        return view;
    }

    public void initUI(View view){
        cancelButton = view.findViewById(R.id.cancel_button);
        logoutButton = view.findViewById(R.id.logout_button);
    }

    public void setListeners(){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutData();
            }
        });
    }

    public void logoutData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi40 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi40.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, ""), mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0));
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Boolean posts = response.body();

                if(posts){
                    memoryOperation.setIdGroupOfUser(0);
                    editGroupActivity.logoutFromGroup();
                    dismiss();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
