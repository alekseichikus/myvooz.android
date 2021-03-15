package ng.com.obkm.myvooz.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeeksSchedule implements Parcelable {

    @SerializedName("week_schedule")
    @Expose
    private ArrayList<sDaySchedule> weekSchedule = null;
    @SerializedName("number_day")
    @Expose
    private Integer numberDay;
    @SerializedName("number_dayofweek")
    @Expose
    private ArrayList<String> numberDayofweek = null;

    protected WeeksSchedule(Parcel in) {
        if (in.readByte() == 0) {
            numberDay = null;
        } else {
            numberDay = in.readInt();
        }
        numberDayofweek = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (numberDay == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numberDay);
        }
        dest.writeStringList(numberDayofweek);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeeksSchedule> CREATOR = new Creator<WeeksSchedule>() {
        @Override
        public WeeksSchedule createFromParcel(Parcel in) {
            return new WeeksSchedule(in);
        }

        @Override
        public WeeksSchedule[] newArray(int size) {
            return new WeeksSchedule[size];
        }
    };

    public ArrayList<sDaySchedule> getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(ArrayList<sDaySchedule> weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public Integer getNumberDay() {
        return numberDay;
    }

    public void setNumberDay(Integer numberDay) {
        this.numberDay = numberDay;
    }

    public ArrayList<String> getNumberDayofweek() {
        return numberDayofweek;
    }

    public void setNumberDayofweek(ArrayList<String> numberDayofweek) {
        this.numberDayofweek = numberDayofweek;
    }

}