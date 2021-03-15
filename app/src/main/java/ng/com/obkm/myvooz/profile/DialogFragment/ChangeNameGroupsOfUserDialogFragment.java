package ng.com.obkm.myvooz.profile.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi38;
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

public class ChangeNameGroupsOfUserDialogFragment extends BottomSheetDialogFragment {

    Context mContext;
    CardView changeNameButton;
    EditGroupActivity editGroupActivity;
    SharedPreferences mSettings;
    IActivityPresenter activityPresenter;
    EditText nameGroupEditText;
    MemoryOperation memoryOperation;

    public ChangeNameGroupsOfUserDialogFragment(EditGroupActivity editGroupActivity){
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

        View view = inflater.inflate(R.layout.dialog_fragment_change_name_group, container,
                false);
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);

        initUI(view);
        setListeners();

        nameGroupEditText.setText(memoryOperation.getGroupOfUserNameProfile());

        return view;
    }

    public void initUI(View view){
        changeNameButton = view.findViewById(R.id.update_button);
        nameGroupEditText = view.findViewById(R.id.link_text_view);
    }

    public void setListeners(){
        changeNameButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changeNameButton.setEnabled(false);
                updateData(nameGroupEditText.getText().toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateData(String nameGroup){
        if(nameGroup.trim().length() > 0){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SITE_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi38 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi38.class);
            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
            Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "")
                    , mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0), nameGroup);
            Log.d("request", call.request().url().toString());
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }

                    Boolean posts = response.body();

                    if(posts){
                        changeNameButton.setEnabled(true);
                        nameGroupEditText.setText(nameGroup);
                        memoryOperation.setGroupOfUserNameProfile(nameGroup);
                        editGroupActivity.setGroupName(nameGroup);
                        dismiss();
                    }
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getContext(), "Произошла какая-то ошибочка", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
