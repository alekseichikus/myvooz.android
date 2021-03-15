package ng.com.obkm.myvooz.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.com.obkm.myvooz.profile.AddNotification.Camera.ImageDialogActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.ImageList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.CustomViewHolder> {
    private AppCompatActivity context;
    private ArrayList<ImageList> items;
    private Integer type;

    public ImageListAdapter(AppCompatActivity context, ArrayList<ImageList> items, Integer type) {
        this.context = context;
        this.items = items;
        this.type = type;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_row, parent, false);
        return new CustomViewHolder(view);
    }

    public ArrayList<ImageList> getItems(){
        return items;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Picasso.get().load(items.get(position).getImage()).into(holder.image);
        if(type==1){
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    items.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        else{
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.imageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageDialogActivity.class);
                intent.putExtra("id", "10");
                intent.putExtra("path", items.get(position).getImage());
                intent.putExtra("full_path", items.get(position).getFullPath());
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView itemTitle;
        private CardView deleteButton;
        private CardView imageContainer;
        public CustomViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            deleteButton = view.findViewById(R.id.delete_button);
            imageContainer = view.findViewById(R.id.image_container);
        }
    }
}