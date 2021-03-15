package ng.com.obkm.myvooz.profile.UserRequestConfirm.presenter;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.model.IUserRequestConfirmModel;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.model.UserRequestConfirmModel;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.view.IUserRequestConfirmView;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class UserRequestConfirmPresenter implements IUserRequestConfirmPresenter, IUserRequestConfirmModel.OnFinishedListener {

    private IUserRequestConfirmModel model;
    private IUserRequestConfirmView view;
    private IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;
    Context context;

    public UserRequestConfirmPresenter(IUserRequestConfirmView view, Context context){
        this.context = context;
        this.view = view;
        this.model = new UserRequestConfirmModel();
        this.activityPresenter = new ActivityPresenter(context);
        memoryOperation = new MemoryOperation(context);
    }

    @Override
    public void onFinished(List<UserShort> items) {
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
    public void onFinishedSend(Integer state, Integer position) {
        if(state==1){
            Toast.makeText(context, "Добавлен", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Отклонено", Toast.LENGTH_SHORT).show();
        }
        view.getUsers().remove(position);

        view.setItemsToRecyclerView(view.getUsers());
        if(view.getUsers().size() == 0){
            view.showEmptyLayout();

        }
        else{
            view.showListLayout();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        view.onResponseFailure(new Throwable());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void requestItemsFromServer() {
        if (view != null) {
            if (activityPresenter.isInternetConnection()) {
                view.showProgress();
                model.getItems(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile());
            }
            else{
                view.setNoInternetConnectionLayout();
            }
        }
    }

    @Override
    public void sendDataToServer(Integer sel_id_user, Integer state, Integer position) {
        if (view != null) {
            if (activityPresenter.isInternetConnection()) {
                //view.showProgress();
                model.sendData(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), sel_id_user, state, position);
            }
            else{
                //view.setNoInternetConnectionLayout();
            }
        }
    }
}
