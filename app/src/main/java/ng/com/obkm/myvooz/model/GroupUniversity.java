package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupUniversity implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    public GroupUniversity(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public GroupUniversity(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupUniversity> CREATOR = new Creator<GroupUniversity>() {
        @Override
        public GroupUniversity createFromParcel(Parcel in) {
            return new GroupUniversity(in);
        }

        @Override
        public GroupUniversity[] newArray(int size) {
            return new GroupUniversity[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
