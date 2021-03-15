package ng.com.obkm.myvooz.note.UserCheckList.model;

import ng.com.obkm.myvooz.model.UsersListData;

public interface IUserCheckListModel {
    interface OnFinishedListener {
        void onFinished(UsersListData items);

        void onFailure(Throwable t);
    }

    void getItems(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer id_after_user_select);
}
