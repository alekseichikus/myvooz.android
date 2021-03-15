package ng.com.obkm.myvooz.home.news.model;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.Story;

public interface INews {
    String getImage();
    String getTitle();
    String getUnionImage();
    String getUnionName();
    String getLink();
    ArrayList<Story> getStories();
}
