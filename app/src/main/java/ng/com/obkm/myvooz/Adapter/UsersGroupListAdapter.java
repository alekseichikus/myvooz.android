package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.UserFuncDialogFragment;
import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.UsersListActivity;
import ng.com.obkm.myvooz.utils.MemoryOperation;
import ng.com.obkm.myvooz.utils.Constants;


public class UsersGroupListAdapter extends RecyclerView.Adapter<UsersGroupListAdapter.CustomViewHolder> {

    public Context context;
    public ArrayList<UserShort> employeeArrayList;
    SharedPreferences mSettings;
    UsersListActivity act;
    MemoryOperation memoryOperation;


    public UsersGroupListAdapter(Context context, ArrayList<UserShort> employeeArrayList, UsersListActivity act) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
        this.act = act;

        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        memoryOperation = new MemoryOperation(context);
    }

    @NonNull
    @Override
    public UsersGroupListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersGroupListAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.user_row, parent, false));
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView fio;
        TextView date;
        TextView rank;
        CardView funcButton;
        public CustomViewHolder(View view) {
            super(view);
            photo = view.findViewById(R.id.photo);
            fio = (TextView) view.findViewById(R.id.fio);
            date = (TextView) view.findViewById(R.id.date);
            rank = (TextView) view.findViewById(R.id.rank_name);
            funcButton = view.findViewById(R.id.func_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UsersGroupListAdapter.CustomViewHolder holder, int position) {

        if(memoryOperation.getIdUserProfile() == memoryOperation.getGroupOfUserIdOlderProfile()){
            holder.funcButton.setVisibility(View.VISIBLE);
        }
        else{
            holder.funcButton.setVisibility(View.GONE);
        }

        if(employeeArrayList.get(position).getId() == mSettings.getInt(Constants.APP_PREFERENCES_USER_ID_PROFILE, 0)){
            holder.funcButton.setVisibility(View.GONE);
        }
        else{
            holder.funcButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserFuncDialogFragment fr =
                            new UserFuncDialogFragment(employeeArrayList.get(position), act, position);
                    fr.show(act.getSupportFragmentManager(),
                            "user_list_fragment");
                }
            });
        }

        if(!employeeArrayList.get(position).getPhoto().equals("")){
            Picasso.get().load(employeeArrayList.get(position).getPhoto()).into(holder.photo);
        }

        holder.fio.setText(employeeArrayList.get(position).getLastName()+ " " + employeeArrayList.get(position).getFirstName());
        holder.date.setText(employeeArrayList.get(position).getDate());
        //holder.rank.setText(employeeArrayList.get(position).getNameRank());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size();
    }
}