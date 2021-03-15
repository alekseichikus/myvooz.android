package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.UserVeryShort;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<UserVeryShort> items;
    public UsersListAdapter(Context context, ArrayList<UserVeryShort> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Picasso.get().load(items.get(position).getPhoto()).into(holder.itemImage);
        if(position==0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            holder.contentLayout.setLayoutParams(params);
        }
        else{
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(-16, 0, 0, 0);
            holder.contentLayout.setLayoutParams(params);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private LinearLayout contentLayout;
        public CustomViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            contentLayout = view.findViewById(R.id.content_layout);
        }
    }
}