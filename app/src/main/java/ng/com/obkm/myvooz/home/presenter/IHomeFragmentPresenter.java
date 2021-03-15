package ng.com.obkm.myvooz.home.presenter;

import java.util.Calendar;

public interface IHomeFragmentPresenter {

    void onDestroy();

    boolean isChangeGroup();

    boolean isChangeSwitchHideEmptyLesson();

    void checkChange();

    Calendar getCalendar();

    Calendar getCurrentCalendar();

    void setCalendar(Calendar calendar);

    void setBlueStatusBar();
}
