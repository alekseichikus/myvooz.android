package ng.com.obkm.myvooz.home.notification.model;

import java.util.ArrayList;

public interface IUserNotificationModel {
    interface OnFinishedListener {
        void onFinished(ArrayList<Notification> items);

        void onFailure(Throwable t);
    }

    void getUserNotificationList(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer type);
    void getUserWithUniversityNotificationList(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer type, Integer id_group);

    void getUniversityNotificationList(OnFinishedListener onFinishedListener, Integer id_university);
}
