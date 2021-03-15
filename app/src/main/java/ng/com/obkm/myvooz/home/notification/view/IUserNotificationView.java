package ng.com.obkm.myvooz.home.notification.view;

import java.util.ArrayList;

import ng.com.obkm.myvooz.home.notification.model.ListItem;
import ng.com.obkm.myvooz.home.notification.model.Notification;

public interface IUserNotificationView {

    void showProgress();

    void hideProgress();

    void setToRecyclerView(ArrayList<Notification> items);

    void setListItems(ArrayList<ListItem> items);

    void setNoInternetConnectionLayout();

    void onResponseFailure(Throwable t);

    void setLayout();

    void setEmptyLayout();

    void showNotAuthLayout();

    void hideNotAuthLayout();

    void showNotConfirmLayout();

    void hideNotConfirmLayout();
}
