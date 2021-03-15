package ng.com.obkm.myvooz.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.com.obkm.myvooz.profile.AddNotification.Camera.ImageDialogActivity;
import ng.com.obkm.myvooz.home.notification.model.Notification;
import ng.com.obkm.myvooz.R;

import static android.text.Html.FROM_HTML_MODE_LEGACY;


public class UserNotificationListAdapter extends BaseAdapter {

    public AppCompatActivity context;
    public ArrayList<Notification> employeeArrayList;
    public ArrayList<Notification> orig;

    public UserNotificationListAdapter(AppCompatActivity context, ArrayList<Notification> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }


    public class EmployeeHolder
    {
        ImageView photo;
        TextView title;
        TextView text;
        TextView date;
        CardView button;
        CardView importantMark;
        ImageView imageNotif;
        CardView imageNotifBlock;
        TextView markMe;
        RecyclerView photos;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return employeeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployeeHolder holder;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.user_notif_1, parent, false);
            holder=new EmployeeHolder();
            holder.photo = convertView.findViewById(R.id.photo);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.button = convertView.findViewById(R.id.button);
            holder.imageNotif = convertView.findViewById(R.id.image_notif);
            holder.photos = convertView.findViewById(R.id.photos);
            holder.importantMark = convertView.findViewById(R.id.important_mark);
            holder.imageNotifBlock = convertView.findViewById(R.id.image_notif_block);
            holder.markMe = convertView.findViewById(R.id.mark_me);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EmployeeHolder) convertView.getTag();
        }
        if(!employeeArrayList.get(position).getPhoto().equals("")){
            Picasso.get().load(employeeArrayList.get(position).getPhoto()).into(holder.photo);
            holder.photo.setVisibility(View.VISIBLE);
        }
        else{
            holder.photo.setVisibility(View.GONE);
        }



        if(employeeArrayList.get(position).getPhotos().size() > 0){
            if(employeeArrayList.get(position).getPhotos().size() == 1){
                if(!employeeArrayList.get(position).getPhotos().get(0).getImage().equals("")){
                    if(employeeArrayList.get(position).getType() == 2){
                        Picasso.get().load(employeeArrayList.get(position).getPhotos().get(0).getImage()).into(holder.imageNotif);
                        holder.imageNotifBlock.setVisibility(View.VISIBLE);
                        holder.photos.setVisibility(View.GONE);
                        holder.imageNotifBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ImageDialogActivity.class);
                                intent.putExtra("id", "10");
                                intent.putExtra("path", employeeArrayList.get(position).getPhotos().get(0).getImage());
                                intent.putExtra("full_path", employeeArrayList.get(position).getPhotos().get(0).getFullPath());
                                context.startActivity(intent);
                                context.overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                            }
                        });
                    }
                    else if(employeeArrayList.get(position).getType() == 3){
                        holder.photos.setLayoutManager(new LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false));
                        ImageListAdapter adapter = new ImageListAdapter(context, employeeArrayList.get(position).getPhotos(), 0);
                        holder.photos.setAdapter(adapter);
                        holder.photos.setVisibility(View.VISIBLE);
                        holder.imageNotifBlock.setVisibility(View.GONE);
                    }
                }
            }
            else{
                holder.photos.setLayoutManager(new LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false));
                ImageListAdapter adapter = new ImageListAdapter(context, employeeArrayList.get(position).getPhotos(), 0);
                holder.photos.setAdapter(adapter);
                holder.photos.setVisibility(View.VISIBLE);
                holder.imageNotifBlock.setVisibility(View.GONE);
            }
        }
        else{
            holder.imageNotifBlock.setVisibility(View.GONE);
            holder.photos.setVisibility(View.GONE);
        }

        if(employeeArrayList.get(position).isSentToMe()){
            holder.markMe.setVisibility(View.VISIBLE);
        }
        else{
            holder.markMe.setVisibility(View.GONE);
        }

        if(employeeArrayList.get(position).getImportantState().equals(0)){
            holder.importantMark.setVisibility(View.GONE);
        }
        else{
            holder.importantMark.setVisibility(View.VISIBLE);
        }

        holder.title.setText(employeeArrayList.get(position).getTitle());
        holder.date.setText(employeeArrayList.get(position).getDate());
        @SuppressLint("WrongConstant") Spanned result = HtmlCompat.fromHtml(employeeArrayList.get(position).getText(), FROM_HTML_MODE_LEGACY);
        holder.text.setText(result);
        if(!employeeArrayList.get(position).getLink().equals("")){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(employeeArrayList.get(position).getLink()));
                    context.startActivity(browserIntent);
                }
            });
        }
        return convertView;
    }

}