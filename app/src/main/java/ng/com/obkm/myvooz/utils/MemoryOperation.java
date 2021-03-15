package ng.com.obkm.myvooz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_FIRST_NAME_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_ID_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_NAME_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_COUNT_USERS_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_ID_CREATOR_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_NAME_OLDER_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_NAME_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_OLDER_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_PHOTO_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_GROUP_OF_USER_ID_VALUE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_ID_USERS_INTERMEDIATE_VALUE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_IS_HIDE_EMPTY_SCHEDULE_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_LAST_NAME_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_NAME_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_NOT_AUTH_DIALOG_HIDE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_NOT_AUTH_DIALOG_NOTE_HIDE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_NOT_CONFIRM_DIALOG_HIDE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_NOT_CONFIRM_NOTE_DIALOG_HIDE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_PHOTO_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_PHOTO_USERS_INTERMEDIATE_VALUE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CHANGE_NOTE_SWITCH;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CREATE_NOTE_SWITCH;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_TERM_NOTE_SWITCH;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_SELECT_IMAGE_NOTIFICATION;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_SHOW_EMPTY_LESSONS_BOOL;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_STATE_CREATE_NEW_NOTE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_TITLE_NOTIFICATION_INTERMEDIATE_VALUE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_USER_ID_PROFILE;
import static ng.com.obkm.myvooz.utils.Constants.APP_PREFERENCES_VERSION_SCHEDULE_VALUE;

public class MemoryOperation {

    SharedPreferences mSettings;

    public  MemoryOperation(Context context){
        this.mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public Integer getIdGroup() {
        return mSettings.getInt(APP_PREFERENCES_GROUP_ID_PROFILE, 0);
    }
    public Integer getSwitchHideEmptyLesson() {
        return mSettings.getInt(APP_PREFERENCES_IS_HIDE_EMPTY_SCHEDULE_PROFILE, 1);
    }
    public String getAccessToken() {
        return mSettings.getString(APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "");
    }
    public Integer getIdUserProfile() {
        return mSettings.getInt(APP_PREFERENCES_USER_ID_PROFILE, 0);
    }

    public Integer getGroupOfUserIdCreatorProfile() {
        return mSettings.getInt(APP_PREFERENCES_GROUP_OF_USER_ID_CREATOR_PROFILE, 0);
    }

    public String getGroupOfUserNameOlderProfile() {
        return mSettings.getString(APP_PREFERENCES_GROUP_OF_USER_NAME_OLDER_PROFILE, "");
    }

    public Boolean isShowEmptyLessons() {
        return mSettings.getBoolean(APP_PREFERENCES_SHOW_EMPTY_LESSONS_BOOL, false);
    }

    public void setShowEmptyLessons(Boolean path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_SHOW_EMPTY_LESSONS_BOOL, path);
        editor.commit();
    }

    public String getGroupOfUserPhotoOlderProfile() {
        return mSettings.getString(APP_PREFERENCES_GROUP_OF_USER_PHOTO_OLDER_PROFILE, "");
    }

    public Integer getGroupOfUserIdOlderProfile() {
        return mSettings.getInt(APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE, 0);
    }

