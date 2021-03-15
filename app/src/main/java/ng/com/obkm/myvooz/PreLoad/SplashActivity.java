package ng.com.obkm.myvooz.PreLoad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ng.com.obkm.myvooz.CheckForFullnessActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(SplashActivity.this, CheckForFullnessActivity.class);
        startActivity(intent);
        SplashActivity.this.overridePendingTransition(0, 0);
        finish();
    }
}
