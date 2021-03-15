package ng.com.obkm.myvooz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ng.com.obkm.myvooz.Adapter.ItemSearchAdapter;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi22;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi28;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi5;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi7;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.ItemSearch;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.search.WeekSchedule.WeekScheduleActivity;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    ItemSearchAdapter mAdapter;
    ListView mListView;
    ArrayList<ItemSearch> groups = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText searchItem;
    TextView textInfo;
    ImageView removeButton;
    LinearLayout emptyEditTextView;
    LinearLayout emptyListView;
    String typeItem;
    private IActivityPresenter activityPresenter;
    private MemoryOperation memoryOperation;

    public SearchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        memoryOperation = new MemoryOperation(this);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        searchItem = findViewById(R.id.search_item);
        removeButton = findViewById(R.id.remove_button);
        emptyEditTextView = findViewById(R.id.empty_edittext_view);
        textInfo = findViewById(R.id.text_info);
        emptyListView = findViewById(R.id.empty_listview);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        mSwipeRefreshLayout.setEnabled(false);

        Bundle arguments = getIntent().getExtras();
        typeItem = arguments.get("type").toString();

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(this);

        mListView = (ListView) findViewById(R.id.list);


        mAdapter = new ItemSearchAdapter(SearchActivity.this, groups);
        mListView.setAdapter(mAdapter);

        CardView returnButton = findViewById(R.id.back_button);

        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groups.clear();
                mAdapter.notifyDataSetChanged();
                searchItem.setText("");
                emptyEditTextView.setVisibility(View.VISIBLE);
                removeButton.setVisibility(View.GONE);
            }
        });

        searchItem.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(s.toString().length() == 0){
                    emptyListView.setVisibility(View.GONE);
                    removeButton.setVisibility(View.GONE);
                }
                else{
                    removeButton.setVisibility(View.VISIBLE);
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            Handler handler = new Handler();
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(showInfo);
                handler.postDelayed(showInfo, 500);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
                SharedPreferences.Editor editor = mSettings.edit();
                if(typeItem.equals("weekFilter")){
                    editor.putInt(WeekScheduleActivity.APP_PREFERENCES_WEEK_FILTER, mAdapter.employeeArrayList.get(i).getId());
                    onBackPressed();
                }
                else if(typeItem.equals("universityCheck")){
                    editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_PROFILE,  mAdapter.employeeArrayList.get(i).getFullName());
                    editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_PROFILE, mAdapter.employeeArrayList.get(i).getId());
                    onBackPressed();
                }
                else if(typeItem.equals("groupProfile")){
                    editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_PROFILE,  mAdapter.employeeArrayList.get(i).getName());
                    editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_PROFILE, mAdapter.employeeArrayList.get(i).getId());
                    onBackPressed();
                }
                else if(typeItem.equals("corpus_intermediate_value")){
                    editor.putString(Constants.APP_PREFERENCES_CORPUS_NAME_INTERMEDIATE_VALUE,  mAdapter.employeeArrayList.get(i).getName());
                    editor.putInt(Constants.APP_PREFERENCES_CORPUS_ID_INTERMEDIATE_VALUE, mAdapter.employeeArrayList.get(i).getId());
                    onBackPressed();
                }
                else if(typeItem.equals("university_intermediate_value") || typeItem.equals("university_confirm")){
                    editor.putString(Constants.APP_PREFERENCES_UNIVERSITY_NAME_INTERMEDIATE_VALUE,  mAdapter.employeeArrayList.get(i).getFullName());
                    editor.putInt(Constants.APP_PREFERENCES_UNIVERSITY_ID_INTERMEDIATE_VALUE, mAdapter.employeeArrayList.get(i).getId());
                    onBackPressed();
                }
                else if(typeItem.equals("group_intermediate_value") || typeItem.equals("group_confirm")){
                    editor.putString(Constants.APP_PREFERENCES_GROUP_NAME_INTERMEDIATE_VALUE,  mAdapter.employeeArrayList.get(i).getName());
                    editor.putInt(Constants.APP_PREFERENCES_GROUP_ID_INTERMEDIATE_VALUE, mAdapter.employeeArrayList.get(i).getId());
                    onBackPressed();
                }
                else if(typeItem.equals("object_intermediate_value")){
                    editor.putString(Constants.APP_PREFERENCES_OBJECT_NAME_INTERMEDIATE_VALUE,  mAdapter.employeeArrayList.get(i).getName());
                    editor.putInt(Constants.APP_PREFERENCES_OBJECT_ID_INTERMEDIATE_VALUE, mAdapter.employeeArrayList.get(i).getId());
                    onBackPressed();
                }
                else if(typeItem.equals("all_type")){
                    Intent intent = new Intent(SearchActivity.this, WeekScheduleActivity.class);
                    intent.putExtra("id_item", mAdapter.employeeArrayList.get(i).getId());
                    intent.putExtra("name_item", mAdapter.employeeArrayList.get(i).getName());
                    intent.putExtra("type_item", mAdapter.employeeArrayList.get(i).getType());
                    startActivity(intent);
                }
                editor.commit();
            }
        });
        if(typeItem.equals("weekFilter")){
            textInfo.setText("Введите номер нужной недели");
        }
        else if(typeItem.equals("universityCheck") || typeItem.equals("university_confirm") || typeItem.equals("university_intermediate_value")){
            textInfo.setText("Введите название нужного учебного заведения. Например, УГАТУ.");
        }
        else if(typeItem.equals("groupProfile") || typeItem.equals("group_confirm") || typeItem.equals("group_intermediate_value")){
            textInfo.setText("Введите название нужной группы. Например, ИБ-316.");
        }
        else if(typeItem.equals("corpus_intermediate_value")){
            textInfo.setText("Введите название нужного корпуса. Например, 8Гк.");
        }
        else if(typeItem.equals("object_intermediate_value")){
            textInfo.setText("Введите название предмета. Например, информационные технологии.");
        }
    }

    Runnable showInfo = new Runnable() {
        public void run() {
            if (!searchItem.getText().toString().isEmpty()) {
                emptyEditTextView.setVisibility(View.GONE);
                getData(typeItem, searchItem.getText().toString());
            }
            else{
                emptyEditTextView.setVisibility(View.VISIBLE);
                groups.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public void getData(String typeItem, String text){
        if(typeItem.equals("universityCheck") || typeItem.equals("university_confirm") || typeItem.equals("university_intermediate_value")){
            groups.clear();
            mSwipeRefreshLayout.setRefreshing(true);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SITE_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi7 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi7.class);
            Call<ArrayList<ItemSearch>> call = jsonPlaceHolderApi.getMyJSON(text);
            Log.d("request", call.request().url().toString());
            call.enqueue(new Callback<ArrayList<ItemSearch>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemSearch>> call, Response<ArrayList<ItemSearch>> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    ArrayList<ItemSearch> posts = response.body();
                    for (ItemSearch post : posts) {
                        groups.add(new ItemSearch(post.getFullName(), post.getId(), post.getType(), post.getName()));
                    }
                    if(posts.size()==0){
                        emptyListView.setVisibility(View.VISIBLE);

                    }
                    else{
                        emptyListView.setVisibility(View.GONE);
                    }
                    emptyEditTextView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<ArrayList<ItemSearch>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else if(typeItem.equals("groupProfile") || typeItem.equals("group_confirm") || typeItem.equals("group_intermediate_value")){
            groups.clear();
            Bundle arguments = getIntent().getExtras();
            Integer idUniversity = Integer.valueOf(arguments.get("id_university").toString());

            mSwipeRefreshLayout.setRefreshing(true);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SITE_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi5 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi5.class);
            Call<ArrayList<ItemSearch>> call = jsonPlaceHolderApi.getMyJSON(text, idUniversity);
            Log.d("regeferfer", call.request().url().toString());
            call.enqueue(new Callback<ArrayList<ItemSearch>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemSearch>> call, Response<ArrayList<ItemSearch>> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    ArrayList<ItemSearch> posts = response.body();
                    for (ItemSearch post : posts) {
                        groups.add(new ItemSearch(post.getName(), post.getId(), post.getType(), ""));
                    }
                    if(posts.size()==0){
                        emptyListView.setVisibility(View.VISIBLE);

                    }
                    else{
                        emptyListView.setVisibility(View.GONE);
                    }
                    emptyEditTextView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<ArrayList<ItemSearch>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else if(typeItem.equals("corpus_intermediate_value")){
            groups.clear();
            mSwipeRefreshLayout.setRefreshing(true);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SITE_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi22 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi22.class);
            Call<ArrayList<ItemSearch>> call = jsonPlaceHolderApi.getMyJSON(memoryOperation.getIdUniversityProfile(), text);
            Log.d("request", call.request().url().toString());
            call.enqueue(new Callback<ArrayList<ItemSearch>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemSearch>> call, Response<ArrayList<ItemSearch>> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    ArrayList<ItemSearch> posts = response.body();
                    for (ItemSearch post : posts) {
                        groups.add(new ItemSearch(post.getName(), post.getId(), post.getType(), ""));
                    }
                    if(posts.size()==0){
                        emptyListView.setVisibility(View.VISIBLE);

                    }
                    else{
                        emptyListView.setVisibility(View.GONE);
                    }
                    emptyEditTextView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<ArrayList<ItemSearch>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else if(typeItem.equals("object_intermediate_value")){
            groups.clear();
            mSwipeRefreshLayout.setRefreshing(true);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SITE_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi28 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi28.class);
            Call<ArrayList<ItemSearch>> call = jsonPlaceHolderApi.getMyJSON(text, memoryOperation.getIdUniversityProfile());
            Log.d("request", call.request().url().toString());
            call.enqueue(new Callback<ArrayList<ItemSearch>>() {
                @Override
                public void onResponse(Call<ArrayList<ItemSearch>> call, Response<ArrayList<ItemSearch>> response) {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    ArrayList<ItemSearch> posts = response.body();
                    for (ItemSearch post : posts) {
                        groups.add(new ItemSearch(post.getName(), post.getId(), post.getType(), ""));
                    }
                    if(posts.size()==0){
                        emptyListView.setVisibility(View.VISIBLE);

                    }
                    else{
                        emptyListView.setVisibility(View.GONE);
                    }
                    emptyEditTextView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<ArrayList<ItemSearch>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onRefresh() {

    }
}