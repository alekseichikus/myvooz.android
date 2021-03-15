package ng.com.obkm.myvooz.profile.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.EditAccount.EditAccountActivity;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class LogoutAccountDialogFragment extends BottomSheetDialogFragment {

    Context mContext;
    CardView cancelButton;
    CardView logoutButton;
    EditAccountActivity editAccountActivity;
    SharedPreferences mSettings;
    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;

    public LogoutAccountDialogFragment(EditAccountActivity editAccountActivity){
        this.editAccountActivity = editAccountActivity;

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

        View view = inflater.inflate(R.layout.dialog_fragment_logout_account, container,
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
        activityPresenter.deleteAuthData(mSettings);
        editAccountActivity.logoutFromGroup();
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
