package ng.com.obkm.myvooz.home.presenter;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.home.view.HomeFragment;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class HomeFragmentPresenter implements IHomeFragmentPresenter {

    private HomeFragment homeFragment;
    private Calendar calendar;
    private Calendar currentCalendar;

    private Integer id_group = 0;
    private Integer state = 0;
    private MemoryOperation memoryOperation;

    public HomeFragmentPresenter(HomeFragment homeFragment, MemoryOperation memoryOperation){
        this.homeFragment = homeFragment;

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Log.d("esvsd", String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
        currentCalendar = (Calendar) calendar.clone();
        this.memoryOperation = memoryOperation;

        id_group = memoryOperation.getIdGroup();
        state = memoryOperation.getSwitchHideEmptyLesson();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean isChangeGroup() {
        if(id_group.equals(memoryOperation.getIdGroup()))
            return false;
        id_group = memoryOperation.getIdGroup();
        return true;
    }

    @Override
    public boolean isChangeSwitchHideEmptyLesson() {
        if(state.equals(memoryOperation.getSwitchHideEmptyLesson()))
            return false;
        state = memoryOperation.getSwitchHideEmptyLesson();
        return true;
    }

    @Override
    public void checkChange() {
        if(isChangeGroup() || isChangeSwitchHideEmptyLesson()){
            homeFragment.daySchedulePresenter.requestDayScheduleServer(memoryOperation.getIdGroup(), calendar, memoryOperation.getSwitchHideEmptyLesson());
            homeFragment.newsListPresenter.requestNewsFromServer(memoryOperation.getIdGroup());
        }
    }



    @Override
    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    @Override
    public void setCalendar(Calendar calendar) {
        this.calendar = (Calendar) calendar.clone();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setBlueStatusBar() {
        Window window = homeFragment.getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(homeFragment.getContext(), R.color.color_light_blue));
    }
}
