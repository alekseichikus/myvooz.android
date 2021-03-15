package ng.com.obkm.myvooz.profile.AddNotification.Camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import ng.com.obkm.myvooz.profile.AddNotification.CameraImage.CameraImageActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.utils.Constants;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback
{
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private CardView shotBtn;
    String path;
    SharedPreferences mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }


        mSettings = PreferenceManager.getDefaultSharedPreferences(CameraActivity.this);
        path = mSettings.getString(Constants.APP_PREFERENCES_SELECT_IMAGE_NOTIFICATION, "");

        // и без заголовка
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_camera);

        // наше SurfaceView имеет имя SurfaceView01
        preview = (SurfaceView) findViewById(R.id.SurfaceView01);

        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // кнопка имеет имя Button01
        shotBtn = findViewById(R.id.Button01);
        shotBtn.setOnClickListener(this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBlack));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.colorBlack));

        preview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                camera.autoFocus(CameraActivity.this);
            }
        });

        shotBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, null, mPictureCallback);
            }
        });
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            String pictureFile = generateFile(); //1
            try {
                savePhotoInFile (data, pictureFile); //2

            }
            catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Ошибочка", Toast.LENGTH_LONG).show();
            }
            camera.startPreview(); //3

            Intent intent = new Intent(CameraActivity.this, CameraImageActivity.class);
            intent.putExtra("full_path", pictureFile);
            startActivity(intent);
            overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
        }
    };

    public static String generateFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;

        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/MyVooz");
        if (! path.exists()){
            if (! path.mkdirs()){
                return null;
            }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        File newFile = new File(path.getPath() + File.separator + timeStamp + ".jpg");
        return path.getPath() + File.separator + timeStamp + ".jpg";
    }


    private void savePhotoInFile(byte[] data, String pictureFile) throws Exception {

        if (pictureFile == null)
            throw new Exception();
        File newFile = new File(pictureFile);
        Uri uu = Uri.fromFile(newFile);
        OutputStream os = getContentResolver().openOutputStream(uu);
        os.write(data);
        os.close();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(path != mSettings.getString(Constants.APP_PREFERENCES_SELECT_IMAGE_NOTIFICATION, "")){
            path = mSettings.getString(Constants.APP_PREFERENCES_SELECT_IMAGE_NOTIFICATION, "");
            onBackPressed();
        }
        camera = Camera.open();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (camera != null)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Camera.CameraInfo info = new Camera.CameraInfo();

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }


        int rotate = ( info.orientation - degrees + 360 ) % 360;

        Camera.Parameters params = camera.getParameters();
        params.setRotation(90);
        camera.setParameters(params);
        camera.setDisplayOrientation(90);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera)
    {
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera)
    {
    }

    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera)
    {
    }

    @Override
    public void onClick(View v) {

    }
}
