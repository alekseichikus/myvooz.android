package ng.com.obkm.myvooz.utils.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

import ng.com.obkm.myvooz.model.DaySchedule;

public class DBScheduleGroup extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database2";
    public static final String TABLE_CONTACTS = "ru_schedules";

    public static final String KEY_ID = "_id";
    public static final String KEY_ID_SCHEDULE = "ids";
    public static final String KEY_NAME_TEACHER = "name_teacher";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_WDAY = "wday";
    public static final String KEY_NAME_CLASSROOM = "name_classroom";
    public static final String KEY_NAME_OBJECT = "name_object";
    public static final String KEY_MIN_WEEK = "min_week";
    public static final String KEY_MAX_WEEK = "max_week";
    public static final String KEY_NAME_TYPE = "name_type";
    public static final String KEY_FIRST_TIME = "first_time";
    public static final String KEY_LAST_TIME = "last_time";

    private Context context;

    public DBScheduleGroup(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME_TEACHER + " text," + KEY_NAME_CLASSROOM + " text," + KEY_NAME_OBJECT
                + " text," + KEY_NUMBER + " INTEGER," + KEY_WDAY + " INTEGER," + KEY_MIN_WEEK + " INTEGER," + KEY_MAX_WEEK
                + " INTEGER," + KEY_ID_SCHEDULE + " INTEGER," + KEY_FIRST_TIME + " STRING," + KEY_LAST_TIME + " STRING," + KEY_NAME_TYPE + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }

    public ArrayList<ArrayList<DaySchedule>> getData(Calendar calendar) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ArrayList<DaySchedule>> array_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_NAME_TEACHER, KEY_ID_SCHEDULE, KEY_NUMBER, KEY_NAME_CLASSROOM, KEY_NAME_OBJECT
                        , KEY_NAME_TYPE, KEY_WDAY, KEY_MIN_WEEK, KEY_MAX_WEEK, KEY_FIRST_TIME, KEY_LAST_TIME}
        , KEY_WDAY + "=" + (getNumberOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))  + " AND " + KEY_MIN_WEEK + "<=" + calendar.get(Calendar.WEEK_OF_YEAR) + " AND " + KEY_MAX_WEEK + ">=" + calendar.get(Calendar.WEEK_OF_YEAR), null, null, null, null);
        cursor.moveToFirst();

        int nameTeacherIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_NAME_TEACHER);
        int idScheduleIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_ID_SCHEDULE);
        int numberIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_NUMBER);
        int nameClassroomIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_NAME_CLASSROOM);
        int nameObjectIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_NAME_OBJECT);
        int nameTypeIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_NAME_TYPE);
        int wdayIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_WDAY);
        int minWeekIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_MIN_WEEK);
        int maxWeekIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_MAX_WEEK);
        int firstTimeIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_FIRST_TIME);
        int lastTimeIndex = cursor.getColumnIndex(DBScheduleGroup.KEY_LAST_TIME);

        ArrayList<Integer> pos = new ArrayList<>();

        while(cursor.isAfterLast() == false) {
            Integer getPosNumb = getPositionNumberSchedule(pos, cursor.getInt(numberIndex));
            if(!getPosNumb.equals(-1)){

                array_list.get(getPosNumb).add(new DaySchedule(cursor.getInt(idScheduleIndex), cursor.getString(nameObjectIndex), cursor.getString(nameClassroomIndex)
                        , cursor.getString(nameTypeIndex), cursor.getString(nameTeacherIndex), cursor.getInt(numberIndex), cursor.getString(firstTimeIndex)
                        , cursor.getString(lastTimeIndex), cursor.getInt(wdayIndex), cursor.getInt(minWeekIndex), cursor.getInt(maxWeekIndex)));
            }
            else{
                pos.add(cursor.getInt(numberIndex));
                ArrayList<DaySchedule> ar = new ArrayList<>();
                ar.add(new DaySchedule(cursor.getInt(idScheduleIndex),
                        cursor.getString(nameObjectIndex), cursor.getString(nameClassroomIndex)
                        , cursor.getString(nameTypeIndex), cursor.getString(nameTeacherIndex), cursor.getInt(numberIndex)
                        , cursor.getString(firstTimeIndex), cursor.getString(lastTimeIndex), cursor.getInt(wdayIndex), cursor.getInt(minWeekIndex), cursor.getInt(maxWeekIndex)));
                array_list.add(ar);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return array_list;
    }

    private Integer getPositionNumberSchedule(ArrayList<Integer> pos, Integer number){
        for(int i =0; i< pos.size(); i++){
            if(pos.get(i).equals(number)){
                return i;
            }
        }
        return -1;
    }

    public void addSchedule(DaySchedule note) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME_TEACHER, note.getName_teacher());
        contentValues.put(KEY_ID_SCHEDULE, note.getId());
        contentValues.put(KEY_NUMBER, note.getNumber());
        contentValues.put(KEY_NAME_CLASSROOM, note.getClassroom());
        contentValues.put(KEY_NAME_OBJECT, note.getName());
        contentValues.put(KEY_NAME_TYPE, note.getNameType());
        contentValues.put(KEY_WDAY, note.getWday());
        contentValues.put(KEY_MIN_WEEK, note.getMinWeek());
        contentValues.put(KEY_MAX_WEEK, note.getMaxWeek());
        contentValues.put(KEY_FIRST_TIME, note.getTimeF());
        contentValues.put(KEY_LAST_TIME, note.getTimeL());

        db.insert(TABLE_CONTACTS, null, contentValues);
        this.close();
    }

    public void deleteAllSchedules() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
    }

    public Integer getNumberOfWeek(Integer now){
        if(now == 1)
            return 6;
        else if(now == 2){
            return 0;
        }
        else if(now == 3){
            return 1;
        }
        else if(now == 4){
            return 2;
        }
        else if(now == 5){
            return 3;
        }
        else if(now == 6){
            return 4;
        }
        else return 5;
    }
}