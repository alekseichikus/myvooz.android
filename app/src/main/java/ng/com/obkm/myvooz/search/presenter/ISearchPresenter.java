package ng.com.obkm.myvooz.search.presenter;

public interface ISearchPresenter {

    void onDestroy();

    void requestItemsFromServer(String text);

    Runnable getRunnable();
}
