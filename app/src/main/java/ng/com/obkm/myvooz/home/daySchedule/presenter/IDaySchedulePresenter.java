package ng.com.obkm.myvooz.home.daySchedule.presenter;

import java.util.Calendar;

public interface IDaySchedulePresenter {

    void onDestroy();

    void requestDayScheduleServer(Integer id_group, Calendar calendar, Integer isSwitchHideEmptySchedule);
}
