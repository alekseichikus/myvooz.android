package ng.com.obkm.myvooz.note.view;

import java.util.ArrayList;
import java.util.List;

import ng.com.obkm.myvooz.home.notification.model.ListItem;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.note.presenter.INotePresenter;

public interface INoteView {

    void showProgress();

    void hideProgress();

    void setItemsToRecyclerView(List<Note> newsList, Boolean type);

    void updateItemsListView();

    void completedNotes(ArrayList<Integer> notes);
    void deleteNotes(ArrayList<Integer> notes);

    ArrayList<ListItem> getItemsListView();


    void showNotAuthLayout();
    void hideNotAuthLayout();

    void setLayout();

    void showNotConfirmLayout();
    void hideNotConfirmLayout();
    void onResponseFailure(String text);

    void setEmptyLayout();

    void addNotes(Note note);

    void changeNote(Note note);

    INotePresenter getNotePresenter();

    void setAddButton();

    void setDataBaseItems(Integer type);
}
