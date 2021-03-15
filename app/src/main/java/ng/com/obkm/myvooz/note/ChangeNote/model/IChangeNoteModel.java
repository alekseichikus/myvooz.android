package ng.com.obkm.myvooz.note.ChangeNote.model;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.Note;

public interface IChangeNoteModel {
    interface OnFinishedListener {
        void onFinished(Note note);

        void onFailure(Throwable t);
    }

    void uploadFile(String filepath);

    void changeNote(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer id_object, String date, String title, String text, ArrayList<Integer> images, Integer mark_me, Integer id_note);
}
