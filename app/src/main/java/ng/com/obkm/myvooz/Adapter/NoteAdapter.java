package ng.com.obkm.myvooz.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.note.ButtonsActiveNoteDialogFragment;
import ng.com.obkm.myvooz.note.ButtonsCompletedNoteDialogFragment;
import ng.com.obkm.myvooz.note.view.INoteView;
import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class NoteAdapter extends BaseAdapter {
    private AppCompatActivity context;
    private ArrayList<Note> items;
    private ArrayList<CardView> buttons;
    private MemoryOperation memoryOperation;
    private  ListAdapter listAdapter;
    private INoteView noteView;

    public NoteAdapter(INoteView noteView, AppCompatActivity context, ArrayList<Note> items, ArrayList<CardView> buttons, ListAdapter listAdapter, MemoryOperation memoryOperation) {
        this.context = context;
        this.items = items;
        this.memoryOperation = memoryOperation;
        this.buttons = buttons;
        this.listAdapter = listAdapter;
        this.noteView = noteView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployeeHolder holder;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.row_note, parent, false);
            holder=new EmployeeHolder();
            holder.itemName = convertView.findViewById(R.id.item_name_note);
            holder.itemText = convertView.findViewById(R.id.item_text_note);
            holder.selectButton = convertView.findViewById(R.id.select_button);
            holder.selectButton2 = convertView.findViewById(R.id.select_button_2);
            holder.itemDate = convertView.findViewById(R.id.item_date);
            holder.itemStateLine = convertView.findViewById(R.id.state_line);
            holder.itemMarkMeText = convertView.findViewById(R.id.group_mark_name);
            holder.selectState = convertView.findViewById(R.id.select_state);
            holder.itemNameObject = convertView.findViewById(R.id.item_name_object_note);
            holder.photos = convertView.findViewById(R.id.photos);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EmployeeHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.itemText.getMaxLines() == 2){
                    holder.itemText.setMaxLines(Integer.MAX_VALUE);
                }
                else{
                    holder.itemText.setMaxLines(2);
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!items.get(position).isStateCompleted()){
                    if(items.get(position).getIdUser().equals(memoryOperation.getIdUserProfile())){
                        ButtonsActiveNoteDialogFragment dialogFragment = new ButtonsActiveNoteDialogFragment(noteView, items.get(position));
                        dialogFragment.show(context.getSupportFragmentManager(), "Sample Fragment");
                    }
                    else{
                        if(!memoryOperation.getIdGroupOfUser().equals(0) && memoryOperation.getIdUserProfile().equals(memoryOperation.getIdGroupOfUser())){
                            ButtonsActiveNoteDialogFragment dialogFragment = new ButtonsActiveNoteDialogFragment(noteView, items.get(position));
                            dialogFragment.show(context.getSupportFragmentManager(), "Sample Fragment");
                        }
                    }
                }
                else{
                    if(items.get(position).getIdUser().equals(memoryOperation.getIdUserProfile())){
                        ButtonsCompletedNoteDialogFragment dialogFragment = new ButtonsCompletedNoteDialogFragment(noteView, items.get(position));
                        dialogFragment.show(context.getSupportFragmentManager(), "Sample Fragment");
                    }
                    else{
                        if(!memoryOperation.getIdGroupOfUser().equals(0) && memoryOperation.getIdUserProfile().equals(memoryOperation.getIdGroupOfUser())){
                            ButtonsCompletedNoteDialogFragment dialogFragment = new ButtonsCompletedNoteDialogFragment(noteView, items.get(position));
                            dialogFragment.show(context.getSupportFragmentManager(), "Sample Fragment");
                        }
                    }
                }
                return false;
            }
        });

        if(items.get(position).getIdUser().equals(memoryOperation.getIdUserProfile())){
            holder.selectButton2.setVisibility(View.VISIBLE);
            holder.selectButton.setEnabled(true);
        }
        else{
            if(!memoryOperation.getIdGroupOfUser().equals(0) && memoryOperation.getIdUserProfile().equals(memoryOperation.getIdGroupOfUser())){
                holder.selectButton2.setVisibility(View.VISIBLE);
                holder.selectButton.setEnabled(true);
            }
            else{
                holder.selectButton2.setVisibility(View.GONE);
                holder.selectButton.setEnabled(false);
            }
        }

        holder.itemText.setMaxLines(2);
        holder.selectState.setCardBackgroundColor(Color.WHITE);
        holder.selectButton2.setCardBackgroundColor(0xffe1e2e3);
        holder.itemName.setText(items.get(position).getName());

        Calendar cal_next_day = Calendar.getInstance();
        cal_next_day.setTimeInMillis(System.currentTimeMillis());
        cal_next_day.add(Calendar.DATE, 1);

        Calendar cal_cur = Calendar.getInstance();
        cal_cur.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            calendar.setTime(sdf.parse(items.get(position).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String ii = Constants.getMonthName(calendar.get(Calendar.MONTH));
        String minutes = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));

        if(items.get(position).getCheck()){
            holder.itemStateLine.setVisibility(View.GONE);
        }
        else{
            holder.itemStateLine.setVisibility(View.VISIBLE);
        }

        if(cal_cur.after(calendar)){
            String mm = calendar.get(Calendar.MONTH) < 10 ? "0" + calendar.get(Calendar.MONTH) : String.valueOf(calendar.get(Calendar.MONTH));
            holder.itemDate.setText("Истек, " + calendar.get(Calendar.DAY_OF_MONTH) + " "+ ii + " в "+ calendar.get(Calendar.HOUR_OF_DAY) + ":"+ minutes);
            holder.itemStateLine.setBackgroundResource(R.drawable.gradient_orange_3_linear);
            holder.itemStateLine.setVisibility(View.VISIBLE);
        }
        else{
            if(cal_next_day.after(calendar)){
                holder.itemStateLine.setBackgroundResource(R.drawable.gradient_orange_3_linear);
                holder.itemStateLine.setVisibility(View.VISIBLE);
                if(cal_cur.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)){
                    holder.itemDate.setText("Сегодня в "+ calendar.get(Calendar.HOUR_OF_DAY) + ":"+ minutes);
                }
                else{
                    holder.itemDate.setText("Завтра в "+ calendar.get(Calendar.HOUR_OF_DAY) + ":"+ minutes);
                }
            }
            else{
                holder.itemStateLine.setBackgroundResource(R.drawable.gradient_orange_4_linear);
                holder.itemStateLine.setVisibility(View.GONE);
                holder.itemDate.setText(Constants.getDayOfWeekNameShort(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " "+ ii + " в "+ calendar.get(Calendar.HOUR_OF_DAY) + ":"+ minutes);
            }
        }



        holder.itemText.setText(items.get(position).getText());
        holder.itemNameObject.setText(items.get(position).getNameObject());
        holder.photos.setLayoutManager(new LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false));
        ImageListAdapter adapter = new ImageListAdapter(context, items.get(position).getPhotos(), 0);
        holder.photos.setAdapter(adapter);

        View finalConvertView = convertView;

        if(items.get(position).isMarkMe()){
            holder.itemMarkMeText.setText("Личное");
        }
        else{
            holder.itemMarkMeText.setText(memoryOperation.getNameGroupProfile());
        }

        if(items.get(position).getCheck()){
            holder.selectState.setCardBackgroundColor(0xff5c9ce6);
            holder.selectButton2.setCardBackgroundColor(0xff5c9ce6);
            finalConvertView.setBackgroundColor(0xfff7f8f9);
        }
        else{
            holder.selectState.setCardBackgroundColor(Color.WHITE);
            holder.selectButton2.setCardBackgroundColor(0xffe1e2e3);
            finalConvertView.setBackgroundColor(Color.WHITE);
        }

        holder.selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.get(position).getCheck()){
                    holder.selectState.setCardBackgroundColor(Color.WHITE);
                    holder.selectButton2.setCardBackgroundColor(0xffe1e2e3);
                    items.get(position).setCheck(false);
                    finalConvertView.setBackgroundColor(Color.WHITE);
                    if(cal_cur.after(calendar)){
                        holder.itemStateLine.setBackgroundResource(R.drawable.gradient_orange_3_linear);
                        holder.itemStateLine.setVisibility(View.VISIBLE);
                    }
                    else{
                        if(cal_next_day.after(calendar)){
                            holder.itemStateLine.setBackgroundResource(R.drawable.gradient_orange_3_linear);
                            holder.itemStateLine.setVisibility(View.VISIBLE);
                        }
                        else{
                            holder.itemStateLine.setBackgroundResource(R.drawable.gradient_orange_4_linear);
                            holder.itemStateLine.setVisibility(View.GONE);
                        }
                    }
                }
                else{
                    holder.selectState.setCardBackgroundColor(0xff5c9ce6);
                    holder.selectButton2.setCardBackgroundColor(0xff5c9ce6);
                    finalConvertView.setBackgroundColor(0xfff7f8f9);
                    items.get(position).setCheck(true);
                    holder.itemStateLine.setVisibility(View.GONE);
                }


                Boolean isCheck = false;
                for(Note note : items){
                    if(note.getCheck()){
                        isCheck = true;
                        break;
                    }
                }

                if(listAdapter.getTypeSelectItem().equals(0)){
                    if(isCheck.equals(true)){
                        buttons.get(0).setVisibility(View.GONE);
                        buttons.get(1).setVisibility(View.VISIBLE);
                        buttons.get(2).setVisibility(View.GONE);
                    }
                    else{
                        buttons.get(0).setVisibility(View.VISIBLE);
                        buttons.get(1).setVisibility(View.GONE);
                        buttons.get(2).setVisibility(View.GONE);
                    }
                }
                else{
                    if(isCheck.equals(true)){
                        buttons.get(0).setVisibility(View.GONE);
                        buttons.get(1).setVisibility(View.GONE);
                        buttons.get(2).setVisibility(View.VISIBLE);
                    }
                    else{
                        buttons.get(0).setVisibility(View.VISIBLE);
                        buttons.get(1).setVisibility(View.GONE);
                        buttons.get(2).setVisibility(View.GONE);
                    }
                }
            }
        });
        return convertView;
    }

    public class EmployeeHolder {
        private TextView itemName;
        private TextView itemText;
        private TextView itemNameObject;
        private TextView itemMarkMeText;
        private TextView itemDate;
        private RelativeLayout selectButton;
        private CardView selectButton2;
        private CardView selectState;
        private RecyclerView photos;
        private FrameLayout itemStateLine;
    }
}