    public void setGroupOfUserIdOlderProfile(Integer path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE, path);
        editor.commit();
    }
    public void setGroupOfUserNameOlderProfile(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_GROUP_OF_USER_NAME_OLDER_PROFILE, path);
        editor.commit();
    }

    public void setGroupOfUserPhotoOlderProfile(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_GROUP_OF_USER_PHOTO_OLDER_PROFILE, path);
        editor.commit();
    }

    public Integer getIdUniversityProfile() {
        return mSettings.getInt(APP_PREFERENCES_UNIVERSITY_ID_PROFILE, 0);
    }

    public String getNameUniversityProfile() {
        return mSettings.getString(APP_PREFERENCES_UNIVERSITY_NAME_PROFILE, "");
    }


    public String getNameGroupProfile() {
        return mSettings.getString(APP_PREFERENCES_GROUP_NAME_PROFILE, "");
    }

    public String getFirstNameProfile() {
        return mSettings.getString(APP_PREFERENCES_FIRST_NAME_PROFILE, "");
    }

    public String getLastNameProfile() {
        return mSettings.getString(APP_PREFERENCES_LAST_NAME_PROFILE, "");
    }

    public String getGroupPhotoProfile() {
        return mSettings.getString(APP_PREFERENCES_GROUP_OF_USER_PHOTO_PROFILE, "");
    }

    public String getGroupOfUserNameProfile() {
        return mSettings.getString(APP_PREFERENCES_GROUP_OF_USER_NAME_PROFILE, "");
    }

    public void setGroupPhotoProfile(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_GROUP_OF_USER_PHOTO_PROFILE, path);
        editor.commit();
    }

    public void setGroupOfUserNameProfile(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_GROUP_OF_USER_NAME_PROFILE, path);
        editor.commit();
    }

    public void setLastNameProfile(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_LAST_NAME_PROFILE, path);
        editor.commit();
    }

    public void setFirstNameProfile(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_FIRST_NAME_PROFILE, path);
        editor.commit();
    }

    public void setFullNameProfile(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME_PROFILE, path);
        editor.commit();
    }

    public String getSelectImageNotification() {
        return mSettings.getString(APP_PREFERENCES_SELECT_IMAGE_NOTIFICATION, "");
    }

    public Boolean getConfirmLayoutNotification() {
        return mSettings.getBoolean(APP_PREFERENCES_NOT_CONFIRM_DIALOG_HIDE, false);
    }

    public Boolean getStateCreateNewNote() {
        return mSettings.getBoolean(APP_PREFERENCES_STATE_CREATE_NEW_NOTE, false);
    }

    public Boolean getAuthLayoutNotification() {
        return mSettings.getBoolean(APP_PREFERENCES_NOT_AUTH_DIALOG_HIDE, false);
    }

    public void setStateCreateNewNote(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_STATE_CREATE_NEW_NOTE, state);
        editor.commit();
    }

    public void setAuthLayoutNotification(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_NOT_AUTH_DIALOG_HIDE, state);
        editor.commit();
    }

    public void setConfirmLayoutNotification(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_NOT_CONFIRM_DIALOG_HIDE, state);
        editor.commit();
    }

    public void setAuthLayoutNote(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_NOT_AUTH_DIALOG_NOTE_HIDE, state);
        editor.commit();
    }

    public void setConfirmLayoutNote(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_NOT_CONFIRM_NOTE_DIALOG_HIDE, state);
        editor.commit();
    }

    public Boolean isHideConfirmLayoutNote() {
        return mSettings.getBoolean(APP_PREFERENCES_NOT_CONFIRM_NOTE_DIALOG_HIDE, false);
    }

    public Boolean isHideAuthLayoutNote() {
        return mSettings.getBoolean(APP_PREFERENCES_NOT_AUTH_DIALOG_NOTE_HIDE, false);
    }

    public Boolean isHideConfirmLayoutNotification() {
        return mSettings.getBoolean(APP_PREFERENCES_NOT_CONFIRM_DIALOG_HIDE, false);
    }

    public Boolean isHideAuthLayoutNotification() {
        return mSettings.getBoolean(APP_PREFERENCES_NOT_AUTH_DIALOG_HIDE, false);
    }

    public void setSelectImageNotification(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_SELECT_IMAGE_NOTIFICATION, path);
        editor.commit();
    }

    public void setUserPhoto(String path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_PHOTO_PROFILE, path);
        editor.commit();
    }

    public String getUserPhoto() {
        return mSettings.getString(APP_PREFERENCES_PHOTO_PROFILE, "");
    }

    public Integer getVersionSchedule() {
        return mSettings.getInt(APP_PREFERENCES_VERSION_SCHEDULE_VALUE, 0);
    }

    public void setVersionSchedule(Integer cnt) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_VERSION_SCHEDULE_VALUE, cnt);
        editor.commit();
    }

    public Integer getGroupOfUserCountUsersProfile() {
        return mSettings.getInt(APP_PREFERENCES_GROUP_OF_USER_COUNT_USERS_PROFILE, 0);
    }

    public void setGroupOfUserCountUsersProfile(Integer cnt) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_GROUP_OF_USER_COUNT_USERS_PROFILE, cnt);
        editor.commit();
    }

    public Integer getIdGroupOfUser() {
        return mSettings.getInt(APP_PREFERENCES_GROUP_OF_USER_ID_VALUE, 0);
    }

    public void setIdGroupOfUser(Integer cnt) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_GROUP_OF_USER_ID_VALUE, cnt);
        editor.commit();
    }

    public void setUserList(ArrayList<Integer> cnt) {
        SharedPreferences.Editor editor = mSettings.edit();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < cnt.size(); i++) {
            str.append(cnt.get(i)).append(",");
        }
        editor.putString(APP_PREFERENCES_ID_USERS_INTERMEDIATE_VALUE, str.toString());
        editor.commit();
    }
    public ArrayList<Integer> getUserList() {
        String savedString = mSettings.getString(APP_PREFERENCES_ID_USERS_INTERMEDIATE_VALUE, "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        Integer ii = st.countTokens();
        ArrayList<Integer> savedList = new ArrayList<>();
        for (int i = 0; i < ii; i++) {
            savedList.add(Integer.parseInt(st.nextToken()));
        }
        return  savedList;
    }
    public void setUserPhotoList(ArrayList<String> cnt) {
        SharedPreferences.Editor editor = mSettings.edit();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < cnt.size(); i++) {
            str.append(cnt.get(i)).append(",");
        }
        editor.putString(APP_PREFERENCES_PHOTO_USERS_INTERMEDIATE_VALUE, str.toString());
        editor.commit();
    }
    public ArrayList<String> getUserPhotoList() {
        String savedString = mSettings.getString(APP_PREFERENCES_PHOTO_USERS_INTERMEDIATE_VALUE, "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        Integer ii = st.countTokens();
        ArrayList<String> savedList = new ArrayList<>();
        for (int i = 0; i < ii; i++) {
            savedList.add(st.nextToken());
        }
        return savedList;
    }

    public Set<String> getNotificationStrings() {
        Set<String> ret = mSettings.getStringSet(APP_PREFERENCES_TITLE_NOTIFICATION_INTERMEDIATE_VALUE, new HashSet<String>());
        return ret;
    }
    public void setNotificationStrings(Set<String> path) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putStringSet(APP_PREFERENCES_TITLE_NOTIFICATION_INTERMEDIATE_VALUE, path);
        editor.commit();
    }

    public void setCreateNoteSettingProfile(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CREATE_NOTE_SWITCH, state);
        editor.commit();
    }

    public Boolean isCreateNoteSettingProfile() {
        return mSettings.getBoolean(APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CREATE_NOTE_SWITCH, true);
    }

    public void setChangeNoteSettingProfile(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CHANGE_NOTE_SWITCH, state);
        editor.commit();
    }

    public Boolean isChangeNoteSettingProfile() {
        return mSettings.getBoolean(APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CHANGE_NOTE_SWITCH, true);
    }

    public void setTermNoteSettingProfile(Boolean state) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_TERM_NOTE_SWITCH, state);
        editor.commit();
    }

    public Boolean isTermNoteSettingProfile() {
        return mSettings.getBoolean(APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_TERM_NOTE_SWITCH, true);
    }
}
