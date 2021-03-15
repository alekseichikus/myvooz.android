package ng.com.obkm.myvooz.profile.AddNotification.model;

import java.util.ArrayList;

public interface IAddNotificationModel {
    interface OnFinishedListener {
        void onFinished(Boolean state);

        void onFailure(Throwable t);
    }

    void uploadFile(String filepath);

    void addNotification(OnFinishedListener onFinishedListener, String access_token, Integer id_user, String text, ArrayList<Integer> images, ArrayList<Integer> users, Boolean important);
}
