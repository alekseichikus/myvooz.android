package ng.com.obkm.myvooz.note;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.note.ChangeNote.view.ChangeNoteActivity;
import ng.com.obkm.myvooz.note.view.INoteView;

public class ButtonsActiveNoteDialogFragment extends DialogFragment {

    LinearLayout changeNoteButton;
    Button completedNoteButton;

    private Note note;
    private INoteView noteView;

    public ButtonsActiveNoteDialogFragment(INoteView noteView, Note note){
        this.note = note;
        this.noteView = noteView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.buttons_active_note, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        initUI(rootView);
        setListeners();

        return rootView;
    }

    public void initUI(View view){
        changeNoteButton = view.findViewById(R.id.change_note_button);
        completedNoteButton = view.findViewById(R.id.completed_note_button);
    }

    public void setListeners(){
        changeNoteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangeNoteActivity.class);
                intent.putExtra(Note.class.getSimpleName(), note);
                getActivity().startActivityForResult(intent, 1);
                dismiss();
            }
        });
        completedNoteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<Integer> items = new ArrayList<>();
                items.add(note.getId());
                noteView.getNotePresenter().completedData(items);
                dismiss();
            }
        });
    }
}
