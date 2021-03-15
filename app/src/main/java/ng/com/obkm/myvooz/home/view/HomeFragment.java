package ng.com.obkm.myvooz.home.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ng.com.obkm.myvooz.model.Story;
import ng.com.obkm.myvooz.view.DatePickerDialogFragment;
import ng.com.obkm.myvooz.model.DaySchedule;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.Adapter.ScheduleDayAdapter;
import ng.com.obkm.myvooz.home.notification.view.UserNotificationActivity;
import ng.com.obkm.myvooz.home.daySchedule.presenter.DaySchedulePresenter;
import ng.com.obkm.myvooz.model.News;
import ng.com.obkm.myvooz.home.news.presenter.NewsListPresenter;
import ng.com.obkm.myvooz.Adapter.NewsAdapter;
import ng.com.obkm.myvooz.home.presenter.HomeFragmentPresenter;
import ng.com.obkm.myvooz.home.presenter.IHomeFragmentPresenter;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.view.presenter.IDatePickerPresenter;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IHomeFragmentView, IDatePickerPresenter {

    private ArrayList<News> news_list = new ArrayList<>();
    private ArrayList<ArrayList<DaySchedule>> schedule_list = new ArrayList<> ();
    private ArrayList<ArrayList<DaySchedule>> schedule_list_adapter = new ArrayList<> ();
    private NewsAdapter adapter;
    private ScheduleDayAdapter adapter1;
    private NestedScrollView scrollView;
    private MainActivity mainActivity;
    private LinearLayout noNetworkConnectLayout;
    private CardView dayScheduleLayout;
    private CardView hideEmptyLessonButton;
    private CardView showEmptyLessonButton;
    private LinearLayout noLessonsLayout;
    private View view;
    private CardView returnCurrentDayButton;
    private TextView dateToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout datePickerButton;
    private CardView againConnectButton;
    private CardView notificationButton;
    public NewsListPresenter newsListPresenter;
    public DaySchedulePresenter daySchedulePresenter;
    private IHomeFragmentPresenter homeFragmentPresenter;
    private MemoryOperation memoryOperation;
    private IActivityPresenter activityPresenter;
    private TextView dayOfWeekName;
    private RecyclerView recyclerView;
    private Boolean is_show_empty_lessons = true;

    public HomeFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(view == null){
            newsListPresenter = new NewsListPresenter(this, getContext());
            daySchedulePresenter = new DaySchedulePresenter(this, getContext());
            memoryOperation = new MemoryOperation(getContext());
            homeFragmentPresenter = new HomeFragmentPresenter(this, memoryOperation);
            activityPresenter = new ActivityPresenter(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);

            adapter = new NewsAdapter(getContext(), news_list, mainActivity);
            adapter1 = new ScheduleDayAdapter(getContext(), schedule_list_adapter, mainActivity);

            initUI(view);
            setListeners();

            if(memoryOperation.isShowEmptyLessons()){
                hideEmptyLessonButton.setVisibility(View.GONE);
                showEmptyLessonButton.setVisibility(View.VISIBLE);
            }
            else{
                hideEmptyLessonButton.setVisibility(View.VISIBLE);
                showEmptyLessonButton.setVisibility(View.GONE);
            }

            is_show_empty_lessons = memoryOperation.isShowEmptyLessons();
            newsListPresenter.requestNewsFromServer(memoryOperation.getIdGroup());
            daySchedulePresenter.requestDayScheduleServer(memoryOperation.getIdGroup(), homeFragmentPresenter.getCalendar(), memoryOperation.getSwitchHideEmptyLesson());
            dateToolbar.setText(homeFragmentPresenter.getCalendar().get(Calendar.DAY_OF_MONTH) + " " + Constants.getMonthName(homeFragmentPresenter.getCalendar().get(Calendar.MONTH)));

            dayScheduleLayout.setBackgroundResource(R.drawable.card_view_bg);
        }

        return view;
    }

    private void initUI(View view){
        noNetworkConnectLayout = view.findViewById(R.id.no_network_connection_layout);
        dayScheduleLayout = view.findViewById(R.id.day_schedule_layout);
        noLessonsLayout = view.findViewById(R.id.no_lessons_layout);
        scrollView = view.findViewById(R.id.scrollview);
        hideEmptyLessonButton = view.findViewById(R.id.hide_empty_lesson_button);
        showEmptyLessonButton = view.findViewById(R.id.show_empty_lesson_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        returnCurrentDayButton = view.findViewById(R.id.return_current_day_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        RecyclerView scheduleRecyclerView = view.findViewById(R.id.schedule_day);
        dateToolbar = view.findViewById(R.id.dateToolbar);
        notificationButton = view.findViewById(R.id.notification_button);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        scheduleRecyclerView.setAdapter(adapter1);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        datePickerButton = view.findViewById(R.id.datepicker_button);
        againConnectButton = view.findViewById(R.id.reconnect_button);
        dayOfWeekName = view.findViewById(R.id.day_of_week);
        dayOfWeekName.setText(Constants.getDayOfWeekName(homeFragmentPresenter.getCalendar().get(Calendar.DAY_OF_WEEK)));

    }

    private  void setListeners(){
        againConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsListPresenter.requestNewsFromServer(memoryOperation.getIdGroup());
                daySchedulePresenter.requestDayScheduleServer(memoryOperation.getIdGroup(), homeFragmentPresenter.getCalendar(), memoryOperation.getSwitchHideEmptyLesson());
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserNotificationActivity.class);
                startActivity(intent);
            }
        });

        hideEmptyLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmptyLessonButton.setVisibility(View.GONE);
                showEmptyLessonButton.setVisibility(View.VISIBLE);
                is_show_empty_lessons = true;
                memoryOperation.setShowEmptyLessons(true);
                setScheduleOfDayToRecyclerView((ArrayList<ArrayList<DaySchedule>>) HomeFragment.this.schedule_list.clone());
            }
        });

        showEmptyLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmptyLessonButton.setVisibility(View.VISIBLE);
                showEmptyLessonButton.setVisibility(View.GONE);
                is_show_empty_lessons = false;
                memoryOperation.setShowEmptyLessons(false);
                setScheduleOfDayToRecyclerView((ArrayList<ArrayList<DaySchedule>>) HomeFragment.this.schedule_list.clone());
            }
        });

        returnCurrentDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragmentPresenter.setCalendar(homeFragmentPresenter.getCurrentCalendar());
                dateToolbar.setText(homeFragmentPresenter.getCalendar().get(Calendar.DAY_OF_MONTH) + " " + Constants.getMonthName(homeFragmentPresenter.getCalendar().get(Calendar.MONTH)));
                daySchedulePresenter.requestDayScheduleServer(memoryOperation.getIdGroup(), homeFragmentPresenter.getCalendar(), memoryOperation.getSwitchHideEmptyLesson());
            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment bottomSheet = new DatePickerDialogFragment(homeFragmentPresenter.getCalendar(), HomeFragment.this);
                bottomSheet.show(getFragmentManager(), "exampleBottomSheet");
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                float alp = (float) (1 - scrollView.getScrollY()/216.0);
                recyclerView.setAlpha(alp);
            }
        });
    }

    @Override
    public void onRefresh() {
        daySchedulePresenter.requestDayScheduleServer(memoryOperation.getIdGroup(), homeFragmentPresenter.getCalendar(), memoryOperation.getSwitchHideEmptyLesson());
        newsListPresenter.requestNewsFromServer(memoryOperation.getIdGroup());
    }

    @Override
    public void onResume() {
        super.onResume();
        homeFragmentPresenter.checkChange();
        homeFragmentPresenter.setBlueStatusBar();
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setNewsToRecyclerView(List<News> newsList) {
        this.news_list.clear();
        if(newsList.size()>0){
            this.news_list.addAll(newsList);
        }
        else{
            ArrayList<Story> stories = new ArrayList<>();
            stories.add(new Story("http://myvooz.ru/public/publications/story_default.png", "кееек"));
            this.news_list.add(new News("http://myvooz.ru/public/publications/666.png", "Медиацентр, тебе с нами!", "https://myvooz.ru/public/images/communities/icon-myvoooz-40.png", "Мой ВУЗ", stories, ""));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setScheduleOfDayToRecyclerView(ArrayList<ArrayList<DaySchedule>> weekSchedule) {
        this.schedule_list_adapter.clear();
        this.schedule_list.clear();
        this.schedule_list.addAll(weekSchedule);
        if(weekSchedule.size() != 0){
            if(is_show_empty_lessons){
                for(int i=0; i < Constants.MAX_LESSONS_OF_UNIVERSITY_COUNT; i++){
                    Boolean is_isset_lesson = false;
                    for(ArrayList<DaySchedule> ds : weekSchedule){
                        if(ds.get(0).getNumber().equals(i+1)){
                            schedule_list_adapter.add(ds);
                            is_isset_lesson = true;
                            break;
                        }
                    }
                    if(!is_isset_lesson){
                        ArrayList<DaySchedule> ds = new ArrayList<>();
                        ds.add(new DaySchedule(0, "", "", "", "", i+1, "", "", 0, 0, 0));
                        schedule_list_adapter.add(ds);
                    }
                }
            }
            else{
                this.schedule_list_adapter.addAll(weekSchedule);
            }
        }


        adapter1.notifyDataSetChanged();
    }

    @Override
    public void showEmptyDayScheduleLayout() {
        noLessonsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyDayScheduleLayout() {
        noLessonsLayout.setVisibility(View.GONE);
    }

    @Override
    public void showReturnButton() {
        returnCurrentDayButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReturnButton() {
        returnCurrentDayButton.setVisibility(View.GONE);
    }

    @Override
    public void setNoInternetConnectionLayout() {
        dayScheduleLayout.setVisibility(View.GONE);
        noNetworkConnectLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDayScheduleLayout() {
        dayScheduleLayout.setVisibility(View.VISIBLE);
        noNetworkConnectLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Произошла какая-то бяка", Toast.LENGTH_LONG).show();
    }

    @Override
    public IHomeFragmentPresenter getHomeFragmentPresenter() {
        return homeFragmentPresenter;
    }

    @Override
    public void changeDate(Calendar calendar) {
        homeFragmentPresenter.setCalendar(calendar);
        dateToolbar.setText(homeFragmentPresenter.getCalendar().get(Calendar.DAY_OF_MONTH) + " " + Constants.getMonthName(homeFragmentPresenter.getCalendar().get(Calendar.MONTH)));
        daySchedulePresenter.requestDayScheduleServer(memoryOperation.getIdGroup(), homeFragmentPresenter.getCalendar(), memoryOperation.getSwitchHideEmptyLesson());
        dayOfWeekName.setText(Constants.getDayOfWeekName(homeFragmentPresenter.getCalendar().get(Calendar.DAY_OF_WEEK)));
    }
}
