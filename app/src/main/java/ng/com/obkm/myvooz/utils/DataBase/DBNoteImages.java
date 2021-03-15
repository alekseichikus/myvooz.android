package ng.com.obkm.myvooz.utils.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.ImageList;

public class DBNoteImages extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "databases";
    public static final String TABLE_CONTACTS = "ru_note_images";

    public static final String KEY_ID = "id";
    public static final String KEY_ID_NOTE = "id_note";
    public static final String KEY_PATH = "path";
    public static final String KEY_FULL_PATH = "full_path";
    public static final String KEY_ID_IMAGE = "id_image";

    public DBNoteImages(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_ID_NOTE + " INTEGER," + KEY_PATH + " text," + KEY_FULL_PATH + " text," + KEY_ID_IMAGE + " INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
    public ArrayList<ImageList> getData(Integer id_note) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ImageList> array_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_PATH, KEY_FULL_PATH, KEY_ID_IMAGE}, KEY_ID_NOTE + "=" + id_note, null, null, null, null);
        cursor.moveToFirst();

        int pathIndex = cursor.getColumnIndex(DBNoteImages.KEY_PATH);
        int fullPathIndex = cursor.getColumnIndex(DBNoteImages.KEY_FULL_PATH);
        int idImageIndex = cursor.getColumnIndex(DBNoteImages.KEY_ID_IMAGE);

        while(cursor.isAfterLast() == false) {

            array_list.add(new ImageList(cursor.getString(pathIndex), cursor.getString(fullPathIndex), cursor.getInt(idImageIndex)));
            cursor.moveToNext();
        }
        cursor.close();
        return array_list;
    }

    public void addImage(ImageList note, Integer id_note) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID_NOTE, id_note);
        contentValues.put(KEY_PATH, note.getImage());
        contentValues.put(KEY_FULL_PATH, note.getFullPath());
        contentValues.put(KEY_ID_IMAGE, note.getId());
        db.insert(TABLE_CONTACTS, null, contentValues);
    }

    public void deleteAllImagesFromNote(Integer id_note) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID_NOTE + "=" + id_note, null);
    }

    public void deleteAllImages() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
    }
}