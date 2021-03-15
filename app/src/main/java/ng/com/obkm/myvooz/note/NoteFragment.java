package ng.com.obkm.myvooz.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ng.com.obkm.myvooz.Adapter.ListAdapter;
import ng.com.obkm.myvooz.Adapter.NoteAdapter;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.home.notification.model.ListItem;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.note.AddNote.view.AddNoteActivity;
import ng.com.obkm.myvooz.note.presenter.INotePresenter;
import ng.com.obkm.myvooz.note.presenter.NotePresenter;
import ng.com.obkm.myvooz.note.view.INoteView;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.DataBase.DBHelper;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Notification.CreateNotification;

public class NoteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, INoteView {

    private ArrayList<Note> items = new ArrayList<>();
    private ArrayList<Note> all_items = new ArrayList<>();
    ArrayList<ListItem> list_items = new ArrayList<>();
    private MainActivity mainActivity;
    private CardView notConfirmLayout;
    private CardView notAuthLayout;
    private View view;
    private ListAdapter adapterList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CardView againConnectButton;
    private MemoryOperation memoryOperation;
    private RelativeLayout emptyLayout;
    private IActivityPresenter activityPresenter;
    private INotePresenter notePresenter;
    private NoteAdapter adapter;
    private CardView addButton;
    private CardView addButton2;
    private CardView completedButton;
    private CardView deleteButton;
    private DBHelper dbHelper;
    private RecyclerView scheduleRecyclerView;
    private ListView recyclerView;
    private CardView hideNotConfirmLayoutButton;
    private CardView hideNotAuthLayoutButton;

    public NoteFragment(MainActivity mainActivity, ArrayList<Note> notes, DBHelper dbHelper){
        this.mainActivity = mainActivity;
        this.dbHelper = dbHelper;
        this.all_items = notes;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(view == null){
            memoryOperation = new MemoryOperation(getContext());
            activityPresenter = new ActivityPresenter(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_note, container, false);

            list_items.add(new ListItem("Активные", 0, true, true, 0, false));
            list_items.add(new ListItem("Выполненные", 1, false, true, 0, false));
            initUI(view);
            setListeners();
            setLayout();
            notePresenter = new NotePresenter(this, getContext(), dbHelper);

            scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapterList = new ListAdapter(getContext(), list_items, notePresenter.getListPresenter());
            scheduleRecyclerView.setAdapter(adapterList);

            notePresenter.setAdapterList(adapterList);

            ArrayList<CardView> buttons = new ArrayList<>();
            buttons.add(addButton);
            buttons.add(completedButton);
            buttons.add(deleteButton);
            adapter = new NoteAdapter(this, mainActivity, items, buttons, adapterList, memoryOperation);
            recyclerView.setAdapter(adapter);

            notePresenter.getListPresenter().requestData(0);
        }
        return view;
    }

    private void initUI(View view){
        addButton = view.findViewById(R.id.add_button);
        addButton2 = view.findViewById(R.id.add_button_2);
        completedButton = view.findViewById(R.id.completed_button);
        deleteButton = view.findViewById(R.id.delete_button);
        emptyLayout = view.findViewById(R.id.layout_empty);
        hideNotConfirmLayoutButton = view.findViewById(R.id.hide_not_confirm_layout_button);
        hideNotAuthLayoutButton = view.findViewById(R.id.hide_not_auth_layout_button);
        notConfirmLayout = view.findViewById(R.id.layout_not_confirm_profile);
        notAuthLayout = view.findViewById(R.id.layout_not_auth);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        mSwipeRefreshLayout.setEnabled(false);
        scheduleRecyclerView = view.findViewById(R.id.recycler_list_view);
        recyclerView = view.findViewById(R.id.content_layout);
        ((TextView) view.findViewById(R.id.layout_not_confirm_profile_long_text)).setText("Вы сможете создавать и просматривать коллективные заметки, созданные для группы.");
        ((TextView) view.findViewById(R.id.layout_not_auth_profile_long_text)).setText("Чтобы видеть заметки своей группы.");
    }

