package ng.com.obkm.myvooz.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class    Note implements Serializable {

    @SerializedName("name_object")
    @Expose
    private String name_object;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("mark_me")
    @Expose
    private Boolean mark_me;

    @SerializedName("state_completed")
    @Expose
    private Boolean state_completed;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("id_user")
    @Expose
    private Integer id_user;

    @SerializedName("id_object")
    @Expose
    private Integer id_object;

    @SerializedName("images")
    @Expose
    private ArrayList<ImageList> images;

    private Boolean check = false;

    protected Note(Parcel in) {

    }

    public Note(String name_object, String name, String text, Boolean mark_me, ArrayList<ImageList> images, Integer id, String date, Boolean state_completed, Integer id_user, Integer id_object){
        this.name_object = name_object;
        this.name = name;
        this.text = text;
        this.mark_me = mark_me;
        this.images = images;
        this.id = id;
        this.date = date;
        this.state_completed = state_completed;
        this.id_user = id_user;
        this.id_object = id_object;
    }
    public String getDate() {
        return date;
    }
    public String getText() {
        return text;
    }
    public String getNameObject() {
        return name_object;
    }
    public String getName() {
        return name;
    }
    public Boolean isMarkMe() {
        return mark_me;
    }
    public Boolean isStateCompleted() {
        return state_completed;
    }

    public ArrayList<ImageList> getPhotos() {
        return images;
    }

    public Integer getId() {
        return id;
    }
    public Integer getIdUser() {
        return id_user;
    }
    public Integer getIdObject() {
        return id_object;
    }

    public void setDate(String date){
        this.date = date;
    }
    public void setStateCompleted(Boolean state_completed){
        this.state_completed = state_completed;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check){
        this.check = check;
    }

}