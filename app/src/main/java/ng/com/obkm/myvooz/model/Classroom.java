package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Classroom implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("floor")
    @Expose
    private Integer floor;

    protected Classroom(Parcel in) {

    }

    public Classroom(String name, Integer floor){
        this.name = name;
        this.floor = floor;
    }

    public static final Creator<Classroom> CREATOR = new Creator<Classroom>() {
        @Override
        public Classroom createFromParcel(Parcel in) {
            return new Classroom(in);
        }

        @Override
        public Classroom[] newArray(int size) {
            return new Classroom[size];
        }
    };

    public String getName() {
        return name;
    }
    public Integer getFloor() {
        return floor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}