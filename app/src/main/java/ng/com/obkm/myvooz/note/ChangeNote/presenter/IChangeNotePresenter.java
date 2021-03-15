package ng.com.obkm.myvooz.note.ChangeNote.presenter;

import android.content.Intent;

import java.util.ArrayList;

import ng.com.obkm.myvooz.presenter.IListPresenter;

public interface IChangeNotePresenter {

    void onDestroy();

    void sendNotificationFromServer(Integer id_object, String date, String title, String text, Integer mark_me, Integer id_note);

    void addImage(String path);
    void checkActivityResult(int requestCode, Intent data, String filename);
    IListPresenter getListPresenter();

    ArrayList<Integer> getIDImages();
}
