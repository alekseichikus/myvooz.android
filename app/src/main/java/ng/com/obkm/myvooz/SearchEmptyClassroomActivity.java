package ng.com.obkm.myvooz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

import me.bendik.simplerangeview.SimpleRangeView;
import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi23;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.Classroom;
import ng.com.obkm.myvooz.presenter.ActivityPresenter;
import ng.com.obkm.myvooz.presenter.IActivityPresenter;
import ng.com.obkm.myvooz.utils.Constants;
import ng.com.obkm.myvooz.view.DatePickerDialogFragment;
import ng.com.obkm.myvooz.view.presenter.IDatePickerPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchEmptyClassroomActivity extends AppCompatActivity implements IDatePickerPresenter {
    SharedPreferences mSettings;
    EditText editText;
    CardView sendButton;
    String name_corpus;
    Integer id_corpus = 0;
    TextView corpusName;
    TextView dateName;
    TextView classrooms;
    Calendar calendar;

    SimpleRangeView rangeView;
    RelativeLayout selectCorpusButton;
    ScrollView scrollView;
    private IActivityPresenter activityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_empty_classroom);

        mSettings = PreferenceManager.getDefaultSharedPreferences(SearchEmptyClassroomActivity.this);
        corpusName = findViewById(R.id.corpus_name);
        dateName = findViewById(R.id.date_name);
        classrooms = findViewById(R.id.classrooms);
        scrollView = findViewById(R.id.scroll);
        TextView pairName = findViewById(R.id.pair_name);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        String ii = Constants.getMonthName(calendar.get(Calendar.MONTH));
        dateName.setText(Constants.getDayOfWeekNameShort(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " "+ ii);

        rangeView = findViewById(R.id.fixed_rangeview);

        activityPresenter = new ActivityPresenter(this);
        activityPresenter.setLightStatusBar(this);

        rangeView.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView rangeView, int start) {
                pairName.setText("с " + String.valueOf(start+1) + " по " + String.valueOf(rangeView.getEnd()+1));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView rangeView, int end) {
                pairName.setText("с " + String.valueOf(rangeView.getStart()+1) + " по " + String.valueOf(end+1));
            }
        });

        rangeView.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView rangeView, int start, int end) {
                pairName.setText("с " + String.valueOf(start+1) + " по " + String.valueOf(end+1));
            }
        });

        selectCorpusButton = findViewById(R.id.corpus_select);
        selectCorpusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchEmptyClassroomActivity.this, SearchActivity.class);
                intent.putExtra("type", "corpus_intermediate_value");
                startActivity(intent);
                Animatoo.animateSlideLeft(SearchEmptyClassroomActivity.this);
            }
        });

        CardView sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(id_corpus != 0){
                    getData();
                }
                else{
                    Toast.makeText(SearchEmptyClassroomActivity.this, "Выберите корпус", Toast.LENGTH_SHORT).show();
                    final Animation animShake = AnimationUtils.loadAnimation(SearchEmptyClassroomActivity.this, R.anim.anim_shake);
                    selectCorpusButton.startAnimation(animShake);
                }
            }
        });

        RelativeLayout datePickerButton = findViewById(R.id.datepicker_button);

        datePickerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment bottomSheet = new DatePickerDialogFragment(calendar, SearchEmptyClassroomActivity.this);
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
            }
        });

        CardView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(Constants.APP_PREFERENCES_CORPUS_ID_INTERMEDIATE_VALUE, 0);
        editor.putString(Constants.APP_PREFERENCES_CORPUS_NAME_INTERMEDIATE_VALUE, "");
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = mSettings.edit();
        if(id_corpus != mSettings.getInt(Constants.APP_PREFERENCES_CORPUS_ID_INTERMEDIATE_VALUE, 0)){
            corpusName.setText(mSettings.getString(Constants.APP_PREFERENCES_CORPUS_NAME_INTERMEDIATE_VALUE, ""));
            id_corpus = mSettings.getInt(Constants.APP_PREFERENCES_CORPUS_ID_INTERMEDIATE_VALUE, 0);
        }
        else{

        }
        editor.commit();
    }

    public void getData(){
        //users.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        classrooms.setText("");
        JsonPlaceHolderApi23 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi23.class);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(SearchEmptyClassroomActivity.this);
        Call<ArrayList<ArrayList<Classroom>>> call = jsonPlaceHolderApi.getMyJSON(calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR), id_corpus, rangeView.getStart()+1, rangeView.getEnd()+1);
        call.enqueue(new Callback<ArrayList<ArrayList<Classroom>>>() {
            @Override
            public void onResponse(Call<ArrayList<ArrayList<Classroom>>> call, Response<ArrayList<ArrayList<Classroom>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                SharedPreferences.Editor editor = mSettings.edit();
                ArrayList<ArrayList<Classroom>> posts = response.body();

                Integer j = 0;
                for (ArrayList<Classroom> post : posts) {
                    classrooms.setText(classrooms.getText() + Integer.toString(post.get(0).getFloor()) + " этаж \n");
                    Integer i = 0;
                    for (Classroom postp : post) {
                        classrooms.setText(classrooms.getText() + Integer.toString(postp.getFloor()) + "-" + postp.getName());
                        if(post.size() == i+1){
                            classrooms.setText(classrooms.getText() + ".");
                        }
                        else{
                            classrooms.setText(classrooms.getText() + ", ");
                        }
                            i++;
                    }
                    if(posts.size() != j+1){
                        classrooms.setText(classrooms.getText() + "\n\n");
                    }
                    j++;
                }
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                },100);
            }
            @Override
            public void onFailure(Call<ArrayList<ArrayList<Classroom>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void changeDate(Calendar calendar) {
        this.calendar = calendar;
        String ii = Constants.getMonthName(calendar.get(Calendar.MONTH));
        dateName.setText(Constants.getDayOfWeekNameShort(calendar.get(Calendar.DAY_OF_WEEK)) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " "+ ii);
    }
}
