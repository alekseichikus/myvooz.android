package ng.com.obkm.myvooz.profile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.SearchEmptyClassroomActivity;
import ng.com.obkm.myvooz.SelectParamActivity;
import ng.com.obkm.myvooz.profile.AboutInfo.AboutInfoActivity;
import ng.com.obkm.myvooz.profile.Auth.AuthDialogFragment;
import ng.com.obkm.myvooz.profile.CreateGroup.CreateGroupActivity;
import ng.com.obkm.myvooz.profile.EditAccount.EditAccountActivity;
import ng.com.obkm.myvooz.profile.EditGroup.EditGroupActivity;
import ng.com.obkm.myvooz.profile.JoinGroup.JoinGroupActivity;
import ng.com.obkm.myvooz.profile.SettingsNotification.SettingActivity;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;

public class NotificationsFragment extends Fragment{

    MainActivity mainActivity;
    Context mContext;
    TextView typeProfile;
    TextView nameProfile;
    ImageView userPhotoProfile;
    ImageView groupPhotoProfile;
    CardView imageBlock;
    LinearLayout infoGroupBlock;
    LinearLayout groupOfUserButton;
    CardView changeGroupDontGOUButton;
    LinearLayout infoBlock;
    TextView groupNameText;
    TextView universityNameText;
    TextView groupNameTextView;
    TextView groupName2TextView;
    SharedPreferences mSettings;

    LinearLayout groupBlock;
    LinearLayout infoAboutButton;
    CardView editButton;
    ImageView edit2Button;
    CardView markerRequestConfirm;
    CardView createGroupButton;
    CardView joinGroupButton;
    View view;
    Boolean stateL = false;
    MemoryOperation memoryOperation;

