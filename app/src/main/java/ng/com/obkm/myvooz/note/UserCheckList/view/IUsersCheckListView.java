package ng.com.obkm.myvooz.note.UserCheckList.view;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.model.UsersListData;

public interface IUsersCheckListView {

    void showProgress();

    void hideProgress();

    void setItemsToRecyclerView(UsersListData items);

    void onResponseFailure(Throwable throwable);

    void showListLayout();

    void showEmptyLayout();

    void setNoInternetConnectionLayout();

    //IUserRequestConfirmPresenter getUserRequestConfirmPresenter();

    ArrayList<UserShort> getUsers();
}
