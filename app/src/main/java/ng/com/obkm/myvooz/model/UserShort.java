package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserShort implements Parcelable {

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("id_user_select")
    @Expose
    private Integer id_user_select;

    private Boolean check = false;

    protected UserShort(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        photo = in.readString();
    }

    public UserShort(String firstName, String lastName, String photo, String date, Integer id, Integer id_user_select, Boolean check){
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.date = date;
        this.id = id;
        this.check = check;
        this.id_user_select = id_user_select;
    }

    public static final Creator<UserShort> CREATOR = new Creator<UserShort>() {
        @Override
        public UserShort createFromParcel(Parcel in) {
            return new UserShort(in);
        }

        @Override
        public UserShort[] newArray(int size) {
            return new UserShort[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }
    public String getPhoto() {
        return photo;
    }
    public String getDate() {
        return date;
    }
    public Integer getId() {
        return id;
    }
    public Integer getIdUserSelect() {
        return id_user_select;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check){
        this.check = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}