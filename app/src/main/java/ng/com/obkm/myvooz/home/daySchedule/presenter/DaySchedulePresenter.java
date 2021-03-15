package ng.com.obkm.myvooz.home.daySchedule.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import ng.com.obkm.myvooz.Adapter.WeekSchedule;
import ng.com.obkm.myvooz.home.daySchedule.model.DayScheduleModel;
import ng.com.obkm.myvooz.home.daySchedule.model.IDayScheduleModel;
import ng.com.obkm.myvooz.home.view.IHomeFragmentView;
import ng.com.obkm.myvooz.model.DaySchedule;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.DataBase.DBScheduleGroup;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class DaySchedulePresenter implements IDaySchedulePresenter, DayScheduleModel.OnFinishedListener {

    private IDayScheduleModel dayScheduleModel;
    private IHomeFragmentView homeFragmentView;
    private IActivityPresenter activityPresenter;
    private MemoryOperation memoryOperation;
    DBScheduleGroup dbScheduleGroup;

    public DaySchedulePresenter(IHomeFragmentView homeFragmentView, Context context){
        this.homeFragmentView = homeFragmentView;
        this.dayScheduleModel = new DayScheduleModel();
        this.activityPresenter = new ActivityPresenter(context);
        dbScheduleGroup = new DBScheduleGroup(context);
        memoryOperation = new MemoryOperation(context);
    }


    @Override
    public void onFinished(WeekSchedule weekSchedule) {
        if (homeFragmentView != null) {
            homeFragmentView.hideProgress();
            if(weekSchedule.getDaySchedule().size() == 0){
                homeFragmentView.showEmptyDayScheduleLayout();
            }
            else{
                homeFragmentView.hideEmptyDayScheduleLayout();
            }
            if(homeFragmentView.getHomeFragmentPresenter().getCalendar().compareTo(homeFragmentView.getHomeFragmentPresenter().getCurrentCalendar()) == 0){
                homeFragmentView.hideReturnButton();
            }
            else{
                homeFragmentView.showReturnButton();
            }
            homeFragmentView.setDayScheduleLayout();
            homeFragmentView.setScheduleOfDayToRecyclerView(weekSchedule.getDaySchedule());
            homeFragmentView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {

        if (homeFragmentView != null) {
            homeFragmentView.hideProgress();
            homeFragmentView.onResponseFailure(t);
        }
    }

    @Override
    public void onDestroy() {
        this.homeFragmentView = null;
    }

    @Override
    public void requestDayScheduleServer(Integer id_group, Calendar calendar, Integer isSwitchHideEmptySchedule) {

        if (homeFragmentView != null) {
            if(!memoryOperation.getAccessToken().equals("") && !memoryOperation.getIdGroupOfUser().equals(0)){
                ArrayList<ArrayList<DaySchedule>> ds = dbScheduleGroup.getData(calendar);
                if(ds.size()>0){
                    homeFragmentView.hideEmptyDayScheduleLayout();
                }
                else{
                    homeFragmentView.showEmptyDayScheduleLayout();
                }
                homeFragmentView.setScheduleOfDayToRecyclerView(ds);
            }
            if(activityPresenter.isInternetConnection()){
                homeFragmentView.showProgress();
                dayScheduleModel.getScheduleDay(this, id_group, calendar);
            }
            else{
                //homeFragmentView.setNoInternetConnectionLayout();
                homeFragmentView.hideProgress();
            }
        }
    }

}
