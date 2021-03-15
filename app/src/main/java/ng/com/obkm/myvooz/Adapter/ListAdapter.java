package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.home.notification.model.ListItem;
import ng.com.obkm.myvooz.presenter.IListPresenter;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    public Context context;
    public ArrayList<ListItem> employeeArrayList;
    private IListPresenter listPresenter;
    Integer select_position = 0;

    public ListAdapter(Context context, ArrayList<ListItem> employeeArrayList, IListPresenter listPresenter) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
        this.listPresenter = listPresenter;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.row_list, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.name.setText(employeeArrayList.get(position).getName());
        holder.count.setText(String.valueOf(employeeArrayList.get(position).getCount()));
        if(employeeArrayList.get(position).getState()){
            holder.line.setCardBackgroundColor(0xfff6f6f6);
            holder.name.setTextColor(0xff313233);
        }
        else{
            holder.line.setCardBackgroundColor(0x00ffffff);
            holder.name.setTextColor(0xff929ba5);
        }
        holder.back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!employeeArrayList.get(position).isLocked()){
                    if(!employeeArrayList.get(position).getState()){
                        updateButton(position);
                        listPresenter.requestData(employeeArrayList.get(position).getId());
                    }
                }
                else{
                    Toast.makeText(context, "Вступите в группу, чтобы опубликовывать коллективные заметки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(employeeArrayList.get(position).isLocked()){
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if(employeeArrayList.get(position).isStateCount()){
            holder.count.setVisibility(View.VISIBLE);
        }
        else{
            holder.count.setVisibility(View.GONE);
        }
    }

    public  void updateButton(Integer position){
        for(ListItem l: employeeArrayList){
            l.setState(false);
        }
        employeeArrayList.get(position).setState(true);
        select_position = position;
        notifyDataSetChanged();
    }

    public Integer getTypeSelectItem(){
        return select_position;
    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private LinearLayout back;
        private CardView line;
        private TextView count;
        public CustomViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.row_list_name);
            back = view.findViewById(R.id.row_list_back);
            line = view.findViewById(R.id.row_list_line);
            count = view.findViewById(R.id.row_list_count);
        }
    }
}