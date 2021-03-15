package ng.com.obkm.myvooz.Adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ng.com.obkm.myvooz.model.DaySchedule;

public class WeekSchedule implements Parcelable {

    @SerializedName("day_schedule")
    @Expose
    private ArrayList<ArrayList<DaySchedule>> daySchedule = null;

    protected WeekSchedule(Parcel in) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeekSchedule> CREATOR = new Creator<WeekSchedule>() {
        @Override
        public WeekSchedule createFromParcel(Parcel in) {
            return new WeekSchedule(in);
        }

        @Override
        public WeekSchedule[] newArray(int size) {
            return new WeekSchedule[size];
        }
    };

    public ArrayList<ArrayList<DaySchedule>> getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(ArrayList<ArrayList<DaySchedule>> daySchedule) {
        this.daySchedule = daySchedule;
    }
}