package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupOfUser implements Parcelable {

    @SerializedName("id_creator")
    @Expose
    private Integer id_creator;

    @SerializedName("id_older")
    @Expose
    private Integer id_older;

    @SerializedName("id_group")
    @Expose
    private Integer id_group;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("count_users")
    @Expose
    private Integer count_users;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("userVeryShort")
    @Expose
    private UserVeryShort userVeryShort;

    public GroupOfUser(String image, String name, Integer id_group, Integer id_creator, Integer id_older, Integer error){
        this.name = name;
        this.id_group = id_group;
        this.id_creator = id_creator;
        this.image = image;
        this.id_older = id_older;
    }

    public static final Creator<GroupOfUser> CREATOR = new Creator<GroupOfUser>() {
        @Override
        public GroupOfUser createFromParcel(Parcel in) {
            return new GroupOfUser(in);
        }

        @Override
        public GroupOfUser[] newArray(int size) {
            return new GroupOfUser[size];
        }
    };

    public GroupOfUser(Parcel in) {
    }

    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
    public Integer getIdGroup() {
        return id_group;
    }
    public UserVeryShort getUserVeryShort() {
        return userVeryShort;
    }
    public Integer getId() {
        return id;
    }
    public Integer getIdCreator() {
        return id_creator;
    }
    public Integer getCountUsers() {
        return count_users;
    }

    public Integer getIdOlder() {
        return id_older;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}