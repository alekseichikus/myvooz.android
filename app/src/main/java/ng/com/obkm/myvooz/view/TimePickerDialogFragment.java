package ng.com.obkm.myvooz.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.view.presenter.ITimePickerPresenter;

public class TimePickerDialogFragment extends BottomSheetDialogFragment implements TimePickerDialog.OnTimeSetListener {

    Context context;
    Calendar calendar;
    ITimePickerPresenter timePickerPresenter;
    public TimePickerDialogFragment(Context context, Calendar calendar, ITimePickerPresenter timePickerPresenter){
        this.context = context;
        this.calendar = calendar;
        this.timePickerPresenter = timePickerPresenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), R.style.Dialog_Theme, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timePickerPresenter.changeTime(hourOfDay, minute);
        dismiss();
    }
}

