package ng.com.obkm.myvooz.profile.UserRequestConfirm;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.presenter.IUserRequestConfirmPresenter;

public class UserRequestConfirmFuncDialogFragment extends BottomSheetDialogFragment {

    Integer idUser;
    IUserRequestConfirmPresenter userRequestConfirmPresenter;
    ArrayList<UserShort> employeeArrayList;
    Integer position;

    public UserRequestConfirmFuncDialogFragment(Integer idUser, IUserRequestConfirmPresenter userRequestConfirmPresenter, ArrayList<UserShort> employeeArrayList, Integer position){
        this.idUser = idUser;
        this.userRequestConfirmPresenter = userRequestConfirmPresenter;
        this.employeeArrayList = employeeArrayList;
        this.position = position;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_request_confirm_func_dialog_fragment, container,
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

    public void makeTheHeadData(Integer selUserId){
        userRequestConfirmPresenter.sendDataToServer(selUserId, 1, position);
        dismiss();
    }

    public void excludeData(Integer selUserId){
        userRequestConfirmPresenter.sendDataToServer(selUserId, 0, position);
        dismiss();
    }
}
