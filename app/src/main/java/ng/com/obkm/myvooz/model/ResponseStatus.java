package ng.com.obkm.myvooz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseStatus implements Parcelable {

    @SerializedName("state")
    @Expose
    private Boolean state;


    protected ResponseStatus(Parcel in) {

    }

    public static final Creator<ResponseStatus> CREATOR = new Creator<ResponseStatus>() {
        @Override
        public ResponseStatus createFromParcel(Parcel in) {
            return new ResponseStatus(in);
        }

        @Override
        public ResponseStatus[] newArray(int size) {
            return new ResponseStatus[size];
        }
    };



    public Boolean getState() {
        return state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}