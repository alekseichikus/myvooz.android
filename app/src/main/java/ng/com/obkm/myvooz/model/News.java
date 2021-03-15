package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import ng.com.obkm.myvooz.home.news.model.INews;


public class News implements INews, Parcelable {
    private String image;
    private String title;
    private String logo_image;
    private String name;
    private String link;
    private ArrayList<Story> stories;

    public News(String image, String title, String logo_image, String name, ArrayList<Story> stories, String link) {
        this.image = image;
        this.title = title;
        this.logo_image = logo_image;
        this.name = name;
        this.stories = stories;
        this.link = link;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUnionImage() { return logo_image; }

    @Override
    public String getUnionName() {
        return name;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public ArrayList<Story> getStories() {
        return stories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}