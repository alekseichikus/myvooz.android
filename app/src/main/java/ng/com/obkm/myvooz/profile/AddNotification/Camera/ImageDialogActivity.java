package ng.com.obkm.myvooz.profile.AddNotification.Camera;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ng.com.obkm.myvooz.R;

public class ImageDialogActivity extends AppCompatActivity {
    PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog_fragment);

        Bundle arguments = getIntent().getExtras();
        String idItem = arguments.get("id").toString();
        String fullPathItem = arguments.get("full_path").toString();
        String pathItem = arguments.get("path").toString();

        ActivityCompat.requestPermissions(ImageDialogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        CardView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        PhotoView imageView = findViewById(R.id.image);

        CardView doownloadButton = findViewById(R.id.download_button);
        doownloadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/MyVooz");
                dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    Toast.makeText(ImageDialogActivity.this, "Сохранено", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(ImageDialogActivity.this, "Ошибочка", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBlack));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.colorBlack));




        Callback imageLoadedCallback = new Callback() {

            @Override
            public void onSuccess() {
                if(attacher!=null){
                    attacher.update();
                }else{
                    attacher = new PhotoViewAttacher(imageView);
                    attacher.update();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        };
        Picasso.get().load(fullPathItem).into(imageView, imageLoadedCallback);
    }
}

