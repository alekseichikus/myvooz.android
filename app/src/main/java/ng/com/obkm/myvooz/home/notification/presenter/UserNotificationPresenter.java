package ng.com.obkm.myvooz.home.notification.presenter;

import android.content.Context;

import java.util.ArrayList;

import ng.com.obkm.myvooz.home.notification.model.IUserNotificationModel;
import ng.com.obkm.myvooz.home.notification.model.ListItem;
import ng.com.obkm.myvooz.home.notification.model.Notification;
import ng.com.obkm.myvooz.home.notification.model.UserNotificationModel;
import ng.com.obkm.myvooz.home.notification.view.IUserNotificationView;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.presenter.IListPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class UserNotificationPresenter implements IUserNotificationPresenter, IUserNotificationModel.OnFinishedListener, IListPresenter {

    private IUserNotificationModel model;
    private IUserNotificationView view;
    private IActivityPresenter activityPresenter;
    private MemoryOperation memoryOperation;

    public UserNotificationPresenter(IUserNotificationView view, Context context, MemoryOperation memoryOperation){
        this.view = view;
        this.model = new UserNotificationModel();
        this.activityPresenter = new ActivityPresenter(context);
        this.memoryOperation = memoryOperation;
    }

    @Override
    public void onFinished(ArrayList<Notification> items) {
        if (view != null) {
            view.hideProgress();
            if(items.size() == 0){
                view.setEmptyLayout();
            }
            else{
                view.setToRecyclerView(items);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {

        if (view != null) {
            view.hideProgress();
            view.onResponseFailure(t);
        }
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public IListPresenter getListPresenter() {
        return this;
    }

    @Override
    public void addListItems() {
        ArrayList<ListItem> items = new ArrayList<>();
        if(activityPresenter.isInternetConnection()){
            if(!memoryOperation.getAccessToken().equals("")){
                items.add(new ListItem("Все", 0, true, false, 0, false));
                items.add(new ListItem("Университет", 1, false, false, 0, false));
                if(!memoryOperation.getIdGroupOfUser().equals(0)){
                    items.add(new ListItem("Группа", 2, false, false, 0, false));
                }
                items.add(new ListItem("Лично мне", 3, false, false, 0, false));
            }
        }
        view.setListItems(items);
    }

    @Override
    public void requestData(Integer type) {
        if (view != null) {

            if(memoryOperation.getAccessToken() != ""){
                view.hideNotAuthLayout();

                if(!memoryOperation.getIdGroupOfUser().equals(0)){
                    view.hideNotConfirmLayout();
                    model.getUserNotificationList(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), type);
                }
                else{
                    if(memoryOperation.isHideConfirmLayoutNotification()){
                        view.hideNotConfirmLayout();
                    }
                    else{
                        view.showNotConfirmLayout();
                    }
                    model.getUserWithUniversityNotificationList(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), type, memoryOperation.getIdUniversityProfile());
                }
            }
            else{

                if(memoryOperation.isHideAuthLayoutNotification()){
                    view.hideNotAuthLayout();
                }
                else{
                    view.showNotAuthLayout();
                }
                model.getUniversityNotificationList(this, memoryOperation.getIdUniversityProfile());
                view.hideNotConfirmLayout();
            }

            if(activityPresenter.isInternetConnection()){
                view.setLayout();
                view.showProgress();
            }
            else{
                view.setNoInternetConnectionLayout();
                view.hideProgress();
            }
        }
    }
}
