package ng.com.obkm.myvooz.note.UserCheckList.presenter;

public interface IUserCheckListPresenter {

    void onDestroy();

    void requestItemsFromServer(Integer id_sel);
}
