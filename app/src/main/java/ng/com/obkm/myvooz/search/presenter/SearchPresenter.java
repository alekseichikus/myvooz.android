package ng.com.obkm.myvooz.search.presenter;

import android.content.Context;

import java.util.List;

import ng.com.obkm.myvooz.model.ItemSearch;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.search.model.ISearchModel;
import ng.com.obkm.myvooz.search.model.SearchModel;
import ng.com.obkm.myvooz.search.view.ISearchView;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class SearchPresenter implements ISearchPresenter, ISearchModel.OnFinishedListener {

    private ISearchModel model;
    private ISearchView view;
    private IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;

    Runnable showInfo = new Runnable() {
        public void run() {
            view.checkEmpty();
        }
    };

    public SearchPresenter(ISearchView view, Context context){
        this.view = view;
        this.model = new SearchModel();
        this.activityPresenter = new ActivityPresenter(context);
        memoryOperation = new MemoryOperation(context);
    }

    @Override
    public void onFinished(List<ItemSearch> items) {
        if(items.size() == 0){
            view.showEmptyLayout();
        }
        else{
            view.showListLayout();
        }
        view.hideProgress();
        view.setItemsToRecyclerView(items);
    }

    @Override
    public void onFailure(Throwable t) {
        view.onResponseFailure(new Throwable());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestItemsFromServer(String text) {
        if (view != null) {
            if (activityPresenter.isInternetConnection()) {
                view.showProgress();
                model.getItems(this, memoryOperation.getIdUniversityProfile(), text);
            }
            else{
                view.setNoInternetConnectionLayout();
            }
        }
    }

    @Override
    public Runnable getRunnable() {
        return showInfo;
    }
}
