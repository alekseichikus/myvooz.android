package ng.com.obkm.myvooz.utils.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ng.com.obkm.myvooz.utils.DataBase.DBHelper;

public class CreateNotification {

    Context context;

    public CreateNotification(Context context){
        this.context = context;
    }

    public void createNotif(){
        Intent intent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        DBHelper dbHelper = new DBHelper(context);
        Calendar c = dbHelper.getFirstNoteDate();
        if(c != null){
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }
}
