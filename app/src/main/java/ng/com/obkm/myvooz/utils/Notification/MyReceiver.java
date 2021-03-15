package ng.com.obkm.myvooz.utils.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Set;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.utils.DataBase.DBHelper;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        MemoryOperation memoryOperation = new MemoryOperation(context);

        if(memoryOperation.isTermNoteSettingProfile()){
            DBHelper dbHelper = new DBHelper(context);
            Set<String> strings = memoryOperation.getNotificationStrings();

            Calendar c = dbHelper.getFirstNoteDate();

            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);

            String str1 = mSettings.getString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_1, "");

            String str2 = mSettings.getString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_2, "");

            String str3 = mSettings.getString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_3, "");

            String str4 = mSettings.getString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_4, "");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "noti")
                    .setSmallIcon(R.drawable.ic_logo)
                    .setContentTitle(str1)
                    .setContentText(str3 + " · " + str4)
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine(str3 + " · " + str4)
                            .addLine("Выполнить до: " + str2))
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            notificationManagerCompat.notify(200, builder.build());

            Intent intent2 = new Intent(context, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if(c != null){
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }
    }
}