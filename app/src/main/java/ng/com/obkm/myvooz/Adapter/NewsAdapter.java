package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Calendar;
import ng.com.obkm.myvooz.MainActivity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.Story;
import ng.com.obkm.myvooz.model.News;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.OnStoryChangedCallback;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<News> items;
    MainActivity mainActivity;
    public NewsAdapter(Context context, ArrayList<News> items, MainActivity mainActivity) {
        this.context = context;
        this.items = items;
        this.mainActivity = mainActivity;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.itemTitle.setText(items.get(position).getTitle());
        holder.itemUnionName.setText(items.get(position).getUnionName());
        Picasso.get().load(items.get(position).getImage()).into(holder.itemImage);
        Picasso.get().load(items.get(position).getUnionImage()).into(holder.itemUnionImage);
        holder.itemNewsBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStories(position);
            }
        });

    }

    public void showStories(Integer position) {

        final ArrayList<MyStory> myStories = new ArrayList<>();

        for(Story story: items.get(position).getStories()){
            myStories.add(new MyStory(
                    story.getImage(),
                    Calendar.getInstance().getTime()
            ));
        }
        new StoryView.Builder(mainActivity.getSupportFragmentManager())
                .setStoriesList(myStories)
                .setStoryDuration(5000)
                .setTitleText(items.get(position).getUnionName())
                .setTitleLogoUrl(items.get(position).getUnionImage())
                .setSubtitleText("Медиацентр")
                .setStoryClickListeners(new StoryClickListeners() {
                    @Override
                    public void onDescriptionClickListener(int position) {
                        Intent browserIntent = new
                                Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getLink()));
                        context.startActivity(browserIntent);
                    }

                    @Override
                    public void onTitleIconClickListener(int position) {
                    }
                })
                .setOnStoryChangedCallback(new OnStoryChangedCallback() {
                    @Override
                    public void storyChanged(int position) {
                        //Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                    }
                })
                .setStartingIndex(0)
                .build()
                .show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;
        private ImageView itemUnionImage;
        private TextView itemUnionName;
        private CardView itemNewsBlock;
        public CustomViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.item_image);
            itemTitle = view.findViewById(R.id.item_title);
            itemUnionImage = view.findViewById(R.id.item_union_image);
            itemUnionName = view.findViewById(R.id.item_union_name);
            itemNewsBlock = view.findViewById(R.id.item_news);
        }
    }
}