package ng.com.obkm.myvooz.note.AddNote.view;

import ng.com.obkm.myvooz.home.presenter.IHomeFragmentPresenter;
import ng.com.obkm.myvooz.model.ImageList;
import ng.com.obkm.myvooz.model.Note;

public interface IAddNoteView {

    void showProgress();

    void hideProgress();

    String getNotificationText();

    void onResponseFailure(String text);

    IHomeFragmentPresenter getHomeFragmentPresenter();

    void onFinished(String text);

    void addImage(ImageList image);

    void addNoteFromServer(Note note);
}
