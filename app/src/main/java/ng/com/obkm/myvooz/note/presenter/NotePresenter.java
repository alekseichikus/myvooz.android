package ng.com.obkm.myvooz.note.presenter;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import ng.com.obkm.myvooz.Adapter.ListAdapter;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.model.NoteFull;
import ng.com.obkm.myvooz.note.model.INoteModel;
import ng.com.obkm.myvooz.note.model.NoteModel;
import ng.com.obkm.myvooz.note.view.INoteView;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.presenter.IListPresenter;
import ng.com.obkm.myvooz.utils.DataBase.DBHelper;
import ng.com.obkm.myvooz.utils.Date.DateWithTimeZone;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class NotePresenter implements INotePresenter, INoteModel.OnFinishedListener, IListPresenter, INoteModel.OnFinishedSendData, INoteModel.OnFinishedDeleteSendData {

    private INoteModel model;
    private INoteView view;
    private IActivityPresenter activityPresenter;
    MemoryOperation memoryOperation;
    private DBHelper dbHelper;
    private ListAdapter adapterList;

    public NotePresenter(INoteView view, Context context, DBHelper dbHelper){
        this.view = view;
        this.dbHelper = dbHelper;
        this.model = new NoteModel();
        this.activityPresenter = new ActivityPresenter(context);
        memoryOperation = new MemoryOperation(context);
    }

    @Override
    public void onFinished(NoteFull item, Integer type) {
        view.getItemsListView().get(0).setCount(item.getCountActive());
        view.getItemsListView().get(1).setCount(item.getCountCompleted());
        view.hideProgress();
        ArrayList<Note> items = new ArrayList<>();

        dbHelper.deleteAllNoteWithStateCompleted(adapterList.getTypeSelectItem());

        for(Note note: item.getNotes()){
            Calendar calendar = null;
            try {
                calendar = DateWithTimeZone.stringToCalendar(note.getDate(), TimeZone.getTimeZone("Europe/Moscow"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Note n = new Note(note.getNameObject(), note.getName(), note.getText(), note.isMarkMe(), note.getPhotos(), note.getId()
                    , dateFormat.format(calendar.getTime()), note.isStateCompleted(), note.getIdUser(), note.getIdObject());
            items.add(n);
            dbHelper.addNote(n);
        }

        if(items.size()==0){
            view.setEmptyLayout();
        }
        else{
            view.setLayout();
        }

        Boolean t = type > 0 ? true : false;

        view.setItemsToRecyclerView(items, t);
    }

    @Override
    public void onFinishedSend(Boolean state, ArrayList<Integer> notes) {
        if(state){
            view.completedNotes(notes);
            view.hideProgress();
        }
    }

    @Override
    public void onFinishedDeleteSend(Boolean state, ArrayList<Integer> notes) {
        if(state){
            view.deleteNotes(notes);
            view.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        //view.onResponseFailure(new Throwable());
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public IListPresenter getListPresenter() {
        return  this;
    }

    @Override
    public void completedData(ArrayList<Integer> items) {
        if (view != null) {
            if (activityPresenter.isInternetConnection()) {
                view.showProgress();
                view.setLayout();
                model.completedItems(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), items);
            }
            else{
                view.onResponseFailure("Ошибка. Проверьте подключение к интернету.");
            }
        }
    }

    @Override
    public void deleteData(ArrayList<Integer> items) {
        if (activityPresenter.isInternetConnection()) {
            view.showProgress();
            view.setLayout();
            model.deleteItems(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), items);
        }
        else{
            view.onResponseFailure("Ошибка. Проверьте подключение к интернету.");
        }
    }

    @Override
    public void setAdapterList(ListAdapter listAdapter) {
        this.adapterList = listAdapter;
    }

    @Override
    public void requestData(Integer type) {
        view.setDataBaseItems(type);
        if (view != null) {
            view.setAddButton();
            if (activityPresenter.isInternetConnection()) {
                if(memoryOperation.getAccessToken() != ""){
                    view.showProgress();
                    model.getItems(this, memoryOperation.getAccessToken(), memoryOperation.getIdUserProfile(), type);
                }
            }

            if(memoryOperation.getAccessToken() != ""){
                view.hideNotAuthLayout();

                if(!memoryOperation.getIdGroupOfUser().equals(0)){
                    view.hideNotConfirmLayout();
                }
                else{
                    if(memoryOperation.isHideConfirmLayoutNote()){
                        view.hideNotConfirmLayout();
                    }
                    else{
                        view.showNotConfirmLayout();
                    }
                }
            }
            else{
                if(memoryOperation.isHideAuthLayoutNote()){
                    view.hideNotAuthLayout();
                }
                else{
                    view.showNotAuthLayout();
                }
                view.hideNotConfirmLayout();
            }
        }
    }
}
