package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IniviteData implements Parcelable {

    public enum Errors{
        ERROR_IS_NOT_EXIST,
        ERROR_LINK_IS_NOT_VALID,
        ERROE_LINK_DOES_NOT_EXIST
    }

    @SerializedName("groupOfUser")
    @Expose
    private GroupOfUser groupOfUser;

    @SerializedName("infoGroup")
    @Expose
    private InfoGroup infoGroup;

    @SerializedName("error")
    @Expose
    private Integer error;

    public static final Creator<IniviteData> CREATOR = new Creator<IniviteData>() {
        @Override
        public IniviteData createFromParcel(Parcel in) {
            return new IniviteData(in);
        }

        @Override
        public IniviteData[] newArray(int size) {
            return new IniviteData[size];
        }
    };

    public IniviteData(Parcel in) {
    }

    public GroupOfUser getGroupOfUser() {
        return groupOfUser;
    }
    public InfoGroup getInfoGroup() {
        return infoGroup;
    }

    public Errors getErrorType() {
        return Errors.values()[error];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}