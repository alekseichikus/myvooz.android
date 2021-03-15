package ng.com.obkm.myvooz.home.news.presenter;

public interface INewsListPresenter {

    void onDestroy();

    void requestNewsFromServer(Integer id_group);
}
