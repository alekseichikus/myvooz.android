package ng.com.obkm.myvooz.search.model;

import java.util.List;

import ng.com.obkm.myvooz.model.ItemSearch;

public interface ISearchModel {
    interface OnFinishedListener {
        void onFinished(List<ItemSearch> movieArrayList);

        void onFailure(Throwable t);
    }

    void getItems(OnFinishedListener onFinishedListener, Integer id_university, String text);
}
