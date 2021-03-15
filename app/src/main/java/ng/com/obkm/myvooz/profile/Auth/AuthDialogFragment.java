package ng.com.obkm.myvooz.profile.Auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;

import java.util.HashSet;
import java.util.Set;

import ng.com.obkm.myvooz.CheckForFullnessActivity;
import ng.com.obkm.myvooz.R;

public class AuthDialogFragment extends BottomSheetDialogFragment {
    private String[] scope = new String[]{VKScope.PAGES, VKScope.EMAIL};
    private Set<String> scope2 = new HashSet<String>();;

    CardView vkAuthButton;
    CardView yaAuthButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.auth_layout, container, false);

        initUI(v);
        setListeners();

        return v;
    }

    public  void initUI(View v){
        vkAuthButton = v.findViewById(R.id.vk_auth_button);
        yaAuthButton = v.findViewById(R.id.ya_auth_button);
    }

    public void setListeners(){
        vkAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(getActivity(), scope);
                dismiss();
            }
        });

        yaAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YandexAuthSdk sdk = new YandexAuthSdk( new YandexAuthOptions(getContext(), true));
                getActivity().startActivityForResult(sdk.createLoginIntent(getContext(), scope2), CheckForFullnessActivity.REQUEST_CODE_YA_LOGIN);
                dismiss();
            }
        });


    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
}
