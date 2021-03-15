package ng.com.obkm.myvooz.home.daySchedule.model;

import java.util.Calendar;

import ng.com.obkm.myvooz.Adapter.WeekSchedule;

public interface IDayScheduleModel {
    interface OnFinishedListener {
        void onFinished(WeekSchedule movieArrayList);

        void onFailure(Throwable t);
    }

    void getScheduleDay(OnFinishedListener onFinishedListener, Integer id_group, Calendar calendar);
}
