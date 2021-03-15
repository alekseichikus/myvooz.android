package ng.com.obkm.myvooz.profile.AddNotification.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ng.com.obkm.myvooz.Adapter.UsersListAdapter;
import ng.com.obkm.myvooz.model.UserVeryShort;
import ng.com.obkm.myvooz.note.UserCheckList.view.UsersCheckListActivity;
import ng.com.obkm.myvooz.profile.AddNotification.Camera.CameraActivity;
import ng.com.obkm.myvooz.model.ImageList;
import ng.com.obkm.myvooz.Adapter.ImageListAdapter;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.home.presenter.IHomeFragmentPresenter;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.AddNotification.presenter.AddNotificationPresenter;
import ng.com.obkm.myvooz.profile.AddNotification.presenter.IAddNotificationPresenter;
import ng.com.obkm.myvooz.utils.FilePath;
import ng.com.obkm.myvooz.utils.MemoryOperation;

import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class AddNotificationActivity extends AppCompatActivity implements IAddNotificationView{

    EditText editText;
    CardView sendButton;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;

    ImageListAdapter adapter;
    UsersListAdapter usersAdapter;
    CardView loadImageButton;
    //CardView loadFileButton;
    CardView cameraButton;
    CardView loadDataBlock;
    CardView importantButton;
    CardView userListButton;
    CardView infoUserListButton;
    ImageView loadDataBlockImage;
    ImageView importantImage;
    TextView userListButtonTitle;
    CardView removeUserListButton;
    String path;
    private MemoryOperation memoryOperation;
    private IActivityPresenter activityPresenter;
    private IAddNotificationPresenter addNotificationPresenter;
    ArrayList<ImageList> items = new ArrayList<>();
    ArrayList<UserVeryShort> user_items = new ArrayList<>();

    private ArrayList<HashMap<String, String>> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        ActivityCompat.requestPermissions(AddNotificationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        initUI();
        setListeners();
        activityPresenter = new ActivityPresenter(this);
        memoryOperation = new MemoryOperation(this);
        memoryOperation.setUserList(new ArrayList());
        memoryOperation.setUserPhotoList(new ArrayList());
        addNotificationPresenter = new AddNotificationPresenter(this, this, memoryOperation, this);

        path = memoryOperation.getSelectImageNotification();
        activityPresenter.setLightStatusBar(this);
    }

    public void initUI(){
        editText = findViewById(R.id.edit_text);
        sendButton = findViewById(R.id.send_button);
        loadImageButton = findViewById(R.id.load_image_button);
        //loadFileButton = findViewById(R.id.load_file_button);
        cameraButton = findViewById(R.id.camera_button);
        userListButton = findViewById(R.id.user_list_button);
        infoUserListButton = findViewById(R.id.info_user_list_button);
        importantImage = findViewById(R.id.important_image);
        importantButton = findViewById(R.id.important_button);
        loadDataBlock = findViewById(R.id.load_data_block);
        loadDataBlockImage = findViewById(R.id.load_data_block_image);
        userListButtonTitle = findViewById(R.id.user_list_button_title);
        removeUserListButton = findViewById(R.id.remove_user_list_button);

        recyclerView2 = findViewById(R.id.user_list_recycler_view);
        recyclerView2.setLayoutManager(new LinearLayoutManager(AddNotificationActivity.this, LinearLayoutManager.HORIZONTAL, false));
        usersAdapter = new UsersListAdapter(this, user_items);
        recyclerView2.setAdapter(usersAdapter);

        recyclerView = findViewById(R.id.photos);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddNotificationActivity.this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageListAdapter(AddNotificationActivity.this, items, 1);
        recyclerView.setAdapter(adapter);
    }

    public void setListeners(){
        CardView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        removeUserListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                user_items.clear();
                removeUserListButton.setVisibility(View.GONE);
                userListButtonTitle.setVisibility(View.VISIBLE);
                addNotificationPresenter.addIdUsers(new ArrayList<>());
                usersAdapter.notifyDataSetChanged();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")){
                    addNotificationPresenter.sendNotificationFromServer();
                }
                else{
                    Toast.makeText(AddNotificationActivity.this, "Введите текст", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNotificationActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        infoUserListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AddNotificationActivity.this);
                dialog.setContentView(R.layout.dialog_info_user_list);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                CardView b = dialog.findViewById(R.id.close_button);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        userListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNotificationActivity.this, UsersCheckListActivity.class);
                startActivity(intent);
            }
        });

        importantButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNotificationPresenter.checkImportant();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        Uri selectedFileUri = data.getData();
        String filename = FilePath.getRealPathFromURI_API19(this, data.getData());
        addNotificationPresenter.checkActivityResult(requestCode, data, filename);
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "Не хватает прав", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(path != memoryOperation.getSelectImageNotification()){
            path = memoryOperation.getSelectImageNotification();
            addNotificationPresenter.addImage(path);
        }
        if(memoryOperation.getUserList().size() != 0){
            user_items.clear();

            ArrayList<Integer> iu = memoryOperation.getUserList();
            ArrayList<String> su = memoryOperation.getUserPhotoList();

            for(int i=0; i < iu.size();i++){
                user_items.add(new UserVeryShort(su.get(i), iu.get(i), ""));
            }
            addNotificationPresenter.addIdUsers(user_items);

            if(user_items.size() != 0){
                removeUserListButton.setVisibility(View.VISIBLE);
                userListButtonTitle.setVisibility(View.GONE);
            }
            else{
                removeUserListButton.setVisibility(View.GONE);
                userListButtonTitle.setVisibility(View.VISIBLE);
            }


            usersAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgress() {
        final Animation animationRotateCorner = AnimationUtils.loadAnimation(
                this, R.anim.rotate_infinity);
        loadDataBlockImage.startAnimation(animationRotateCorner);
        loadImageButton.setEnabled(false);
        cameraButton.setEnabled(false);
        loadDataBlock.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if(items.size() < 4){
            loadImageButton.setEnabled(true);
            cameraButton.setEnabled(true);
        }

        loadDataBlock.setVisibility(View.GONE);
    }

    @Override
    public String getNotificationText() {
        return editText.getText().toString();
    }

    @Override
    public void onResponseFailure() {
        Toast.makeText(AddNotificationActivity.this, "Произошла какая-то бяка", Toast.LENGTH_LONG).show();
    }

    @Override
    public IHomeFragmentPresenter getHomeFragmentPresenter() {
        return null;
    }

    @Override
    public void onFinished() {
        Toast.makeText(AddNotificationActivity.this, "Уведомление отправлено", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void addImage(ImageList image) {
        items.add(image);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setColorImportantButton(Integer color) {
        importantButton.setCardBackgroundColor(color);
    }
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            //dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SITE_ADDRESS);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("vrgergerg", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            //dialog.dismiss();
            return serverResponseCode;
        }
    }
}
