package ng.com.obkm.myvooz.search.WeekSchedule;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import ng.com.obkm.myvooz.Adapter.ScheduleDayAdapter;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.schedule.sDaySchedule;

public class WeekSchedulePartFragment extends Fragment {

    sDaySchedule daySchedule;
    ScheduleDayAdapter adapter1;
    MainActivity mainActivity;
    Context mContext;
    LinearLayout noLessonsLayout;
    RecyclerView scheduleRecyclerView;
    View view;

    public WeekSchedulePartFragment(MainActivity mainActivity, sDaySchedule daySchedule){
        this.mainActivity = mainActivity;
        this.daySchedule = daySchedule;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.day_schedule_layout, container, false);
        noLessonsLayout = view.findViewById(R.id.no_lessons_layout);

        adapter1 = new ScheduleDayAdapter(mContext, daySchedule.getSchedule(), mainActivity);

        scheduleRecyclerView = view.findViewById(R.id.schedule_day);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        scheduleRecyclerView.setAdapter(adapter1);


        if(daySchedule.getSchedule().size() == 0){
            noLessonsLayout.setVisibility(View.VISIBLE);
            scheduleRecyclerView.setVisibility(View.GONE);
        }
        else{
            noLessonsLayout.setVisibility(View.GONE);
            scheduleRecyclerView.setVisibility(View.VISIBLE);
        }
        TextView dayOfWeek = view.findViewById(R.id.day_of_week);
        dayOfWeek.setText(daySchedule.getNameDay());
        return view;
    }
}
