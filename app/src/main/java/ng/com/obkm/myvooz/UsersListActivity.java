package ng.com.obkm.myvooz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Adapter.UsersGroupListAdapter;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi16;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.model.UsersListData;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ng.com.obkm.myvooz.utils.Constants.SITE_ADDRESS;

public class UsersListActivity extends AppCompatActivity {
    SharedPreferences mSettings;
    RecyclerView mListView;
    UsersGroupListAdapter mAdapter;
    ArrayList<UserShort> users = new ArrayList<>();
    private MemoryOperation memoryOperation;
    private Boolean loadData = false;
    private Boolean nextPage = true;
    private Integer id_after_user_select = 0;
    private LinearLayout contentLayout;
    private ImageView loadImageView;
    RelativeLayout loadBlock;
    NestedScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        mSettings = PreferenceManager.getDefaultSharedPreferences(UsersListActivity.this);

        memoryOperation = new MemoryOperation(this);

        CardView backButton = findViewById(R.id.back_button);
        contentLayout = findViewById(R.id.content_layout);
        loadImageView = findViewById(R.id.load_image);
        scrollView = findViewById(R.id.scroll_view);
        loadBlock = findViewById(R.id.load_data_block);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));

        final Animation animationRotateCorner = AnimationUtils.loadAnimation(
                this, R.anim.rotate_infinity);

        loadImageView.startAnimation(animationRotateCorner);

        mAdapter = new UsersGroupListAdapter(UsersListActivity.this, users, UsersListActivity.this);
        mListView = findViewById(R.id.list);
        mListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mListView.setAdapter(mAdapter);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setListeners();

        getData();
    }

    public void setListeners(){
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff <= 200) {
                    if(loadData && nextPage){
                        getData();
                        loadData=false;
                    }
                }
            }
        });
    }

    public void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi16 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi16.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(UsersListActivity.this);
        Call<UsersListData> call = jsonPlaceHolderApi.getMyJSON(mSettings.getString(Constants.APP_PREFERENCES_ACCESS_TOKEN_PROFILE, "")
                , mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0), id_after_user_select);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<UsersListData>() {
            @Override
            public void onResponse(Call<UsersListData> call, Response<UsersListData> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                UsersListData posts = response.body();
                users.addAll(posts.getUsers());
                mAdapter.notifyDataSetChanged();

                if(posts.getUsers().size() > 0){
                    id_after_user_select = posts.getUsers().get(posts.getUsers().size()-1).getIdUserSelect();
                }

                if(posts.isNextPage()){
                    nextPage = true;
                }
                else{
                    nextPage = false;
                    loadBlock.setVisibility(View.GONE);
                }

                loadData = true;
            }
            @Override
            public void onFailure(Call<UsersListData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteItem(Integer id_user){
        ArrayList<UserShort> userShorts = new ArrayList<>();
        for(UserShort userShort : users){
            if(userShort.getId() != id_user){
                userShorts.add(userShort);
            }
        }
        users.clear();
        users.addAll(userShorts);
        mAdapter.notifyDataSetChanged();
    }

    public void makeTheHead(UserShort user){
        Toast.makeText(this, "Староста назначен", Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
        memoryOperation.setGroupOfUserIdOlderProfile(user.getId());
        memoryOperation.setGroupOfUserNameOlderProfile(user.getLastName() + " " + user.getFirstName());
        memoryOperation.setGroupOfUserPhotoOlderProfile(user.getPhoto());
        /*for(UserShort us : users){
            if(us.getId() == user.getId()){
                us.setNameRank("Староста");
            }
            else if(us.getId() == mSettings.getInt(APP_PREFERENCES_USER_ID_PROFILE, 0)){
                us.setNameRank("Студент");
            }
        }*/
    }
}
