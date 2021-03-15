package ng.com.obkm.myvooz.note.AddNote.presenter;

import android.content.Intent;

import ng.com.obkm.myvooz.presenter.IListPresenter;

public interface IAddNotePresenter {

    void onDestroy();

    void sendNotificationFromServer(Integer id_object, String date, String title, String text, Integer mark_me);

    void addImage(String path);
    void checkActivityResult(int requestCode, Intent data, String filename);
    IListPresenter getListPresenter();
}
