package ng.com.obkm.myvooz.profile.UserRequestConfirm.presenter;

public interface IUserRequestConfirmPresenter {

    void onDestroy();

    void requestItemsFromServer();

    void sendDataToServer(Integer sel_id_user, Integer state, Integer position);
}
