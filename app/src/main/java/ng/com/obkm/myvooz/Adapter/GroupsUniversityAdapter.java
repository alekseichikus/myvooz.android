package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import ng.com.obkm.myvooz.model.GroupUniversity;
import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.search.WeekSchedule.WeekScheduleActivity;

public class GroupsUniversityAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<GroupUniversity> employeeArrayList;
    public ArrayList<GroupUniversity> orig;

    public GroupsUniversityAdapter(Context context, ArrayList<GroupUniversity> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }


    public class EmployeeHolder
    {
        TextView name;
        TextView age;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<GroupUniversity> results = new ArrayList<GroupUniversity>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final GroupUniversity g : orig) {
                            if (g.getName().toLowerCase()
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
                employeeArrayList = (ArrayList<GroupUniversity>) results.values;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.group_row, parent, false);
            holder=new EmployeeHolder();
            holder.name=(TextView) convertView.findViewById(R.id.txtName);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EmployeeHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WeekScheduleActivity.class);
                context.startActivity(intent);
            }
        });
        holder.name.setText(employeeArrayList.get(position).getName());
        //holder.age.setText(String.valueOf(employeeArrayList.get(position).getId()));

        return convertView;

    }

}