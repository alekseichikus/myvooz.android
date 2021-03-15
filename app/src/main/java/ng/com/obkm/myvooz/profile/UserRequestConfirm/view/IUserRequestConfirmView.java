package ng.com.obkm.myvooz.profile.UserRequestConfirm.view;

import java.util.ArrayList;
import java.util.List;

import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.presenter.IUserRequestConfirmPresenter;

public interface IUserRequestConfirmView {

    void showProgress();

    void hideProgress();

    void setItemsToRecyclerView(List<UserShort> items);

    void onResponseFailure(Throwable throwable);

    void showListLayout();

    void showEmptyLayout();

    void setNoInternetConnectionLayout();

    IUserRequestConfirmPresenter getUserRequestConfirmPresenter();

    ArrayList<UserShort> getUsers();
}
