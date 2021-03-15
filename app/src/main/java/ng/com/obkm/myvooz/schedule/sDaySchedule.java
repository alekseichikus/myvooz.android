package ng.com.obkm.myvooz.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ng.com.obkm.myvooz.model.DaySchedule;

public class sDaySchedule implements Parcelable {

    @SerializedName("schedule")
    @Expose
    private ArrayList<ArrayList<DaySchedule>> schedule = null;
    @SerializedName("name_day")
    @Expose
    private String nameDay;

    public sDaySchedule(ArrayList<ArrayList<DaySchedule>> schedule, String nameDay) {
        this.schedule = schedule;
        this.nameDay = nameDay;
    }

    public sDaySchedule(Parcel in) {
        nameDay = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameDay);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<sDaySchedule> CREATOR = new Creator<sDaySchedule>() {
        @Override
        public sDaySchedule createFromParcel(Parcel in) {
            return new sDaySchedule(in);
        }

        @Override
        public sDaySchedule[] newArray(int size) {
            return new sDaySchedule[size];
        }
    };

    public ArrayList<ArrayList<DaySchedule>> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<ArrayList<DaySchedule>> schedule) {
        this.schedule = schedule;
    }

    public String getNameDay() {
        return nameDay;
    }

    public void setNameDay(String nameDay) {
        this.nameDay = nameDay;
    }

}