package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UsersListData implements Parcelable {


    @SerializedName("users")
    @Expose
    private ArrayList<UserShort> users;

    @SerializedName("next_page")
    @Expose
    private Boolean next_page;

    public static final Creator<UsersListData> CREATOR = new Creator<UsersListData>() {
        @Override
        public UsersListData createFromParcel(Parcel in) {
            return new UsersListData(in);
        }

        @Override
        public UsersListData[] newArray(int size) {
            return new UsersListData[size];
        }
    };

    public UsersListData(Parcel in) {
    }

    public ArrayList<UserShort> getUsers() {
        return users;
    }
    public Boolean isNextPage() {
        return next_page;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}