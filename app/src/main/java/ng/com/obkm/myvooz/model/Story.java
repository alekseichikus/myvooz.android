package ng.com.obkm.myvooz.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Story {
    private String image;

    @SerializedName("body")
    private String text;

    public Story(String image, String text){
        this.image = image;
        this.text = text;
    }


    public String getImage() {
        return image;
    }

}
