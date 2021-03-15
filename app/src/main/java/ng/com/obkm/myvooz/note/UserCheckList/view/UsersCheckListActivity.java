package ng.com.obkm.myvooz.note.UserCheckList.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Adapter.UsersCheckListAdapter;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.model.UsersListData;
import ng.com.obkm.myvooz.note.UserCheckList.presenter.IUserCheckListPresenter;
import ng.com.obkm.myvooz.note.UserCheckList.presenter.UserCheckListPresenter;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class UsersCheckListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, IUsersCheckListView {
    RecyclerView mListView;
    RelativeLayout emptyLayout;
    LinearLayout contentLayout;
    UsersCheckListAdapter mAdapter;
    ArrayList<UserShort> users = new ArrayList<>();
    IActivityPresenter activityPresenter;
    IUserCheckListPresenter userRequestConfirmPresenter;
    CardView backButton;
    CardView resumeButton;
    LinearLayout noInternetConnectionLayout;
    MemoryOperation memoryOperation;

    private Boolean loadData = false;
    private Boolean nextPage = true;
    private Integer id_after_user_select = 0;
    private ImageView loadImageView;
    RelativeLayout loadBlock;
    NestedScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_check_list);

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(UsersCheckListActivity.this);
        userRequestConfirmPresenter = new UserCheckListPresenter(this, this);
        resumeButton = findViewById(R.id.next_button);
        memoryOperation = new MemoryOperation(this);
        mAdapter = new UsersCheckListAdapter(UsersCheckListActivity.this, users, this, resumeButton);

        initUI();
        setListeners();

        userRequestConfirmPresenter.requestItemsFromServer(id_after_user_select);
    }

    void initUI(){
        backButton = findViewById(R.id.back_button);
        emptyLayout = findViewById(R.id.layout_empty);
        contentLayout = findViewById(R.id.content_layout);
        loadImageView = findViewById(R.id.load_image);
        scrollView = findViewById(R.id.scroll_view);
        loadBlock = findViewById(R.id.load_data_block);
        mListView = findViewById(R.id.list);
        mListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mListView.setAdapter(mAdapter);
        noInternetConnectionLayout = findViewById(R.id.no_network_connection_layout);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));

        final Animation animationRotateCorner = AnimationUtils.loadAnimation(
                this, R.anim.rotate_infinity);

        loadImageView.startAnimation(animationRotateCorner);
    }

    void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> u = new ArrayList<>();
                ArrayList<String> pu = new ArrayList<>();
                for(UserShort userShort : users){
                    if(userShort.getCheck()){
                        u.add(userShort.getId());
                        pu.add(userShort.getPhoto());
                    }
                }
                memoryOperation.setUserList(u);
                memoryOperation.setUserPhotoList(pu);
                onBackPressed();
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff <= 200) {
                    if(loadData && nextPage){
                        userRequestConfirmPresenter.requestItemsFromServer(id_after_user_select);
                        loadData=false;
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        userRequestConfirmPresenter.requestItemsFromServer(id_after_user_select);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setItemsToRecyclerView(UsersListData items) {
        for(UserShort user : items.getUsers()){
            if(user.getId() != memoryOperation.getIdUserProfile()){
                users.add(new UserShort(user.getFirstName(), user.getLastName(), user.getPhoto(), user.getDate(), user.getId(), user.getIdUserSelect(), false));
            }
        }
        mAdapter.notifyDataSetChanged();

        if(items.getUsers().size() > 0){
            if(id_after_user_select == 0 && items.getUsers().size() == 1){
                emptyLayout.setVisibility(View.VISIBLE);
            }
            else{
                resumeButton.setVisibility(View.VISIBLE);
            }
            id_after_user_select = items.getUsers().get(items.getUsers().size()-1).getIdUserSelect();
        }

        if(items.isNextPage()){
            nextPage = true;
        }
        else{
            nextPage = false;
            loadBlock.setVisibility(View.GONE);
        }

        loadData = true;
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
    public ArrayList<UserShort> getUsers() {
        return users;
    }
}
