package ng.com.obkm.myvooz.note.AddNote.model;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.Note;

public interface IAddNoteModel {
    interface OnFinishedListener {
        void onFinished(Note note);

        void onFailure(Throwable t);
    }

    void uploadFile(String filepath);

    void addNote(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer id_object, String date, String title, String text, ArrayList<Integer> images, Integer mark_me);
}
