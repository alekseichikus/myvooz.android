package ng.com.obkm.myvooz.home.news.presenter;

import android.content.Context;

import java.util.List;

import ng.com.obkm.myvooz.home.news.model.INewsListModel;
import ng.com.obkm.myvooz.model.News;
import ng.com.obkm.myvooz.home.news.model.NewsListModel;
import ng.com.obkm.myvooz.home.view.IHomeFragmentView;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;

public class NewsListPresenter implements INewsListPresenter, NewsListModel.OnFinishedListener {

    private INewsListModel newsListModel;
    private IHomeFragmentView homeFragmentView;
    private IActivityPresenter activityPresenter;

    public NewsListPresenter(IHomeFragmentView homeFragmentView, Context context){
        this.homeFragmentView = homeFragmentView;
        this.newsListModel = new NewsListModel();
        this.activityPresenter = new ActivityPresenter(context);
    }

    @Override
    public void onFinished(List<News> newsList) {

        if (homeFragmentView != null) {
            homeFragmentView.setNewsToRecyclerView(newsList);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        homeFragmentView.onResponseFailure(t);
        if (homeFragmentView != null) {
        }
    }

    @Override
    public void onDestroy() {
        this.homeFragmentView = null;
    }

    @Override
    public void requestNewsFromServer(Integer id_group) {
        if (homeFragmentView != null) {
            if(activityPresenter.isInternetConnection()){
                newsListModel.getNewsList(this, id_group);
            }
        }
    }
}
