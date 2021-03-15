package ng.com.obkm.myvooz.profile.UserRequestConfirm.model;

import java.util.List;

import ng.com.obkm.myvooz.model.UserShort;

public interface IUserRequestConfirmModel {
    interface OnFinishedListener {
        void onFinished(List<UserShort> items);
        void onFinishedSend(Integer state, Integer position);

        void onFailure(Throwable t);
    }

    void getItems(OnFinishedListener onFinishedListener, String access_token, Integer id_user);

    void sendData(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer sel_id_user, Integer state, Integer position);
}
