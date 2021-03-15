package ng.com.obkm.myvooz.Adapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

import ng.com.obkm.myvooz.search.WeekSchedule.WeekSchedulePartFragment;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.schedule.sDaySchedule;


public class WeekPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> nameDays = new ArrayList<>();
    MainActivity mainActivity;
    ArrayList<sDaySchedule> items;

    public WeekPagerAdapter(FragmentManager fm, ArrayList<sDaySchedule> items, MainActivity mainActivity) {
        super(fm);
        this.mainActivity = mainActivity;
        this.items = items;
        nameDays.add("Пн");
        nameDays.add("Вт");
        nameDays.add("Ср");
        nameDays.add("Чт");
        nameDays.add("Пт");
        nameDays.add("Сб");
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Fragment getItem(int i) { ;
        return new WeekSchedulePartFragment(mainActivity, items.get(i));
    }

    @NonNull


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return nameDays.get(position);
    }
}