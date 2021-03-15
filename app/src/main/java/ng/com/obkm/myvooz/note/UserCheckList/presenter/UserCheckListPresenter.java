package ng.com.obkm.myvooz.note.UserCheckList.presenter;

import android.content.Context;

import ng.com.obkm.myvooz.model.UsersListData;
import ng.com.obkm.myvooz.note.UserCheckList.model.IUserCheckListModel;
import ng.com.obkm.myvooz.note.UserCheckList.model.UserCheckListModel;
import ng.com.obkm.myvooz.note.UserCheckList.view.IUsersCheckListView;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class UserCheckListPresenter implements IUserCheckListPresenter, IUserCheckListModel.OnFinishedListener {

    private IUserCheckListModel model;
    private IUsersCheckListView view;
    private IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;
    Context context;

    public UserCheckListPresenter(IUsersCheckListView view, Context context){
        this.context = context;
        this.view = view;
        this.model = new UserCheckListModel();
        this.activityPresenter = new ActivityPresenter(context);
        memoryOperation = new MemoryOperation(context);
    }

    @Override
    public void onFinished(UsersListData items) {
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
    public void requestItemsFromServer(Integer id_after_user_select) {
        if (view != null) {
            if (activityPresenter.isInternetConnection()) {
                model.getItems(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), id_after_user_select);
            }
            else{
                view.setNoInternetConnectionLayout();
            }
        }
    }
}
