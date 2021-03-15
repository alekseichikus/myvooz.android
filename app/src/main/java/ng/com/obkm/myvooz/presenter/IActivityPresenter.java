package ng.com.obkm.myvooz.presenter;

import android.app.Activity;
import android.content.SharedPreferences;

import ng.com.obkm.myvooz.model.GroupOfUser;

public interface IActivityPresenter {

    boolean isInternetConnection();

    void setLightStatusBar(Activity activity);

    void deleteAuthData(SharedPreferences mSettings);

    void setAuthInfo(String firstName, String lastName, Integer id, String userPhoto, String rankName, Integer rankId
            , Boolean state, String accessToken, Integer universityId, String universityName
            , Integer groupId, String groupName, Integer week, Integer idGroupOfUser, GroupOfUser groupOfUser, SharedPreferences mSettings);
}