    public NotificationsFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        memoryOperation = new MemoryOperation(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (savedInstanceState != null) {
            //probably orientation change

        } else {
            if (stateL) {

            } else {
                view = inflater.inflate(R.layout.fragment_notifications, container, false);
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state", (Serializable) "1");
    }

    public void isStateView(){

        if(!memoryOperation.getAccessToken().equals("")){
            editButton.setVisibility(View.VISIBLE);
            edit2Button.setVisibility(View.GONE);
            if(!memoryOperation.getIdGroupOfUser().equals(0)){
                changeGroupDontGOUButton.setVisibility(View.GONE);
                if (!memoryOperation.getGroupPhotoProfile().equals("")) {
                    String image = memoryOperation.getGroupPhotoProfile();
                    Picasso.get().load(image).into(groupPhotoProfile);
                }
                groupNameTextView.setText(memoryOperation.getGroupOfUserNameProfile());
                groupName2TextView.setText(memoryOperation.getNameGroupProfile());
                infoGroupBlock.setVisibility(View.VISIBLE);
                groupBlock.setVisibility(View.GONE);
            }
            else{
                changeGroupDontGOUButton.setVisibility(View.VISIBLE);
                groupBlock.setVisibility(View.VISIBLE);
                infoGroupBlock.setVisibility(View.GONE);
            }
            infoBlock.setVisibility(View.VISIBLE);
        }
        else{
            changeGroupDontGOUButton.setVisibility(View.VISIBLE);
            infoBlock.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            edit2Button.setVisibility(View.VISIBLE);
            infoGroupBlock.setVisibility(View.GONE);
            groupBlock.setVisibility(View.GONE);
        }

        if (!memoryOperation.getAccessToken().equals("")){
            userPhotoProfile.setVisibility(View.VISIBLE);
            imageBlock.setVisibility(View.INVISIBLE);


            if(!memoryOperation.getIdGroupOfUser().equals(0) && memoryOperation.getIdUserProfile().equals(memoryOperation.getGroupOfUserIdOlderProfile())){
                typeProfile.setText("Староста");
            }
            else{
                typeProfile.setText("Студент");
            }

            if (mSettings.getString(Constants.APP_PREFERENCES_NAME_PROFILE, "") != "") {
                nameProfile.setText(mSettings.getString(Constants.APP_PREFERENCES_NAME_PROFILE, ""));
            }
            else{
                nameProfile.setText("null");
            }

            universityNameText.setText(mSettings.getString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, ""));
            groupNameText.setText(mSettings.getString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, ""));
            if (mSettings.contains(Constants.APP_PREFERENCES_PHOTO_PROFILE)) {
                String image = mSettings.getString(Constants.APP_PREFERENCES_PHOTO_PROFILE, "");
                if (image.equals("")) {
                    imageBlock.setVisibility(View.VISIBLE);
                } else{
                    Picasso.get().load(image).into(userPhotoProfile);
                }

            }

            if(!memoryOperation.getIdGroupOfUser().equals(0)){

            }
        }
        else {
            markerRequestConfirm.setVisibility(View.GONE);
            markerRequestConfirm.setVisibility(View.GONE);
            final Animation animationRotateCorner = AnimationUtils.loadAnimation(
                    mContext, R.anim.rotate_infinity);
            //confirmBlock.setVisibility(View.GONE);
            typeProfile.setText("Группа");
            userPhotoProfile.setVisibility(View.GONE);
            imageBlock.setVisibility(View.VISIBLE);
            if (mSettings.contains(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE)) {
                nameProfile.setText(mSettings.getString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE, ""));
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (savedInstanceState != null) {
            //probably orientation change

        } else {
            if (stateL) {

            } else {
                userPhotoProfile = view.findViewById(R.id.user_photo_profile);
                LinearLayout buuton_1 = view.findViewById(R.id.vk_group_button);
                typeProfile = view.findViewById(R.id.type_profile);
                nameProfile = view.findViewById(R.id.name_profile);
                imageBlock = view.findViewById(R.id.image_block);

                groupBlock = view.findViewById(R.id.group_block);
                groupNameText = view.findViewById(R.id.group_name);
                infoBlock = view.findViewById(R.id.info_block);
                changeGroupDontGOUButton = view.findViewById(R.id.change_group_dont_gou_button);
                universityNameText = view.findViewById(R.id.university_name);
                editButton = view.findViewById(R.id.edit_button);
                edit2Button = view.findViewById(R.id.edit_2_button);
                groupNameTextView = view.findViewById(R.id.group_name_profile);
                markerRequestConfirm = view.findViewById(R.id.marker_new_request_confirm);
                infoAboutButton = view.findViewById(R.id.about_info_button);
                groupOfUserButton = view.findViewById(R.id.group_of_user_button);
                infoGroupBlock = view.findViewById(R.id.info_group_block);
                createGroupButton = view.findViewById(R.id.create_group_button);
                joinGroupButton = view.findViewById(R.id.join_group_button);
                groupPhotoProfile = view.findViewById(R.id.group_photo_profile);
                groupName2TextView = view.findViewById(R.id.name_group_profile);
                LinearLayout changeUserButton = view.findViewById(R.id.change_user_button);


                stateL = true;

                buuton_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/myvoooz"));
                        startActivity(browserIntent);
                    }
                });

                LinearLayout SearchEmptyClassroomButton = view.findViewById(R.id.search_empty_classroom_button);
                SearchEmptyClassroomButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, SearchEmptyClassroomActivity.class);
                        startActivity(intent);
                    }
                });

                SearchEmptyClassroomButton.setVisibility(View.GONE);

                LinearLayout SettingButton = view.findViewById(R.id.setting_button);
                SettingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, SettingActivity.class);
                        startActivity(intent);
                    }
                });

                groupOfUserButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), EditGroupActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.diagonaltranslate,R.anim.alpha);
                    }
                });

                infoAboutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, AboutInfoActivity.class);
                        startActivity(intent);
                    }
                });

                createGroupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, CreateGroupActivity.class);
                        startActivity(intent);
                    }
                });

                joinGroupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, JoinGroupActivity.class);
                        startActivity(intent);
                    }
                });

                changeGroupDontGOUButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, SelectParamActivity.class);
                        getActivity().startActivityForResult(intent, Constants.CHANGE_GROUP_DONT_GOU_PARAM_RESULT);
                    }
                });

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, EditAccountActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.diagonaltranslate,R.anim.alpha);
                    }
                });

                changeUserButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!memoryOperation.getAccessToken().equals("")){

                        }
                        else{
                            AuthDialogFragment bottomSheet = new AuthDialogFragment();
                            bottomSheet.show(getFragmentManager(), "exampleBottomSheet");
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        isStateView();

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorWhite));
    }
    @Override
    public void onStop() {
        super.onStop();
    }

}
