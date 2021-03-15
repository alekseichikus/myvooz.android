package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthUser implements Parcelable {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("state")
    @Expose
    private Boolean state;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("name_rank")
    @Expose
    private String rank;
    @SerializedName("id_rank")
    @Expose
    private Integer idRank;
    @SerializedName("id_university")
    @Expose
    private Integer idUniversity;
    @SerializedName("name_university")
    @Expose
    private String nameUniversity;
    @SerializedName("id_group")
    @Expose
    private Integer idGroup;
    @SerializedName("id_group_of_user")
    @Expose
    private Integer idGroupOfUser;
    @SerializedName("info_groups_of_user")
    @Expose
    private GroupOfUser groupOfUser;
    @SerializedName("name_group")
    @Expose
    private String nameGroup;
    @SerializedName("is_request_confirm")
    @Expose
    private Boolean isRequestConfirm;
    @SerializedName("state_request_confirm")
    @Expose
    private Boolean stateRequestConfirm;
    @SerializedName("week")
    @Expose
    private Integer week;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ver_schedule")
    @Expose
    private Integer ver_schedule;

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("request_confirm_count")
    @Expose
    private Integer request_confirm_count;

    protected AuthUser(Parcel in) {
        accessToken = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        photo = in.readString();
        rank = in.readString();
        accessToken = in.readString();
        idRank = in.readInt();
        idGroup = in.readInt();
        week = in.readInt();
        nameGroup = in.readString();
    }

    public static final Creator<AuthUser> CREATOR = new Creator<AuthUser>() {
        @Override
        public AuthUser createFromParcel(Parcel in) {
            return new AuthUser(in);
        }

        @Override
        public AuthUser[] newArray(int size) {
            return new AuthUser[size];
        }
    };

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getPhoto() {
        return photo;
    }
    public String getRankName() {
        return rank;
    }
    public GroupOfUser getInfoGroupOfUser() {
        return groupOfUser;
    }

    public Integer getRankId() {
        return idRank;
    }

    public Integer getGroupId() {
        return idGroup;
    }
    public Integer getIdGroupOfUser() {
        return idGroupOfUser;
    }
    public Integer getVersionSchedule() {
        return ver_schedule;
    }
    public Integer getError() {
        return error;
    }

    public Integer getUniversityId() {
        return idUniversity;
    }
    public Integer getRequestConfirmCount() {
        return request_confirm_count;
    }

    public String getUniversityName() {
        return nameUniversity;
    }

    public Integer getWeek() {
        return week;
    }
    public Integer getId() {
        return id;
    }

    public String getGroupName() {
        return nameGroup;
    }

    public Boolean isRequestConfirm() {
        return isRequestConfirm;
    }

    public Boolean getStateRequestConfirm() {
        return stateRequestConfirm;
    }
    public Boolean getStateProfile() {
        return state;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public void setRankName(String rank) {
        this.rank = rank;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}