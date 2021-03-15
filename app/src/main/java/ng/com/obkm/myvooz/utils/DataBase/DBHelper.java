package ng.com.obkm.myvooz.utils.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import ng.com.obkm.myvooz.model.ImageList;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class DBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database";
    public static final String TABLE_CONTACTS = "ru_notes";

    public static final String KEY_ID = "_id";
    public static final String KEY_ID_2 = "ids";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEXT = "text";
    public static final String KEY_NAME_OBJECT = "name_object";
    public static final String KEY_MARK_ME = "mark_me";
    public static final String KEY_DATE = "date";
    public static final String KEY_STATE_COMPLETED = "state_completed";
    public static final String KEY_ID_USER = "id_user";
    public static final String KEY_ID_OBJECT = "id_object";
    private SharedPreferences mSettings;

    private DBNoteImages dbNoteImages;
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbNoteImages = new DBNoteImages(context);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_TEXT + " text," + KEY_NAME_OBJECT + " text," + KEY_MARK_ME + " BOOLEAN," + KEY_STATE_COMPLETED + " BOOLEAN," + KEY_ID_OBJECT + " INTEGER," + KEY_ID_USER + " INTEGER," + KEY_ID_2 + " INTEGER," + KEY_DATE + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }

    public DBNoteImages getDBNoteImages(){
        return  dbNoteImages;
    }

    public ArrayList<Note> getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> array_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_ID_2, KEY_NAME, KEY_TEXT, KEY_NAME_OBJECT, KEY_MARK_ME, KEY_DATE, KEY_STATE_COMPLETED, KEY_ID_USER, KEY_ID_OBJECT}, null, null, null, null, null);
        cursor.moveToFirst();

        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_2);
        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int textIndex = cursor.getColumnIndex(DBHelper.KEY_TEXT);
        int nameObjectIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_OBJECT);
        int markMeIndex = cursor.getColumnIndex(DBHelper.KEY_MARK_ME);
        int idUserIndex = cursor.getColumnIndex(DBHelper.KEY_ID_USER);
        int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
        int stateCompletedIndex = cursor.getColumnIndex(DBHelper.KEY_STATE_COMPLETED);
        int idObjectIndex = cursor.getColumnIndex(DBHelper.KEY_ID_OBJECT);
        while(cursor.isAfterLast() == false) {
            Boolean mark_me = cursor.getInt(markMeIndex) > 0 ? true : false;
            Boolean state_compl = cursor.getInt(stateCompletedIndex) > 0 ? true : false;
            Integer id_note = cursor.getInt(idIndex);
            ArrayList<ImageList> images = getDBNoteImages().getData(id_note);
            array_list.add(new Note(cursor.getString(nameObjectIndex), cursor.getString(nameIndex), cursor.getString(textIndex)
                    , mark_me, images, id_note, cursor.getString(dateIndex), state_compl, cursor.getInt(idUserIndex), cursor.getInt(idObjectIndex)));
            cursor.moveToNext();
        }
        cursor.close();
        return array_list;
    }

    public Calendar getFirstNoteDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_DATE, KEY_NAME, KEY_TEXT}, KEY_STATE_COMPLETED + "=0" , null, null, null, null);
        cursor.moveToFirst();

        int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int textIndex = cursor.getColumnIndex(DBHelper.KEY_TEXT);
        String name = "";
        String text = "";
        String date = "";

        Calendar current_calendar = Calendar.getInstance();
        current_calendar.setTimeInMillis(System.currentTimeMillis());
        current_calendar.add(Calendar.DAY_OF_YEAR, 1);

        Calendar select_calendar = null;

        while(cursor.isAfterLast() == false) {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                String d = cursor.getString(dateIndex);
                calendar.setTime(sdf.parse(d));
                date = d;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(current_calendar.before(calendar)){
                if(calendar.before(select_calendar) || select_calendar == null){
                    select_calendar = calendar;
                    name = cursor.getString(nameIndex);
                    text = cursor.getString(textIndex);
                }
            }
            cursor.moveToNext();
        }
        cursor.close();

        if(select_calendar != null){
            MemoryOperation memoryOperation = new MemoryOperation(context);
            Set<String> strings = new HashSet<String>();

            String ii = Constants.getMonthName(select_calendar.get(Calendar.MONTH));
            String minutes = select_calendar.get(Calendar.MINUTE) < 10 ? "0" + select_calendar.get(Calendar.MINUTE) : String.valueOf(select_calendar.get(Calendar.MINUTE));

            mSettings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_1, "Срок заметки подходит к концу");
            editor.putString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_2, name);
            editor.putString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_3, text);
            editor.putString(Constants.APP_PREFERENCES_NOTIF_NOTE_STRING_4, Constants.getDayOfWeekNameShort(select_calendar.get(Calendar.DAY_OF_WEEK)) + ", " + select_calendar.get(Calendar.DAY_OF_MONTH) + "."+ ii + " в "+ select_calendar.get(Calendar.HOUR_OF_DAY) + ":"+ minutes);
            editor.commit();
            memoryOperation.setNotificationStrings(strings);

            select_calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        return select_calendar;
    }

    public ArrayList<Integer> getIDNotesWithState(Integer state) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> array_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_ID_2}, KEY_STATE_COMPLETED + "="+state, null, null, null, null);
        cursor.moveToFirst();

        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_2);
        while(cursor.isAfterLast() == false) {
            array_list.add(cursor.getInt(idIndex));
            cursor.moveToNext();
        }
        cursor.close();
        return array_list;
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, note.getName());
        contentValues.put(KEY_ID_2, note.getId());
        contentValues.put(KEY_DATE, note.getDate());
        contentValues.put(KEY_MARK_ME, note.isMarkMe());
        contentValues.put(KEY_TEXT, note.getText());
        contentValues.put(KEY_STATE_COMPLETED, note.isStateCompleted());
        contentValues.put(KEY_NAME_OBJECT, note.getNameObject());
        contentValues.put(KEY_ID_OBJECT, note.getIdObject());

        for(ImageList imageList : note.getPhotos()){
            dbNoteImages.addImage(imageList, note.getId());
        }

        db.insert(TABLE_CONTACTS, null, contentValues);
        this.close();
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, note.getName());
        contentValues.put(KEY_ID_2, note.getId());
        contentValues.put(KEY_DATE, note.getDate());
        contentValues.put(KEY_MARK_ME, note.isMarkMe());
        contentValues.put(KEY_TEXT, note.getText());
        contentValues.put(KEY_STATE_COMPLETED, note.isStateCompleted());
        contentValues.put(KEY_NAME_OBJECT, note.getNameObject());
        contentValues.put(KEY_ID_OBJECT, note.getIdObject());

        for(ImageList imageList : note.getPhotos()){
            dbNoteImages.addImage(imageList, note.getId());
        }

        db.update(TABLE_CONTACTS, contentValues, KEY_ID_2 + "=" + note.getId(), null);
    }

    public void deleteNote(Integer id_note) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID_2 + " = "+id_note, null);
        dbNoteImages.deleteAllImagesFromNote(id_note);
    }

    public void changeStateCompletedNote(Integer id_note, Boolean state) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATE_COMPLETED, state);
        SQLiteDatabase db = this.getReadableDatabase();
        db.update(TABLE_CONTACTS, cv, KEY_ID_2 + " = "+id_note, null);
    }

    public void deleteAllNoteWithStateCompleted(Integer state){
        SQLiteDatabase db = this.getReadableDatabase();
        for(Integer items: getIDNotesWithState(state)){
            dbNoteImages.deleteAllImagesFromNote(items);
        }
        db.delete(TABLE_CONTACTS, KEY_STATE_COMPLETED + "="+state, null);
    }

    public void deleteAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
    }
}