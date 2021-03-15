package ng.com.obkm.myvooz.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mSettings.edit();
        MemoryOperation memoryOperation = new MemoryOperation(getApplicationContext());

        Intent intent = new Intent(this, MainActivity.class);

        if(remoteMessage.getData().get("type").equals("USER_RECEIVE_NOTIFICATION")){
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        else if(remoteMessage.getData().get("type").equals("NEW_USER_NOTE")){
            if(memoryOperation.isCreateNoteSettingProfile()){
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        }
        else if(remoteMessage.getData().get("type").equals("CHANGE_USER_NOTE")){

            if(memoryOperation.isChangeNoteSettingProfile()){
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        }
        else if(remoteMessage.getData().get("type").equals("CHANGE_USER_RANK")){

            if(memoryOperation.isChangeNoteSettingProfile()){
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

                editor.putString(Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_OLDER_PROFILE,  memoryOperation.getUserPhoto());
                editor.putInt(Constants.APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE, memoryOperation.getIdUserProfile());
                editor.commit();
            }
        }

        else if(remoteMessage.getData().get("type").equals("LEAVE_GROUP_OF_USER")){

            if(memoryOperation.isChangeNoteSettingProfile()){
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

                memoryOperation.setIdGroupOfUser(0);
                editor.commit();
            }
        }

        editor.commit();
    }

    public void showNotification(String title, String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "aleks")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_logo)
                .setAutoCancel(true)
                .setContentText(message);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999, builder.build());
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(250);
    }
}
