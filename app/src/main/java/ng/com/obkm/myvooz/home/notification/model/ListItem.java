package ng.com.obkm.myvooz.home.notification.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListItem implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    private Boolean state = false;
    private Boolean state_count = false;
    private Integer count = 0;
    private Boolean locked = false;

    public ListItem(String name, Integer id, Boolean state, Boolean state_count, Integer count, Boolean locked) {
        this.name = name;
        this.id = id;
        this.state = state;
        this.state_count = state_count;
        this.count = count;
        this.locked = locked;
    }

    protected ListItem(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public Boolean getState() {
        return state;
    }
    public Boolean isStateCount() {
        return state_count;
    }

    public Boolean isLocked() {
        return locked;
    }

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setState(Boolean state) {
        this.state = state;
    }

}