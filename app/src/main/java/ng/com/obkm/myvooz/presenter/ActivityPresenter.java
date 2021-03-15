package ng.com.obkm.myvooz.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.vk.sdk.VKSdk;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.home.news.model.INewsListModel;
import ng.com.obkm.myvooz.home.news.model.NewsListModel;
import ng.com.obkm.myvooz.model.GroupOfUser;
import ng.com.obkm.myvooz.utils.DataBase.DBHelper;
import ng.com.obkm.myvooz.utils.DataBase.DBNoteImages;
import ng.com.obkm.myvooz.utils.DataBase.DBScheduleGroup;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;

public class ActivityPresenter implements IActivityPresenter {

    private INewsListModel newsListModel;
    private Context context;
    private MemoryOperation memoryOperation;

    public ActivityPresenter(Context context){
        this.context = context;
        this.newsListModel = new NewsListModel();
        memoryOperation = new MemoryOperation(context);
    }

    @Override
    public boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void setLightStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorWhite));
    }

    @Override
    public void deleteAuthData(SharedPreferences mSettings) {
        VKSdk.logout();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(Constants.APP_PREFERENCES_NAME_PROFILE, memoryOperation.getNameGroupProfile());
        editor.putString(Constants.APP_PREFERENCES_PHOTO_PROFILE, "");
        editor.putString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "");
        editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE, 0);
        editor.putInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0);
        editor.commit();

        memoryOperation.setVersionSchedule(0);
        memoryOperation.setIdGroupOfUser(0);

        DBScheduleGroup dbScheduleGroup = new DBScheduleGroup(context);
        DBHelper dbHelper = new DBHelper(context);
        DBNoteImages dbNoteImages = new DBNoteImages(context);
        dbHelper.deleteAllNotes();
        dbScheduleGroup.deleteAllSchedules();
        dbNoteImages.deleteAllImages();
    }

    public void setAuthInfo(String firstName, String lastName, Integer id, String userPhoto, String rankName, Integer rankId
            , Boolean state, String accessToken, Integer universityId, String universityName
            , Integer groupId, String groupName, Integer week, Integer idGroupOfUser, GroupOfUser groupOfUser, SharedPreferences mSettings){

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(Constants.APP_PREFERENCES_NAME_PROFILE, firstName + " " + lastName);
        editor.putString(Constants.APP_PREFERENCES_FIRST_NAME_PROFILE, firstName);
        editor.putString(Constants.APP_PREFERENCES_LAST_NAME_PROFILE, lastName);
        editor.putString(Constants.APP_PREFERENCES_PHOTO_PROFILE, userPhoto);
        editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE,  groupName);
        editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE,  universityName);
        editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_NAME_PROFILE,  groupOfUser.getName());
        editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_NAME_OLDER_PROFILE,  groupOfUser.getUserVeryShort().getName());
        editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_OLDER_PROFILE,  groupOfUser.getUserVeryShort().getPhoto());
        editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, universityId);
        editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE, groupOfUser.getIdOlder());
        editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, groupId);
        editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_COUNT_USERS_PROFILE, groupOfUser.getCountUsers());
        editor.putInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, id);
        editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_VALUE, idGroupOfUser);
        editor.putString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, accessToken);
        editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_PROFILE, groupOfUser.getImage());
        editor.commit();
    }
}
