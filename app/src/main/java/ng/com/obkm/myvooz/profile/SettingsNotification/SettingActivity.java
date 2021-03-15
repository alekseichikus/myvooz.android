package ng.com.obkm.myvooz.profile.SettingsNotification;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class SettingActivity extends AppCompatActivity{

    CardView backButton;
    IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;
    Switch createNoteSwitch;
    Switch changeNoteSwitch;
    Switch termNoteSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_notification);

        memoryOperation = new MemoryOperation(this);

        initUI();
        setListeners();

        if (memoryOperation.isCreateNoteSettingProfile()){
            createNoteSwitch.setChecked(true);
        }
        else{
            createNoteSwitch.setChecked(false);
        }

        if (memoryOperation.isChangeNoteSettingProfile()){
            changeNoteSwitch.setChecked(true);
        }
        else{
            changeNoteSwitch.setChecked(false);
        }

        if (memoryOperation.isTermNoteSettingProfile()){
            termNoteSwitch.setChecked(true);
        }
        else{
            termNoteSwitch.setChecked(false);
        }

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(SettingActivity.this);
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        createNoteSwitch = findViewById(R.id.switch_create_note);
        changeNoteSwitch = findViewById(R.id.switch_change_note);
        termNoteSwitch = findViewById(R.id.switch_term_note);
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        createNoteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                memoryOperation.setCreateNoteSettingProfile(createNoteSwitch.isChecked());
            }
        });

        changeNoteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                memoryOperation.setChangeNoteSettingProfile(changeNoteSwitch.isChecked());
            }
        });

        termNoteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                memoryOperation.setTermNoteSettingProfile(termNoteSwitch.isChecked());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
