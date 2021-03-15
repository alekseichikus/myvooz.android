package ng.com.obkm.myvooz.note.AddNote.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import ng.com.obkm.myvooz.Json.JsonPlaceHolderApi29;
import ng.com.obkm.myvooz.model.Note;
import ng.com.obkm.myvooz.utils.Date.DateWithTimeZone;
import ng.com.obkm.myvooz.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNoteModel implements IAddNoteModel {

    @Override
    public void addNote(OnFinishedListener onFinishedListener, String access_token, Integer id_user
            , Integer id_object, String date, String title, String text, ArrayList<Integer> images, Integer mark_me) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SITE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi29 jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi29.class);

        Call<Note> call = jsonPlaceHolderApi.getMyJSON(access_token, id_user, id_object, title, text, date, mark_me, images);
        Log.d("request", call.request().url().toString());
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Note _note = response.body();
                Calendar calendar = null;
                try {
                    calendar = DateWithTimeZone.stringToCalendar(_note.getDate(), TimeZone.getTimeZone("Europe/Moscow"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                Note note = new Note(_note.getNameObject(), _note.getName(), _note.getText(), _note.isMarkMe(), _note.getPhotos(), _note.getId()
                        , dateFormat.format(calendar.getTime()), _note.isStateCompleted(), _note.getIdUser(), _note.getIdObject());
                onFinishedListener.onFinished(note);
            }
            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                t.printStackTrace();
                onFinishedListener.onFailure(t);
            }
        });
    }
    @Override
    public void uploadFile(String filepath) {

    }


}

