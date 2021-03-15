package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSearch implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("full_name")
    @Expose
    private String fullName;

    public ItemSearch(String name, Integer id, Integer type, String fullName) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.fullName = fullName;
    }

    protected ItemSearch(Parcel in) {
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

    public String getFullName() {
        return fullName;
    }

    public String getName() {
        return name;
    }
    public Integer getType() {
        return type;
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