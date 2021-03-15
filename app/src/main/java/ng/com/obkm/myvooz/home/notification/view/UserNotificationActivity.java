package ng.com.obkm.myvooz.home.notification.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;

import ng.com.obkm.myvooz.Adapter.ListAdapter;
import ng.com.obkm.myvooz.Adapter.UserNotificationListAdapter;
import ng.com.obkm.myvooz.home.notification.model.ListItem;
import ng.com.obkm.myvooz.home.notification.model.Notification;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.home.notification.presenter.IUserNotificationPresenter;
import ng.com.obkm.myvooz.home.notification.presenter.UserNotificationPresenter;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class UserNotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, IUserNotificationView {
    ArrayList<Notification> items = new ArrayList<>();
    ArrayList<ListItem> list_items = new ArrayList<>();
    UserNotificationListAdapter mAdapter;
    ListView mListView;
    LinearLayout emptyView;
    CardView accountNotConfirmedLayout;
    LinearLayout noInternetConnectionLayout;
    CardView notAuthLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private IUserNotificationPresenter userNotificationPresenter;
    MemoryOperation memoryOperation;
    IActivityPresenter activityPresenter;
    ListAdapter adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);

        memoryOperation = new MemoryOperation(UserNotificationActivity.this);
        userNotificationPresenter = new UserNotificationPresenter(this, UserNotificationActivity.this, memoryOperation);

        initUI();
        setListeners();

        userNotificationPresenter.getListPresenter().requestData(0);
        userNotificationPresenter.addListItems();
        activityPresenter = new ActivityPresenter(UserNotificationActivity.this);
        activityPresenter.setLightStatusBar(UserNotificationActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void initUI(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        noInternetConnectionLayout = findViewById(R.id.no_network_connection_layout);
        notAuthLayout = findViewById(R.id.layout_not_auth);
        mListView = findViewById(R.id.list);
        mAdapter = new UserNotificationListAdapter(UserNotificationActivity.this, items);
        mListView.setAdapter(mAdapter);
        emptyView = findViewById(R.id.empty_layer);
        accountNotConfirmedLayout = findViewById(R.id.account_not_confirmed_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        mSwipeRefreshLayout.setEnabled(false);

        RecyclerView scheduleRecyclerView = findViewById(R.id.recycler_list_view);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(UserNotificationActivity.this, LinearLayoutManager.HORIZONTAL, false));
        adapterList = new ListAdapter(UserNotificationActivity.this, list_items, userNotificationPresenter.getListPresenter());
        scheduleRecyclerView.setAdapter(adapterList);
    }

    void setListeners(){
        CardView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CardView notAuthLayoutButton = findViewById(R.id.hide_not_auth_layout_button);

        notAuthLayoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                notAuthLayout.setVisibility(View.GONE);
                memoryOperation.setAuthLayoutNotification(true);
            }
        });

        CardView notConfirmLayoutButton = findViewById(R.id.hide_not_confirm_layout_button);

        notConfirmLayoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                accountNotConfirmedLayout.setVisibility(View.GONE);
                memoryOperation.setConfirmLayoutNotification(true);
            }
        });
    }

    @Override
    public void onRefresh() {
        userNotificationPresenter.getListPresenter().requestData(0);
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
    public void setToRecyclerView(ArrayList<Notification> items) {
        this.items.clear();
        this.items.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListItems(ArrayList<ListItem> items) {
        this.list_items.clear();
        this.list_items.addAll(items);
        adapterList.notifyDataSetChanged();
    }

    @Override
    public void setNoInternetConnectionLayout() {
        noInternetConnectionLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        accountNotConfirmedLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        notAuthLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResponseFailure(Throwable t) {
        Toast.makeText(UserNotificationActivity.this, "Произошла какая-то бяка", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNotAuthLayout() {
        notAuthLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNotAuthLayout() {
        notAuthLayout.setVisibility(View.GONE);
    }

    @Override
    public void showNotConfirmLayout() {
        accountNotConfirmedLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNotConfirmLayout() {
        accountNotConfirmedLayout.setVisibility(View.GONE);
    }

    @Override
    public void setLayout() {
        emptyView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEmptyLayout() {
        this.items.clear();
        mAdapter.notifyDataSetChanged();
        noInternetConnectionLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        accountNotConfirmedLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        notAuthLayout.setVisibility(View.GONE);
    }
}
