package ng.com.obkm.myvooz.profile.AddNotification.view;

import ng.com.obkm.myvooz.model.ImageList;
import ng.com.obkm.myvooz.home.presenter.IHomeFragmentPresenter;

public interface IAddNotificationView {

    void showProgress();

    void hideProgress();

    String getNotificationText();

    void onResponseFailure();

    IHomeFragmentPresenter getHomeFragmentPresenter();

    void onFinished();

    void addImage(ImageList image);

    void setColorImportantButton(Integer color);
}
