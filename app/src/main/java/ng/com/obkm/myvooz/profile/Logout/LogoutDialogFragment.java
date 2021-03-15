package ng.com.obkm.myvooz.profile.Logout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;

public class LogoutDialogFragment extends BottomSheetDialogFragment {


    Button exitButton;
    IActivityPresenter activityPresenter;
    SharedPreferences mSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        activityPresenter = new ActivityPresenter(getContext());
        mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_logout_layout, container, false);
        initUI(v);
        setListeners();
        return v;
    }

    public  void initUI(View v){
        exitButton = v.findViewById(R.id.logout_button);
    }

    public void setListeners(){
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityPresenter.deleteAuthData(mSettings);
                dismiss();
            }
        });
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
}
