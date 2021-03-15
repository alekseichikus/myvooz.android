package ng.com.obkm.myvooz.search.view;

import java.util.List;

import ng.com.obkm.myvooz.model.ItemSearch;

public interface ISearchView {

    void showProgress();

    void hideProgress();

    void setItemsToRecyclerView(List<ItemSearch> newsList);

    void onResponseFailure(Throwable throwable);

    void checkEmpty();

    void showStartLayout();

    void showListLayout();

    void showEmptyLayout();

    void setNoInternetConnectionLayout();
}
