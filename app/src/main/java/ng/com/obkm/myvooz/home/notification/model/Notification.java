package ng.com.obkm.myvooz.home.notification.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.ImageList;

public class Notification implements Parcelable {

    @SerializedName("photo")
    @Expose
    private String photo;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("is_sent_to_me")
    @Expose
    private Boolean is_sent_to_me;

    @SerializedName("important_state")
    @Expose
    private Integer important_state;

    @SerializedName("images")
    @Expose
    private ArrayList<ImageList> images;


    protected Notification(Parcel in) {

    }

    public Notification(String title, String text, String photo, String link, ArrayList<ImageList> images, Integer important_state, Integer type, Boolean is_sent_to_me, String date){
        this.title = title;
        this.text = text;
        this.photo = photo;
        this.link = link;
        this.images = images;
        this.important_state = important_state;
        this.type = type;
        this.is_sent_to_me = is_sent_to_me;
        this.date = date;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getTitle() {
        return title;
    }
    public ArrayList<ImageList> getPhotos() {
        return images;
    }
    public String getPhoto() {
        return photo;
    }
    public String getDate() {
        return date;
    }
    public String getLink() {
        return link;
    }
    public String getText() {
        return text;
    }
    public Integer getImportantState() {
        return important_state;
    }
    public Integer getType() {
        return type;
    }
    public Boolean isSentToMe() {
        return is_sent_to_me;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}