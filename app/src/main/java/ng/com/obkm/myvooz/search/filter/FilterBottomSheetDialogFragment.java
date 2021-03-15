package ng.com.obkm.myvooz.search.filter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.view.DatePickerDialogFragment;
import ng.com.obkm.myvooz.view.presenter.IDatePickerPresenter;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.search.WeekSchedule.WeekScheduleActivity;

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment implements IDatePickerPresenter {

    Context mContext;
    String nameItem;
    WeekScheduleActivity weekScheduleActivity;
    Integer week;
    Calendar calendar;
    TextView weekItem;

    public FilterBottomSheetDialogFragment(WeekScheduleActivity weekScheduleActivity, String nameItem, Integer week, Calendar calendar){
        this.nameItem = nameItem;
        this.weekScheduleActivity = weekScheduleActivity;
        this.week = week;
        this.calendar = calendar;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.filter_dialog_fragment, container,
                false);
        TextView nameItem = view.findViewById(R.id.name_item);
        weekItem = view.findViewById(R.id.week_item);
        RelativeLayout weekSelectButton = view.findViewById(R.id.week_select);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);

        String ii = Constants.getMonthName(calendar.get(Calendar.MONTH));
        weekItem.setText(Constants.getDayOfWeekNameShort(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " "+ ii);

        weekSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialogFragment bottomSheet = new DatePickerDialogFragment(calendar, FilterBottomSheetDialogFragment.this);
                bottomSheet.show(getFragmentManager(), "exampleBottomSheet");
                dismiss();
            }
        });
        nameItem.setText(this.nameItem);
        return view;

    }

    @Override
    public void changeDate(Calendar calendar) {
        weekScheduleActivity.changeDate(calendar);
        String ii = Constants.getMonthName(calendar.get(Calendar.MONTH));
        weekItem.setText(Constants.getDayOfWeekNameShort(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " "+ ii);
    }
}