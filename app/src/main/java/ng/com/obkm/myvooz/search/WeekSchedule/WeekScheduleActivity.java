package ng.com.obkm.myvooz.search.WeekSchedule;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Calendar;

import ng.com.obkm.myvooz.Adapter.WeekPagerAdapter;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi3;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.schedule.WeeksSchedule;
import ng.com.obkm.myvooz.schedule.sDaySchedule;
import ng.com.obkm.myvooz.search.filter.FilterBottomSheetDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class WeekScheduleActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ArrayList<sDaySchedule> items = new ArrayList<sDaySchedule>();
    MainActivity mainActivity;
    WeekPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences mSettings;
    Integer id_week = 0;
    Integer idItem =0;
    Calendar calendar;
    Calendar currentCalendar;

    public static final String APP_PREFERENCES_WEEK_FILTER = "week_filter";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_schedule_layout);
        demoCollectionPagerAdapter = new WeekPagerAdapter(getSupportFragmentManager(), items, mainActivity);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(demoCollectionPagerAdapter);
        CardView returnButton = findViewById(R.id.back_button);
        CardView filterButton = findViewById(R.id.filter_button);
        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(6);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        mSwipeRefreshLayout.setEnabled(false);

        Bundle arguments = getIntent().getExtras();
        String nameItem = arguments.get("name_item").toString();
        idItem = (Integer) arguments.get("id_item");


        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_WEEK_FILTER, id_week);
        editor.commit();

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterBottomSheetDialogFragment addPhotoBottomDialogFragment =
                        new FilterBottomSheetDialogFragment(WeekScheduleActivity.this, nameItem, id_week, calendar);
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        "day_of_week_select_fragment");
            }
        });

        ((CardView) findViewById(R.id.content_l)).setBackgroundResource(R.drawable.card_view_bg);

        getData(idItem, id_week);
    }

    public void getData(Integer id_group, Integer week){
        mSwipeRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SITE_ADDRESS)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi3 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi3.class);
                Call<WeeksSchedule> call = jsonPlaceHolderApi.getMyJSON(id_group.toString(), week);
                Log.d("request", call.request().url().toString());
                call.enqueue(new Callback<WeeksSchedule>() {
                    @Override
                    public void onResponse(Call<WeeksSchedule> call, Response<WeeksSchedule> response) {
                        items.clear();
                        if (!response.isSuccessful()) {
                            return;
                        }
                        WeeksSchedule posts = response.body();
                        int i=0;
                        for (sDaySchedule post : posts.getWeekSchedule()) {
                            items.add(post);
                        }
                        demoCollectionPagerAdapter = new WeekPagerAdapter(getSupportFragmentManager(), items, mainActivity);
                        viewPager.setAdapter(demoCollectionPagerAdapter);
                        demoCollectionPagerAdapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(calendar.get(Calendar.DAY_OF_WEEK)-2);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    @Override
                    public void onFailure(Call<WeeksSchedule> call, Throwable t) {
                    }
                });
            }
        }).start();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                calendar.set(Calendar.DAY_OF_WEEK, position+2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void changeDate(Calendar calendar){
        currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        this.calendar = calendar;
        getData(idItem, calendar.get(Calendar.WEEK_OF_YEAR));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_light_blue));
    }

    @Override
    public void onRefresh() {

    }
}
