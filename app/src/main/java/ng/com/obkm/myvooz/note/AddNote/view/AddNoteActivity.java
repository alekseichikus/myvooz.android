package ng.com.obkm.myvooz.note.AddNote.view;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import ng.com.obkm.myvooz.Adapter.ImageListAdapter;
import ng.com.obkm.myvooz.Adapter.ListAdapter;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.SearchActivity;
import ng.com.obkm.myvooz.home.notification.model.ListItem;
import ng.com.obkm.myvooz.home.presenter.IHomeFragmentPresenter;
import ng.com.obkm.myvooz.model.ImageList;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.model.UserVeryShort;
import ng.com.obkm.myvooz.note.AddNote.presenter.AddNotePresenter;
import ng.com.obkm.myvooz.note.AddNote.presenter.IAddNotePresenter;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.AddNotification.Camera.CameraActivity;
import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.utils.FilePath;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.view.DatePickerDialogFragment;
import ng.com.obkm.myvooz.view.TimePickerDialogFragment;
import ng.com.obkm.myvooz.view.presenter.IDatePickerPresenter;
import ng.com.obkm.myvooz.view.presenter.ITimePickerPresenter;

import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class AddNoteActivity extends AppCompatActivity implements IAddNoteView, IDatePickerPresenter, ITimePickerPresenter {

    EditText editText;
    EditText editText2;
    CardView sendButton;
    RecyclerView recyclerView;
    ArrayList<ListItem> list_items = new ArrayList<>();

    ImageListAdapter adapter;
    CardView loadImageButton;
    //CardView loadFileButton;
    CardView cameraButton;
    CardView loadDataBlock;
    ImageView loadDataBlockImage;
    RelativeLayout selectObjectButton;
    RelativeLayout dateSelectButton;
    ImageView importantImage;
    String path;
    private MemoryOperation memoryOperation;
    private IActivityPresenter activityPresenter;
    private IAddNotePresenter addNotificationPresenter;
    private Integer id_object = 0;
    ArrayList<ImageList> items = new ArrayList<>();
    ArrayList<UserVeryShort> user_items = new ArrayList<>();
    SharedPreferences mSettings;
    TextView objectName;
    TextView dateText;
    Calendar calendar;
    private ListAdapter adapterList;
    private Calendar calendar_select;

    private ArrayList<HashMap<String, String>> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        memoryOperation = new MemoryOperation(this);
        addNotificationPresenter = new AddNotePresenter(this, this, memoryOperation, this);

        initUI();
        setListeners();
        activityPresenter = new ActivityPresenter(this);

        memoryOperation.setUserList(new ArrayList());
        memoryOperation.setUserPhotoList(new ArrayList());

        mSettings = PreferenceManager.getDefaultSharedPreferences(AddNoteActivity.this);

        path = memoryOperation.getSelectImageNotification();
        activityPresenter.setLightStatusBar(this);

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(Constants.APP_PREFERENCES_OBJECT_ID_INTERMEDIATE_VALUE, 0);
        editor.putString(Constants.APP_PREFERENCES_OBJECT_NAME_INTERMEDIATE_VALUE, "");
        editor.commit();

        Boolean lock = false;
        if(memoryOperation.getIdGroupOfUser().equals(0)){
            lock = true;
        }

        String nameGroup = memoryOperation.getNameGroupProfile();
        if(!memoryOperation.getIdGroupOfUser().equals(0)){
            nameGroup = memoryOperation.getGroupOfUserNameProfile();
        }

        list_items.add(new ListItem("Личное", 1, true, false, 0, false));
        list_items.add(new ListItem(nameGroup, 0, false, false, 0, lock));
    }

    public void initUI(){
        editText = findViewById(R.id.edit_text);
        editText2 = findViewById(R.id.edit_text2);
        sendButton = findViewById(R.id.send_button);
        loadImageButton = findViewById(R.id.load_image_button);
        //loadFileButton = findViewById(R.id.load_file_button);
        cameraButton = findViewById(R.id.camera_button);
        importantImage = findViewById(R.id.important_image);
        dateText = findViewById(R.id.date_select_text);
        dateSelectButton = findViewById(R.id.date_select_button);
        objectName = findViewById(R.id.name_object_select);
        loadDataBlock = findViewById(R.id.load_data_block);
        selectObjectButton = findViewById(R.id.select_object_button);
        loadDataBlockImage = findViewById(R.id.load_data_block_image);

        recyclerView = findViewById(R.id.photos);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddNoteActivity.this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageListAdapter(AddNoteActivity.this, items, 1);
        recyclerView.setAdapter(adapter);

        RecyclerView scheduleRecyclerView = findViewById(R.id.recycler_list_view);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterList = new ListAdapter(this, list_items, addNotificationPresenter.getListPresenter());
        scheduleRecyclerView.setAdapter(adapterList);
    }

    public void setListeners(){
        CardView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().length() > 0 && editText2.getText().toString().trim().length() > 0){
                    if(id_object != 0){
                        if(calendar_select != null){
                            if(activityPresenter.isInternetConnection()){
                                if(memoryOperation.getAccessToken() != ""){
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    dateFormat.setCalendar(calendar_select);
                                    calendar_select.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                                    addNotificationPresenter.sendNotificationFromServer(id_object, dateFormat.format(calendar_select.getTime())
                                            , editText.getText().toString(), editText2.getText().toString(), adapterList.getTypeSelectItem());

                                }
                                else{
                                    onResponseFailure("Необходимо авторизоваться");
                                }
                            }
                        }
                        else{
                            Toast.makeText(AddNoteActivity.this, "Срок выполнения не выбран", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(AddNoteActivity.this, "Предмет не выбран", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddNoteActivity.this, "Введите текст", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AddNoteActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        selectObjectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this, SearchActivity.class);
                intent.putExtra("type", "object_intermediate_value");
                startActivity(intent);
            }
        });

        dateSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment bottomSheet = new DatePickerDialogFragment(calendar, AddNoteActivity.this);
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
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

        SharedPreferences.Editor editor = mSettings.edit();
        if(id_object != mSettings.getInt(Constants.APP_PREFERENCES_OBJECT_ID_INTERMEDIATE_VALUE, 0)){
            objectName.setText(mSettings.getString(Constants.APP_PREFERENCES_OBJECT_NAME_INTERMEDIATE_VALUE, ""));
            id_object = mSettings.getInt(Constants.APP_PREFERENCES_OBJECT_ID_INTERMEDIATE_VALUE, 0);
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
    public void onResponseFailure(String text) {
        Toast.makeText(AddNoteActivity.this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public IHomeFragmentPresenter getHomeFragmentPresenter() {
        return null;
    }

    @Override
    public void onFinished(String text) {
        Toast.makeText(AddNoteActivity.this, text, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void addImage(ImageList image) {
        items.add(image);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addNoteFromServer(Note note) {
        Intent intent = new Intent();
        intent.putExtra(Note.class.getSimpleName(),  note);
        setResult(RESULT_OK, intent);
        finish();
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

    @Override
    public void changeDate(Calendar calendar) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                changeTime(hourOfDay, minute);
            }
        };
        TimePickerDialogFragment timePickerDialog = new TimePickerDialogFragment(this, calendar, this);
        timePickerDialog.show(getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void changeTime(Integer hour, Integer minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Calendar ccc = (Calendar) calendar.clone();
        ccc.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        Calendar cc = Calendar.getInstance();
        cc.setTimeInMillis(System.currentTimeMillis());
        cc.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        if(ccc.after(cc)){
            String ii = Constants.getMonthName(calendar.get(Calendar.MONTH));
            String minutes = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
            dateText.setText(Constants.getDayOfWeekNameShort(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + "."+ ii + " в "+ calendar.get(Calendar.HOUR_OF_DAY) + ":"+ minutes);
            calendar_select = (Calendar) calendar.clone();
        }
        else{
            Toast.makeText(AddNoteActivity.this, "Вы выбрали уже прошедшее время. Пожалуйста, повторите попытку.", Toast.LENGTH_LONG).show();
        }
    }
}
