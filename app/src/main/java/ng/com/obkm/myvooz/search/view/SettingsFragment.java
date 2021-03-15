package ng.com.obkm.myvooz.search.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;

import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ng.com.obkm.myvooz.Adapter.ItemSearchAdapter;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.ItemSearch;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.search.WeekSchedule.WeekScheduleActivity;
import ng.com.obkm.myvooz.search.presenter.ISearchPresenter;
import ng.com.obkm.myvooz.search.presenter.SearchPresenter;
import ng.com.obkm.myvooz.utils.MemoryOperation;

public class SettingsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ISearchView {
    Context mContext;
    ItemSearchAdapter mAdapter;
    ListView mListView;
    ArrayList<ItemSearch> groups = new ArrayList<>();
    EditText searchItem;
    RelativeLayout emptyEditTextView;
    LinearLayout noInternetConnectionLayout;
    ImageView removeButton;
    RelativeLayout emptyListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MainActivity mainActivity;
    View view;
    Boolean stateL = false;
    MemoryOperation memoryOperation;

    IActivityPresenter activityPresenter;
    ISearchPresenter searchPresenter;

    public SettingsFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        } else {
            if (stateL) {

            } else {
                activityPresenter = new ActivityPresenter(getContext());
                searchPresenter = new SearchPresenter(this, getContext());
                mAdapter = new ItemSearchAdapter(getContext(), groups);

                view = inflater.inflate(R.layout.fragment_settings, container, false);
                stateL = true;

                initUI(view);
                setListeners();
                showStartLayout();
            }
        }
        return view;
    }

    void initUI(View view){
        searchItem = view.findViewById(R.id.search_item);
        emptyEditTextView = view.findViewById(R.id.empty_edittext_view);
        emptyListView = view.findViewById(R.id.empty_listview);
        mListView = (ListView) view.findViewById(R.id.list);
        removeButton = view.findViewById(R.id.remove_button);
        noInternetConnectionLayout = view.findViewById(R.id.no_network_connection_layout);

        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.DKGRAY, Color.GRAY);
        mSwipeRefreshLayout.setEnabled(false);
    }

    void setListeners(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), WeekScheduleActivity.class);
                intent.putExtra("id_item", groups.get(i).getId());
                intent.putExtra("name_item", groups.get(i).getName());
                intent.putExtra("type_item", groups.get(i).getType());
                startActivity(intent);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groups.clear();
                mAdapter.notifyDataSetChanged();
                searchItem.setText("");
                removeButton.setVisibility(View.GONE);
                showStartLayout();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                    emptyEditTextView.setVisibility(View.GONE);
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            Handler handler = new Handler();
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(searchPresenter.getRunnable());
                handler.postDelayed(searchPresenter.getRunnable(), 500);
            }
        });
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ItemSearchAdapter(getContext(), groups);
        memoryOperation = new MemoryOperation(getContext());
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onResume() {
        super.onResume();
        activityPresenter.setLightStatusBar(getActivity());
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
    public void setItemsToRecyclerView(List<ItemSearch> items) {
        groups.clear();
        groups.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Произошла какая-то бяка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkEmpty() {
        if (!searchItem.getText().toString().isEmpty()) {
            searchPresenter.requestItemsFromServer(searchItem.getText().toString());
        }
        else{
            showStartLayout();
        }
    }

    @Override
    public void showStartLayout() {
        emptyEditTextView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        noInternetConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showListLayout() {
        emptyEditTextView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        emptyListView.setVisibility(View.GONE);
        noInternetConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLayout() {
        emptyEditTextView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);
        noInternetConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void setNoInternetConnectionLayout() {
        emptyEditTextView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.GONE);
        noInternetConnectionLayout.setVisibility(View.VISIBLE);
    }
}