package ng.com.obkm.myvooz.note.presenter;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Adapter.ListAdapter;
import ng.com.obkm.myvooz.presenter.IListPresenter;

public interface INotePresenter {

    void onDestroy();

    IListPresenter getListPresenter();

    void completedData(ArrayList<Integer> items);

    void deleteData(ArrayList<Integer> items);

    void setAdapterList(ListAdapter listAdapter);

}
