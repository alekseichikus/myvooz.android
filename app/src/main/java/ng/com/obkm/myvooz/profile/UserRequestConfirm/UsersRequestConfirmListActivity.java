package ng.com.obkm.myvooz.profile.UserRequestConfirm;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ng.com.obkm.myvooz.Adapter.UsersRequestConfirmAdapter;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.presenter.IUserRequestConfirmPresenter;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.presenter.UserRequestConfirmPresenter;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.view.IUserRequestConfirmView;

public class UsersRequestConfirmListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, IUserRequestConfirmView {
    ListView mListView;
    LinearLayout emptyLayout;
    SwipeRefreshLayout contentLayout;
    UsersRequestConfirmAdapter mAdapter;
    ArrayList<UserShort> users = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    IActivityPresenter activityPresenter;
    IUserRequestConfirmPresenter userRequestConfirmPresenter;
    CardView backButton;
    LinearLayout noInternetConnectionLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_request_confirm_list);

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(UsersRequestConfirmListActivity.this);
        userRequestConfirmPresenter = new UserRequestConfirmPresenter(this, this);
        mAdapter = new UsersRequestConfirmAdapter(UsersRequestConfirmListActivity.this, users, this, this, userRequestConfirmPresenter);

        initUI();
        setListeners();

        userRequestConfirmPresenter.requestItemsFromServer();
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        emptyLayout = findViewById(R.id.empty_layer);
        contentLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        mListView = findViewById(R.id.list);
        noInternetConnectionLayout = findViewById(R.id.no_network_connection_layout);
        mListView.setAdapter(mAdapter);
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onRefresh() {
        userRequestConfirmPresenter.requestItemsFromServer();
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
    public void setItemsToRecyclerView(List<UserShort> items) {
        users.clear();
        users.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    @Override
    public void showListLayout() {
        emptyLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        noInternetConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        noInternetConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void setNoInternetConnectionLayout() {
        emptyLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        noInternetConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public IUserRequestConfirmPresenter getUserRequestConfirmPresenter() {
        return userRequestConfirmPresenter;
    }

    @Override
    public ArrayList<UserShort> getUsers() {
        return users;
    }
}
