package ng.com.obkm.myvooz.utils;

public class Constants {
    public static final String SITE_ADDRESS = "https://myvooz.ru/";

    //note
    public static final String APP_PREFERENCES_NOTIF_NOTE_STRING_1 = "notif_note_string_1";
    public static final String APP_PREFERENCES_NOTIF_NOTE_STRING_2 = "notif_note_string_2";
    public static final String APP_PREFERENCES_NOTIF_NOTE_STRING_3 = "notif_note_string_3";
    public static final String APP_PREFERENCES_NOTIF_NOTE_STRING_4 = "notif_note_string_4";

    //user
    public static final String APP_PREFERENCES_NAME_PROFILE = "name_profile";
    public static final String APP_PREFERENCES_FIRST_NAME_PROFILE = "first_name_profile";
    public static final String APP_PREFERENCES_LAST_NAME_PROFILE = "last_name_profile";
    public static final String APP_PREFERENCES_PHOTO_PROFILE = "photo_profile";
    public static final String APP_PREFERENCES_ACCESS_TOKEN_PROFILE = "access_token_profile";
    public static final String APP_PREFERENCES_USER_ID_PROFILE = "user_id_profile";

    //group_of_user
    public static final String APP_PREFERENCES_GROUP_OF_USER_PHOTO_PROFILE = "group_photo_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_NAME_PROFILE = "group_of_user_name_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_ID_CREATOR_PROFILE = "group_of_user_id_creator_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_NAME_CREATOR_PROFILE = "group_of_user_name_creator_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_NAME_OLDER_PROFILE = "group_of_user_name_creator_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_PHOTO_CREATOR_PROFILE = "group_of_user_photo_creator_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_COUNT_USERS_PROFILE = "group_of_user_count_users_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_ID_OLDER_PROFILE = "group_of_user_id_older_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_PHOTO_OLDER_PROFILE = "group_of_user_photo_older_profile";
    public static final String APP_PREFERENCES_GROUP_OF_USER_ID_VALUE = "id_group_of_user_value";

    //dialog_fragment
    public static final String APP_PREFERENCES_IS_HIDE_EMPTY_SCHEDULE_PROFILE = "is_hide_empty_schedule_profile";
    public static final String APP_PREFERENCES_NOT_AUTH_DIALOG_HIDE = "auth_dialog_hide";
    public static final String APP_PREFERENCES_NOT_CONFIRM_DIALOG_HIDE = "confirm_dialog_hide";
    public static final String APP_PREFERENCES_NOT_AUTH_DIALOG_NOTE_HIDE = "auth_dialog_note_hide";
    public static final String APP_PREFERENCES_NOT_CONFIRM_NOTE_DIALOG_HIDE = "confirm_dialog_note_hide";

    //group
    public static final String APP_PREFERENCES_GROUP_NAME_PROFILE = "user_group_name_profile";
    public static final String APP_PREFERENCES_GROUP_ID_PROFILE = "user_group_id_profile";
    public static final String APP_PREFERENCES_UNIVERSITY_NAME_PROFILE = "user_university_name_profile";
    public static final String APP_PREFERENCES_UNIVERSITY_ID_PROFILE = "user_university_id_profile";

    //intermediate_value
    public static final String APP_PREFERENCES_GROUP_NAME_INTERMEDIATE_VALUE = "user_group_name_intermediate_value";
    public static final String APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE = "user_group_id_intermediate_value";
    public static final String APP_PREFERENCES_CORPUS_NAME_INTERMEDIATE_VALUE = "corpus_name_intermediate_value";
    public static final String APP_PREFERENCES_CORPUS_ID_INTERMEDIATE_VALUE = "corpus_id_intermediate_value";
    public static final String APP_PREFERENCES_OBJECT_NAME_INTERMEDIATE_VALUE = "object_name_intermediate_value";
    public static final String APP_PREFERENCES_OBJECT_ID_INTERMEDIATE_VALUE = "object_id_intermediate_value";
    public static final String APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE = "user_university_name_intermediate_value";
    public static final String APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE = "user_university_id_intermediate_value";
    public static final String APP_PREFERENCES_ID_USERS_INTERMEDIATE_VALUE = "id_users_intermediate_value";
    public static final String APP_PREFERENCES_PHOTO_USERS_INTERMEDIATE_VALUE = "photo_users_intermediate_value";

    //home
    public static final String APP_PREFERENCES_SHOW_EMPTY_LESSONS_BOOL = "show_empty_lessons_bool";

    //version_schedule
    public static final String APP_PREFERENCES_VERSION_SCHEDULE_VALUE = "version_schedule_value";

    //switch
    public static final String APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CREATE_NOTE_SWITCH = "profile_setting_notification_create_note_switch";
    public static final String APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_CHANGE_NOTE_SWITCH = "profile_setting_notification_change_note_switch";
    public static final String APP_PREFERENCES_PROFILE_SETTING_NOTIFICATION_TERM_NOTE_SWITCH = "profile_setting_notification_term_note_switch";

    public static final String APP_PREFERENCES_SELECT_IMAGE_NOTIFICATION = "select_image_notification";

    public static final String APP_PREFERENCES_STATE_CREATE_NEW_NOTE = "state_create_new_note";

    public static final String APP_PREFERENCES_TITLE_NOTIFICATION_INTERMEDIATE_VALUE = "title_notification_intermediate_value";

    public static final Integer GROUP_SELECT_PARAM_RESULT = 2;
    public static final Integer CHANGE_GROUP_DONT_GOU_PARAM_RESULT = 2;
    public static final Integer MAX_LESSONS_OF_UNIVERSITY_COUNT = 8;

    public static String getMonthName(int month){
        String[] monthNames = {"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сеп", "окт", "ноя", "дек"};
        return monthNames[month];
    }
    public static String getDayOfWeekName(int month){
        String monthName = "";
        switch (month) {
            case  (2):
                monthName="Понедельник";
                break;
            case  (3):
                monthName="Вторник";
                break;
            case  (4):
                monthName="Среда";
                break;
            case  (5):
                monthName="Четверг";
                break;
            case  (6):
                monthName="Пятница";
                break;
            case  (7):
                monthName="Суббота";
                break;
            case  (1):
                monthName="Воскресенье";
                break;
        }
        return monthName;
    }
    public static String getDayOfWeekNameShort(int month){
        String monthName = "";
        switch (month) {
            case  (2):
                monthName="Пн";
                break;
            case  (3):
                monthName="Вт";
                break;
            case  (4):
                monthName="Ср";
                break;
            case  (5):
                monthName="Чт";
                break;
            case  (6):
                monthName="Пт";
                break;
            case  (7):
                monthName="Сб";
                break;
            case  (1):
                monthName="Вс";
                break;
        }
        return monthName;
    }
}
