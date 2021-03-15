package ng.com.obkm.myvooz.note.model;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.NoteFull;

public interface INoteModel {
    interface OnFinishedListener {
        void onFinished(NoteFull movieArrayList, Integer type);

        void onFailure(Throwable t);
    }

    interface OnFinishedSendData {
        void onFinishedSend(Boolean state, ArrayList<Integer> notes);

        void onFailure(Throwable t);
    }

    interface OnFinishedDeleteSendData {
        void onFinishedDeleteSend(Boolean state, ArrayList<Integer> notes);

        void onFailure(Throwable t);
    }

    void getItems(OnFinishedListener onFinishedListener, String access_token, Integer id_user, Integer type);
    void completedItems(OnFinishedSendData onFinishedSendData, String access_token, Integer id_user, ArrayList<Integer> notes);
    void deleteItems(OnFinishedDeleteSendData onFinishedSendData, String access_token, Integer id_user, ArrayList<Integer> notes);
}
