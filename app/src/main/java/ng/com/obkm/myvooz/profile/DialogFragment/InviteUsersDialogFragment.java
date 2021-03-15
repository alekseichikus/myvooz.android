package ng.com.obkm.myvooz.profile.DialogFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi36;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi37;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi45;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.EntryLink;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.EditGroup.EditGroupActivity;
import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InviteUsersDialogFragment extends BottomSheetDialogFragment {

    Context mContext;
    EditGroupActivity editGroupActivity;
    SharedPreferences mSettings;
    IActivityPresenter activityPresenter;
    TextView validityTextView;
    CardView updateLinkButton;
    CardView lockOpenButton;
    CardView lockButton;
    CardView inviteLinkBlock;
    TextView linkTextView;
    TextView blockTextView;
    MemoryOperation memoryOperation;

    public InviteUsersDialogFragment(EditGroupActivity editGroupActivity){
        this.editGroupActivity = editGroupActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        activityPresenter = new ActivityPresenter(context);
        memoryOperation = new MemoryOperation(getContext());
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

        View view = inflater.inflate(R.layout.dialog_fragment_invite_users, container,
                false);
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);

        CardView copyButton = view.findViewById(R.id.copy_button);
        linkTextView = view.findViewById(R.id.link_text_view);
        validityTextView = view.findViewById(R.id.validity_textview);
        inviteLinkBlock = view.findViewById(R.id.invite_link_block);
        blockTextView = view.findViewById(R.id.block_textview);
        updateLinkButton = view.findViewById(R.id.update_button);
        lockButton = view.findViewById(R.id.lock_button);
        lockOpenButton = view.findViewById(R.id.lock_open_button);

        if(!memoryOperation.getGroupOfUserIdOlderProfile().equals(memoryOperation.getIdUserProfile())){
            updateLinkButton.setVisibility(View.GONE);
        }

        copyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", linkTextView.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Скопировано", Toast.LENGTH_SHORT).show();
            }
        });

        updateLinkButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                updateLinkButton.setEnabled(false);
                updateData();
            }
        });

        lockOpenButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                lockOpenButton.setEnabled(false);
                lockOpenButton.setVisibility(View.GONE);
                lockButton.setVisibility(View.VISIBLE);

                lockData(true);
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                lockButton.setEnabled(false);
                lockOpenButton.setVisibility(View.VISIBLE);
                lockButton.setVisibility(View.GONE);

                lockData(false);
            }
        });
        getData();

        lockInterface(false);
        return view;
    }

    public void lockInterface(Boolean state){

        if(state){
            lockOpenButton.setVisibility(View.GONE);
            lockButton.setVisibility(View.VISIBLE);
            blockTextView.setVisibility(View.VISIBLE);
            validityTextView.setVisibility(View.GONE);
            updateLinkButton.setVisibility(View.GONE);
            inviteLinkBlock.setVisibility(View.GONE);
        }
        else{
            lockOpenButton.setVisibility(View.VISIBLE);
            lockButton.setVisibility(View.GONE);
            blockTextView.setVisibility(View.GONE);
            validityTextView.setVisibility(View.VISIBLE);
            updateLinkButton.setVisibility(View.VISIBLE);
            inviteLinkBlock.setVisibility(View.VISIBLE);
        }

        if(!memoryOperation.getIdUserProfile().equals(memoryOperation.getGroupOfUserIdOlderProfile())){
            updateLinkButton.setVisibility(View.GONE);
            lockOpenButton.setVisibility(View.GONE);
            lockButton.setVisibility(View.GONE);
            blockTextView.setText("Ссылка-приглашение заблокирована.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi36 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi36.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Call<EntryLink> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, ""), mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0));
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<EntryLink>() {
            @Override
            public void onResponse(Call<EntryLink> call, Response<EntryLink> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                EntryLink posts = response.body();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());


                Calendar cal = (Calendar) posts.getDate().clone();
                Calendar cal2 = (Calendar) posts.getDate().clone();

                cal.add(Calendar.HOUR_OF_DAY, -calendar.get(Calendar.HOUR_OF_DAY));
                cal.add(Calendar.MINUTE, -calendar.get(Calendar.MINUTE));

                if(posts.getCode().equals("")){
                    validityTextView.setText("Попросите сгенерировать ссылку своего старосту.");
                    linkTextView.setText("-");
                }
                else{
                    if(cal2.after(calendar)){
                        if(cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0){
                            validityTextView.setText("Через 24 ч. 00 м. ссылка станет не действительна");
                        }
                        else{
                            validityTextView.setText("Через " + cal.get(Calendar.HOUR_OF_DAY) + " ч. "+ cal.get(Calendar.MINUTE) + " м. ссылка станет не действительна");
                        }
                    }
                    else{
                        validityTextView.setText("Ссылка недействительна. Попросите старосту сгенерировать новую.");
                    }
                    linkTextView.setText(posts.getCode());
                }
                lockInterface(posts.isLock());
            }
            @Override
            public void onFailure(Call<EntryLink> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void updateData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi37 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi37.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Call<EntryLink> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, ""), mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0));
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<EntryLink>() {
            @Override
            public void onResponse(Call<EntryLink> call, Response<EntryLink> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                EntryLink posts = response.body();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                Calendar cal = (Calendar) posts.getDate().clone();

                String minutes = cal.get(Calendar.MINUTE) < 10 ? "0" + cal.get(Calendar.MINUTE) : String.valueOf(cal.get(Calendar.MINUTE));

                cal.add(Calendar.HOUR_OF_DAY, -calendar.get(Calendar.HOUR_OF_DAY));
                cal.add(Calendar.MINUTE, -calendar.get(Calendar.MINUTE));

                validityTextView.setText("Через 24 ч. 00 м. ссылка станет не действительна");
                updateLinkButton.setEnabled(true);
                linkTextView.setText(posts.getCode());

                lockInterface(posts.isLock());
            }
            @Override
            public void onFailure(Call<EntryLink> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void lockData(Boolean state){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi45 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi45.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Call<Boolean> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "")
                , mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0), state.equals(true) ? 1 : 0);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Boolean posts = response.body();

                lockInterface(state);
                lockButton.setEnabled(true);
                lockOpenButton.setEnabled(true);
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
