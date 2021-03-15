package ng.com.obkm.myvooz.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.view.presenter.IDatePickerPresenter;

public class DatePickerDialogFragment extends BottomSheetDialogFragment {
    Calendar calendar;
    IDatePickerPresenter datePickerPresenter;

    public DatePickerDialogFragment(Calendar calendar, IDatePickerPresenter datePickerPresenter) {
        this.calendar = calendar;
        this.datePickerPresenter =  datePickerPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_dialog_fragment, container,
                false);

        CalendarView calendarView = view.findViewById(R.id.calendar_view);

        long milliTime = calendar.getTimeInMillis();
        calendarView.setDate(milliTime);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                datePickerPresenter.changeDate(calendar);
                dismiss();
            }
        });
        return view;
    }
}

