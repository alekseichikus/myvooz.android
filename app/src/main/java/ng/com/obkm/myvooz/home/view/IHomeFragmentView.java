package ng.com.obkm.myvooz.home.view;

import java.util.ArrayList;
import java.util.List;

import ng.com.obkm.myvooz.model.DaySchedule;
import ng.com.obkm.myvooz.model.News;
import ng.com.obkm.myvooz.home.presenter.IHomeFragmentPresenter;

public interface IHomeFragmentView {

    void showProgress();

    void hideProgress();

    void setNewsToRecyclerView(List<News> newsList);

    void setScheduleOfDayToRecyclerView(ArrayList<ArrayList<DaySchedule>> weekSchedule);

    void showEmptyDayScheduleLayout();

    void hideEmptyDayScheduleLayout();

    void showReturnButton();

    void hideReturnButton();

    void  setNoInternetConnectionLayout();

    void  setDayScheduleLayout();

    void onResponseFailure(Throwable throwable);

    IHomeFragmentPresenter getHomeFragmentPresenter();
}
