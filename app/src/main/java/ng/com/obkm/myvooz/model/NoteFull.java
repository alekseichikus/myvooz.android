package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NoteFull implements Parcelable {

    @SerializedName("notes")
    @Expose
    private ArrayList<Note> notes;

    @SerializedName("count_active")
    @Expose
    private Integer count_active;

    @SerializedName("count_completed")
    @Expose
    private Integer count_completed;

    private Boolean state = false;

    protected NoteFull(Parcel in) {

    }

    public static final Creator<NoteFull> CREATOR = new Creator<NoteFull>() {
        @Override
        public NoteFull createFromParcel(Parcel in) {
            return new NoteFull(in);
        }

        @Override
        public NoteFull[] newArray(int size) {
            return new NoteFull[size];
        }
    };

    public ArrayList<Note> getNotes() {
        return notes;
    }
    public Integer getCountActive() {
        return count_active;
    }
    public Integer getCountCompleted() {
        return count_completed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}