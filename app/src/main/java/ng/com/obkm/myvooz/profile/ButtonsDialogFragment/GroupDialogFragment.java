package ng.com.obkm.myvooz.profile.ButtonsDialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi13;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.UsersListActivity;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.EditGroup.EditGroupActivity;
import ng.com.obkm.myvooz.profile.NotificationsFragment;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupDialogFragment extends BottomSheetDialogFragment {

    Context mContext;
    NotificationsFragment notificationsFragment;
    SharedPreferences mSettings;
    IActivityPresenter activityPresenter;

    public GroupDialogFragment(NotificationsFragment notificationsFragment){
        this.notificationsFragment = notificationsFragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        activityPresenter = new ActivityPresenter(context);
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

        View view = inflater.inflate(R.layout.buttons_group_of_user, container,
                false);
        Button logoutButton = view.findViewById(R.id.logout_button);
        LinearLayout listGroupButton = view.findViewById(R.id.list_group_button);
        LinearLayout infoAboutGroupButton = view.findViewById(R.id.info_about_group_button);

        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


        listGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UsersListActivity.class);
                startActivity(intent);
            }

        });

        infoAboutGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditGroupActivity.class);
                startActivity(intent);
            }

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void logout(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi13 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi13.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, ""), mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                activityPresenter.deleteAuthData(mSettings);
                notificationsFragment.isStateView();
                dismiss();
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
