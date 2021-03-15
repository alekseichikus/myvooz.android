package ng.com.obkm.myvooz.note.ChangeNote.view;

import java.util.ArrayList;

import ng.com.obkm.myvooz.home.presenter.IHomeFragmentPresenter;
import ng.com.obkm.myvooz.model.ImageList;
import ng.com.obkm.myvooz.model.Note;

public interface IChangeNoteView {

    void showProgress();

    void hideProgress();

    String getNotificationText();

    void onResponseFailure(String text);

    IHomeFragmentPresenter getHomeFragmentPresenter();

    void onFinished(String text);

    void addImage(ImageList image);

    void addNoteFromServer(Note note);

    ArrayList<Integer> getIDImages();
}
