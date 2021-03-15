package ng.com.obkm.myvooz.home.news.model;

import java.util.List;

import ng.com.obkm.myvooz.model.News;

public interface INewsListModel {
    interface OnFinishedListener {
        void onFinished(List<News> movieArrayList);

        void onFailure(Throwable t);
    }

    void getNewsList(OnFinishedListener onFinishedListener, Integer id_group);
}
