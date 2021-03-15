package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVeryShort implements Parcelable {

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private Integer id;

    protected UserVeryShort(Parcel in) {

        photo = in.readString();
    }

    public UserVeryShort(String photo, Integer id, String name){
        this.photo = photo;
        this.id = id;
    }

    public static final Creator<UserVeryShort> CREATOR = new Creator<UserVeryShort>() {
        @Override
        public UserVeryShort createFromParcel(Parcel in) {
            return new UserVeryShort(in);
        }

        @Override
        public UserVeryShort[] newArray(int size) {
            return new UserVeryShort[size];
        }
    };

    public String getPhoto() {
        return photo;
    }
    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}