    private  void setListeners(){
        addButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memoryOperation.getAccessToken() != ""){
                    if(activityPresenter.isInternetConnection()){
                        Intent intent = new Intent(getContext(), AddNoteActivity.class);
                        getActivity().startActivityForResult(intent, 10);
                    }
                    else{
                        onResponseFailure("Нет соединения с интернетом");
                    }
                }
                else{
                    onResponseFailure("Необходимо авторизоваться");
                }
            }
        });

        hideNotAuthLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notAuthLayout.setVisibility(View.GONE);
                memoryOperation.setAuthLayoutNote(true);
            }
        });

        hideNotConfirmLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notConfirmLayout.setVisibility(View.GONE);
                memoryOperation.setConfirmLayoutNote(true);
            }
        });

        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> id_notes = new ArrayList<>();
                for(Note note : items){
                    if(note.getCheck()){
                        id_notes.add(note.getId());
                    }
                }
                notePresenter.completedData(id_notes);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> id_notes = new ArrayList<>();
                for(Note note : items){
                    if(note.getCheck()){
                        id_notes.add(note.getId());
                    }
                }
                notePresenter.deleteData(id_notes);
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onResume() {
        super.onResume();
        activityPresenter.setLightStatusBar(getActivity());
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setItemsToRecyclerView(List<Note> newsList, Boolean type) {
        ArrayList<Note> notes = new ArrayList<>();
        for(Note note : all_items){
            if(!note.isStateCompleted().equals(type)){
                notes.add(note);
            }
        }

        adapterList.notifyDataSetChanged();
        items.clear();
        items.addAll(newsList);
        adapter.notifyDataSetChanged();
        notes.addAll(newsList);
        all_items.clear();
        all_items.addAll(notes);

        CreateNotification createNotification = new CreateNotification(getContext());
        createNotification.createNotif();
    }

    @Override
    public void updateItemsListView() {
        adapterList.notifyDataSetChanged();
    }

    @Override
    public void completedNotes(ArrayList<Integer> notes) {

        ArrayList<Note>  nt = (ArrayList<Note>) items.clone();

        for(Note note : items){
            for(Integer id_notes : notes){
                if(note.getId().equals(id_notes)){
                    note.setStateCompleted(true);
                    dbHelper.changeStateCompletedNote(id_notes, true);
                    nt.remove(note);
                    break;
                }
            }
        }

        getItemsListView().get(0).setCount(getItemsListView().get(0).getCount()-notes.size());
        getItemsListView().get(1).setCount(getItemsListView().get(1).getCount()+notes.size());

        ArrayList<Note> notess = new ArrayList<>();
        for(Note note : all_items){
            if(!note.isStateCompleted().equals(false)){
                notess.add(note);
            }
        }

        adapterList.notifyDataSetChanged();
        notess.addAll(nt);

        all_items.clear();
        all_items.addAll(notess);

        items.clear();
        items.addAll(nt);

        adapterList.notifyDataSetChanged();

        updateItemsListView();
        addButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);
        completedButton.setVisibility(View.GONE);
        if(items.size() == 0){
            setEmptyLayout();
        }
        else{
            setLayout();
        }
        adapter.notifyDataSetChanged();

        CreateNotification createNotification = new CreateNotification(getContext());
        createNotification.createNotif();
    }

    @Override
    public void deleteNotes(ArrayList<Integer> notes) {
        ArrayList<Note> its = (ArrayList<Note>) items.clone();

        for(Note note : items){
            for(Integer id_notes : notes){
                if(note.getId().equals(id_notes)){
                    its.remove(note);
                    dbHelper.deleteNote(id_notes);
                    break;
                }
            }
        }

        ArrayList<Note> notess = new ArrayList<>();
        for(Note note : all_items){
            if(note.isStateCompleted().equals(false)){
                notess.add(note);
            }
        }

        adapterList.notifyDataSetChanged();
        notess.addAll(its);
        all_items.clear();
        all_items.addAll(notess);

        getItemsListView().get(1).setCount(getItemsListView().get(1).getCount()-notes.size());
        adapterList.notifyDataSetChanged();

        updateItemsListView();
        items.clear();
        items.addAll(its);
        addButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);
        completedButton.setVisibility(View.GONE);
        if(its.size() == 0){
            setEmptyLayout();
        }
        else{
            setLayout();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<ListItem> getItemsListView() {
        return list_items;
    }

    @Override
    public void showNotAuthLayout() {
        notAuthLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNotAuthLayout() {
        notAuthLayout.setVisibility(View.GONE);
    }

    @Override
    public void setLayout() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showNotConfirmLayout() {
        notConfirmLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNotConfirmLayout() {
        notConfirmLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResponseFailure(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void addNotes(Note note) {

        ArrayList<Note> notes = new ArrayList<>();

        for(Note n : all_items){
            if(n.isStateCompleted().equals(false)){
                notes.add(n);
            }
        }

        items.clear();
        items.addAll(notes);
        items.add(note);
        items.sort(new CompareByDate());

        all_items.add(note);

        getItemsListView().get(0).setCount(notes.size()+1);
        adapterList.notifyDataSetChanged();
        dbHelper.addNote(note);

        adapterList.updateButton(0);
        adapter.notifyDataSetChanged();
        setLayout();

        CreateNotification createNotification = new CreateNotification(getContext());
        createNotification.createNotif();
    }

    @Override
    public void changeNote(Note note) {
        Integer position = 0;
        Integer ii = 0;
        for(Note n : all_items){
            if(n.getId().equals(note.getId())){
                position = ii;
                break;
            }
            ii++;
        }
        all_items.set(position, note);

        ArrayList<Note> notes = new ArrayList<>();

        for(Note n : all_items){
            if(n.isStateCompleted().equals(false)){
                notes.add(n);
            }
        }

        items.clear();
        items.addAll(notes);
        items.sort(new CompareByDate());

        getItemsListView().get(0).setCount(notes.size()+1);
        adapterList.notifyDataSetChanged();

        dbHelper.updateNote(note);

        adapterList.updateButton(0);
        adapter.notifyDataSetChanged();
        setLayout();

        CreateNotification createNotification = new CreateNotification(getContext());
        createNotification.createNotif();
    }

    @Override
    public INotePresenter getNotePresenter() {
        return notePresenter;
    }

    class CompareByAll implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.getId() - o2.getId();
        }
    }

    class CompareByDate implements Comparator<Note> {
        @Override
        public int compare(Note o1, Note o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }

    @Override
    public void setAddButton() {
        addButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);
        completedButton.setVisibility(View.GONE);
    }

    @Override
    public void setDataBaseItems(Integer type) {
        Boolean t = type>0 ? true : false;
        ArrayList<Note> notes = new ArrayList<>();
        for(Note note : all_items){
            if(note.isStateCompleted().equals(t)){
                note.setCheck(false);
                notes.add(note);
            }
        }

        if(notes.size() == 0){
            setEmptyLayout();
        }
        else{
            setLayout();
        }

        Integer count_active = 0;
        Integer count_completed = 0;

        if(!t){
            count_active = notes.size();
            count_completed = all_items.size() - notes.size();
        }
        else{
            count_completed = notes.size();
            count_active = all_items.size() - notes.size();
        }

        getItemsListView().get(0).setCount(count_active);
        getItemsListView().get(1).setCount(count_completed);
        adapterList.notifyDataSetChanged();

        items.clear();
        items.addAll(notes);
        items.sort(new CompareByDate());
        if(items.size()>0){
            setLayout();
        }
        adapter.notifyDataSetChanged();
    }
}
