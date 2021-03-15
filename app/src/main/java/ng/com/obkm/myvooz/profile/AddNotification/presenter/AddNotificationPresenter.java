package ng.com.obkm.myvooz.profile.AddNotification.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ng.com.obkm.myvooz.model.ImageList;
import ng.com.obkm.myvooz.model.UserVeryShort;
import ng.com.obkm.myvooz.utils.VolleyMultipartRequest;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.AddNotification.model.AddNotificationModel;
import ng.com.obkm.myvooz.profile.AddNotification.model.IAddNotificationModel;
import ng.com.obkm.myvooz.profile.AddNotification.view.IAddNotificationView;
import ng.com.obkm.myvooz.utils.MemoryOperation;

import static android.content.ContentValues.TAG;
import static com.vk.sdk.VKUIHelper.getApplicationContext;
import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class AddNotificationPresenter implements IAddNotificationPresenter, AddNotificationModel.OnFinishedListener {

    private IAddNotificationModel model;
    private IAddNotificationView view;
    private IActivityPresenter activityPresenter;
    private MemoryOperation memoryOperation;

    ArrayList<Integer> id_images = new ArrayList<>();
    ArrayList<Integer> id_users = new ArrayList<>();
    Boolean important_state = false;
    private final int GALLERY = 1;
    private String upload_URL = SITE_ADDRESS + "profile?";

    private RequestQueue rQueue;
    private Context context;
    private Activity activity;

    public AddNotificationPresenter(IAddNotificationView view, Context context, MemoryOperation memoryOperation, Activity activity){
        this.view = view;
        this.model = new AddNotificationModel();
        this.activityPresenter = new ActivityPresenter(context);
        this.memoryOperation = memoryOperation;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onFinished(Boolean state) {
        view.onFinished();
    }

    @Override
    public void onFailure(Throwable t) {

        if (view != null) {
            view.hideProgress();
            view.onResponseFailure();
        }
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public void sendNotificationFromServer() {
        if (view != null) {
            if(activityPresenter.isInternetConnection()){
                model.addNotification(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), view.getNotificationText(), id_images, id_users, important_state);
            }
            else{
                view.onResponseFailure();
            }
        }
    }

    @Override
    public void addImage(String path) {
        Bitmap bitmap = getBitmap(path);
        Bitmap scaledBitmap = scaleDown(bitmap, 1000, true);
        if(scaledBitmap.getByteCount() <= 5242880){
            view.showProgress();

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new com.android.volley.Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));

                                jsonObject.toString().replace("\\\\","");

                                if (jsonObject.getString("status").equals("true")) {

                                    String url = jsonObject.getString("path");
                                    String fullPath = jsonObject.getString("full_path");
                                    Integer id = jsonObject.getInt("id");
                                    view.addImage(new ImageList(url, fullPath, id));
                                    id_images.add(id);
                                    view.hideProgress();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("filename", new DataPart(imagename + ".png", getFileDataFromDrawable(scaledBitmap)));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(context);
            rQueue.add(volleyMultipartRequest);
        }
        else{
            Toast.makeText(context, "Файл должен быть не более 5 мб", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void checkActivityResult(int requestCode, Intent data, String filename) {
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                    // imageView.setImageBitmap(bitmap);
                    //Log.d("rgerhgerg", getFileName(contentURI));
                    //model.uploadFile(getRealPathFromUri(contentURI));

                    uploadImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode == 2){
            if (data != null) {

                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                // imageView.setImageBitmap(bitmap);

                model.uploadFile(filename);

                //uploadImage(bitmap);

            }
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    public void checkImportant() {
        if(important_state){
            view.setColorImportantButton(0xffc2cbd5);
            important_state=false;
            Toast.makeText(context, "Убрано из важных", Toast.LENGTH_SHORT).show();
        }
        else{
            view.setColorImportantButton(0xffeb4242);
            important_state=true;
            Toast.makeText(context, "Добавлен в важные", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addIdUsers(ArrayList<UserVeryShort> users) {
        id_users.clear();
        for(UserVeryShort userVeryShort : users){
            id_users.add(userVeryShort.getId());
        }
    }

    private static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private Bitmap getBitmap(String path) {
        Bitmap bitmap=null;
        File imgFile = new  File(path);

        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap ;
    }
    private void uploadImage(final Bitmap bitmap){
        Bitmap scaledBitmap = scaleDown(bitmap, 1000, true);
        if(scaledBitmap.getByteCount() <= 5242880){
            view.showProgress();

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new com.android.volley.Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));

                                jsonObject.toString().replace("\\\\","");

                                if (jsonObject.getString("status").equals("true")) {

                                    String url = jsonObject.getString("path");
                                    String fullPath = jsonObject.getString("full_path");
                                    Integer id = jsonObject.getInt("id");
                                    view.addImage(new ImageList(url, fullPath, id));
                                    id_images.add(id);
                                    view.hideProgress();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("filename", new DataPart(imagename + ".png", getFileDataFromDrawable(scaledBitmap)));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(context);
            rQueue.add(volleyMultipartRequest);
        }
        else{
            Toast.makeText(getApplicationContext(), "Файл должен быть не более 5 мб", Toast.LENGTH_SHORT).show();
        }
    }

}
