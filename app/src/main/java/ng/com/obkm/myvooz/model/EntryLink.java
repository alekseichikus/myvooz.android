package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

import ng.com.obkm.myvooz.utils.Date.DateWithTimeZone;

public class EntryLink implements Parcelable {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("is_lock")
    @Expose
    private Boolean is_lock;

    public EntryLink(String code, String date, Boolean is_lock){
        this.code = code;
        this.date = date;
        this.is_lock = is_lock;
    }

    public static final Creator<EntryLink> CREATOR = new Creator<EntryLink>() {
        @Override
        public EntryLink createFromParcel(Parcel in) {
            return new EntryLink(in);
        }

        @Override
        public EntryLink[] newArray(int size) {
            return new EntryLink[size];
        }
    };

    public EntryLink(Parcel in) {
    }

    public String getCode() {
        return code;
    }
    public Boolean isLock() {
        return is_lock;
    }
    public Calendar getDate() {
        Calendar calendar = null;
        try {
            calendar = DateWithTimeZone.stringToCalendar(date, TimeZone.getTimeZone("Europe/Moscow"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}