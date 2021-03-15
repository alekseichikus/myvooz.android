package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.DaySchedule;

public class ScheduleDayAdapter extends RecyclerView.Adapter<ScheduleDayAdapter.CustomViewHolder>{
    private Context context;
    private ArrayList<ArrayList<DaySchedule>> items;
    ScheduleLessonAdapter adapter;
    MainActivity mainActivity;

    public ScheduleDayAdapter(Context context, ArrayList<ArrayList<DaySchedule>> items, MainActivity mainActivity) {
        this.context = context;
        this.items = items;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.schedule_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        adapter = new ScheduleLessonAdapter(context, items.get(position), true, mainActivity);
        holder.viewPager.setAdapter(adapter);
        holder.indicatorView.setCount(holder.viewPager.getIndicatorCount());

        if(items.get(position).size() > 1){
            holder.someLessonsBlock.setVisibility(View.VISIBLE);
        }
        if(items.get(position).get(0).getId() == 0){
            holder.infoBlock.setVisibility(View.GONE);
        }
        else{
            holder.timeLabel.setText(items.get(position).get(0).getTimeF() + " - " + items.get(position).get(0).getTimeL());
            holder.infoBlock.setVisibility(View.VISIBLE);
        }

        holder.viewPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
                holder.indicatorView.setProgress(selectingPosition, progress);
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
//                indicatorView.setSelection(newIndicatorPosition);
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private LoopingViewPager viewPager;
        private PageIndicatorView indicatorView;
        private RelativeLayout someLessonsBlock;
        private LinearLayout infoBlock;
        private TextView timeLabel;
        public CustomViewHolder(View view) {
            super(view);
            viewPager = view.findViewById(R.id.viewpager);
            timeLabel = view.findViewById(R.id.time_label);
            indicatorView = view.findViewById(R.id.indicator);
            infoBlock = view.findViewById(R.id.info_block);
            someLessonsBlock = view.findViewById(R.id.item_some_lessons_block);
        }
    }
}