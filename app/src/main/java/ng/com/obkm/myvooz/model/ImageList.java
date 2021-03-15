package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageList implements Parcelable, Serializable {

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("full_path")
    @Expose
    private String fullPath;

    @SerializedName("id")
    @Expose
    private Integer id;


    protected ImageList(Parcel in) {

    }

    public ImageList(String image, String fullPath, Integer id){
        this.path = image;
        this.id = id;
        this.fullPath = fullPath;
    }

    public static final Creator<ImageList> CREATOR = new Creator<ImageList>() {
        @Override
        public ImageList createFromParcel(Parcel in) {
            return new ImageList(in);
        }

        @Override
        public ImageList[] newArray(int size) {
            return new ImageList[size];
        }
    };

    public String getImage() {
        return path;
    }
    public String getFullPath() {
        return fullPath;
    }
    public Integer getId() {
        return id;
    }

    public void setImage(String image) {
        this.path = image;
    }
    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}