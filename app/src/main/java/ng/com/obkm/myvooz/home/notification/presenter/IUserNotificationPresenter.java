package ng.com.obkm.myvooz.home.notification.presenter;

import ng.com.obkm.myvooz.presenter.IListPresenter;

public interface IUserNotificationPresenter {

    void onDestroy();

    IListPresenter getListPresenter();

    void addListItems();
}
