package ng.com.obkm.myvooz.profile.AddNotification.presenter;

import android.content.Intent;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.UserVeryShort;

public interface IAddNotificationPresenter {

    void onDestroy();

    void sendNotificationFromServer();

    void addImage(String path);
    void checkActivityResult(int requestCode, Intent data, String filename);
    void checkImportant();
    void addIdUsers(ArrayList<UserVeryShort> id_users);
}
