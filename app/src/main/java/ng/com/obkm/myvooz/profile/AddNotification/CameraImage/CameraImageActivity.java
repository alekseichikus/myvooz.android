package ng.com.obkm.myvooz.profile.AddNotification.CameraImage;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.chrisbanes.photoview.PhotoView;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class CameraImageActivity extends AppCompatActivity {
    IActivityPresenter activityPresenter;
    CardView backButton;
    CardView checkButton;
    String fullPathItem;
    MemoryOperation memoryOperation;
    PhotoView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_image);

        Bundle arguments = getIntent().getExtras();
        fullPathItem = arguments.get("full_path").toString();

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(CameraImageActivity.this);

        memoryOperation = new MemoryOperation(this);

        initUI();
        setListeners();

        Uri imgUri=Uri.parse(fullPathItem);
        imageView.setImageURI(imgUri);
    }

    void initUI(){
        checkButton = findViewById(R.id.check_button);
        backButton = findViewById(R.id.back_button);
        imageView = findViewById(R.id.image);
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                memoryOperation.setSelectImageNotification(fullPathItem);
                onBackPressed();
            }
        });
    }
}

