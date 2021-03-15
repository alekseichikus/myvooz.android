package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;

import java.util.ArrayList;

import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.DaySchedule;

public class ScheduleLessonAdapter extends LoopingPagerAdapter<DaySchedule> {

    private static final int VIEW_TYPE_NORMAL = 100;
    MainActivity mainActivity;

    public ScheduleLessonAdapter(Context context, ArrayList<DaySchedule> itemList, boolean isInfinite, MainActivity mainActivity) {
        super(context, itemList, isInfinite);
        this.mainActivity = mainActivity;
    }

    @Override
    protected int getItemViewType(int listPosition) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        View view;

        if(itemList.get(listPosition).getId() !=0){
            view  = LayoutInflater.from(context).inflate(R.layout.lesson_layout, container, false);
        }
        else{
            view  = LayoutInflater.from(context).inflate(R.layout.empty_lesson_layout, container, false);
        }
        return view;
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        TextView itemTitle = convertView.findViewById(R.id.item_lesson_name);
        TextView itemTypeText = convertView.findViewById(R.id.item_type_lesson);
        TextView itemNumberText = convertView.findViewById(R.id.item_number_lesson);
        TextView itemTeacherText = convertView.findViewById(R.id.item_teacher_name);
        TextView itemClassroomText = convertView.findViewById(R.id.item_classroom_name);
        TextView itemTimeText = convertView.findViewById(R.id.time_label);

        if(itemList.get(listPosition).getId() == 0){
            itemTimeText.setText(itemList.get(listPosition).getTimeF() + " - " + itemList.get(listPosition).getTimeL());
        }
        else{
            itemTitle.setText(itemList.get(listPosition).getName());
            itemTypeText.setText(itemList.get(listPosition).getNameType().toUpperCase());
            itemTeacherText.setText(itemList.get(listPosition).getName_teacher());
            itemClassroomText.setText(itemList.get(listPosition).getClassroom());
        }
        itemNumberText.setText(itemList.get(listPosition).getNumber() + ".");
    }
}