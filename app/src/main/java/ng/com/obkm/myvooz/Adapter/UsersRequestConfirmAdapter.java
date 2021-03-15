package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.UserShort;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.UserRequestConfirmFuncDialogFragment;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.presenter.IUserRequestConfirmPresenter;
import ng.com.obkm.myvooz.profile.UserRequestConfirm.view.IUserRequestConfirmView;


public class UsersRequestConfirmAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<UserShort> employeeArrayList;
    public ArrayList<UserShort> orig;
    IUserRequestConfirmView userRequestConfirmView;
    IUserRequestConfirmPresenter userRequestConfirmPresenter;
    AppCompatActivity activity;

    public UsersRequestConfirmAdapter(Context context, ArrayList<UserShort> employeeArrayList, IUserRequestConfirmView userRequestConfirmView, AppCompatActivity activity, IUserRequestConfirmPresenter userRequestConfirmPresenter) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
        this.userRequestConfirmView = userRequestConfirmView;
        this.activity = activity;
        this.userRequestConfirmPresenter = userRequestConfirmPresenter;
    }


    public class EmployeeHolder
    {
        ImageView photo;
        TextView fio;
        TextView date;
        CardView funcButton;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<UserShort> results = new ArrayList<UserShort>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final UserShort g : orig) {
                            if (g.getLastName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                employeeArrayList = (ArrayList<UserShort>) results.values;
                notifyDataSetChanged();
            }
        };
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
            convertView= LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
            holder=new EmployeeHolder();
            holder.photo = convertView.findViewById(R.id.photo);
            holder.fio = (TextView) convertView.findViewById(R.id.fio);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.funcButton = convertView.findViewById(R.id.func_button);
            convertView.setTag(holder);

        }
        else
        {
            holder=(EmployeeHolder) convertView.getTag();
        }
        Picasso.get().load(employeeArrayList.get(position).getPhoto()).into(holder.photo);
        holder.fio.setText(employeeArrayList.get(position).getLastName()+ " " + employeeArrayList.get(position).getFirstName());
        holder.date.setText(employeeArrayList.get(position).getDate());

        holder.funcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRequestConfirmFuncDialogFragment fr =
                        new UserRequestConfirmFuncDialogFragment(employeeArrayList.get(position).getId(), userRequestConfirmPresenter, employeeArrayList, position);
                fr.show(activity.getSupportFragmentManager(),
                        "user_list_fragment");
            }
        });

        return convertView;

    }
}