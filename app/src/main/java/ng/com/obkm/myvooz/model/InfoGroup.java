package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoGroup implements Parcelable {

    @SerializedName("name_university")
    @Expose
    private String name_university;

    @SerializedName("name_group")
    @Expose
    private String name_group;

    @SerializedName("id_university")
    @Expose
    private Integer id_university;

    @SerializedName("id_group")
    @Expose
    private Integer id_group;


    public static final Creator<InfoGroup> CREATOR = new Creator<InfoGroup>() {
        @Override
        public InfoGroup createFromParcel(Parcel in) {
            return new InfoGroup(in);
        }

        @Override
        public InfoGroup[] newArray(int size) {
            return new InfoGroup[size];
        }
    };

    public InfoGroup(Parcel in) {
    }

    public String getNameGroup() {
        return name_group;
    }
    public String getNameUniversity() {
        return name_university;
    }
    public Integer getIdUniversity() {
        return id_university;
    }
    public Integer getIdGroup() {
        return id_group;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}