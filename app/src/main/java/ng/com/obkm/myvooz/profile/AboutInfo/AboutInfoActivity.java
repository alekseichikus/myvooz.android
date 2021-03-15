package ng.com.obkm.myvooz.profile.AboutInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;

public class AboutInfoActivity extends AppCompatActivity{

    CardView backButton;
    LinearLayout privacyPolicyButton;
    IActivityPresenter activityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_info);

        initUI();
        setListeners();

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(AboutInfoActivity.this);
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        privacyPolicyButton = findViewById(R.id.privacy_policy_button);
    }

    void setListeners(){
        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/@myvoooz-privacy"));
                startActivity(browserIntent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
