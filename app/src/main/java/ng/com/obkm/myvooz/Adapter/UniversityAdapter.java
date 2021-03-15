package ng.com.obkm.myvooz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import ng.com.obkm.myvooz.R;
import ng.com.obkm.myvooz.model.University;

public class UniversityAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<University> employeeArrayList;
    public ArrayList<University> orig;

    public UniversityAdapter(Context context, ArrayList<University> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }


    public class EmployeeHolder
    {
        TextView name;
        TextView fullname;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<University> results = new ArrayList<University>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final University g : orig) {
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
                employeeArrayList = (ArrayList<University>) results.values;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.university_row, parent, false);
            holder=new EmployeeHolder();
            holder.name=(TextView) convertView.findViewById(R.id.txtName);
            holder.fullname=(TextView) convertView.findViewById(R.id.university_fullname);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EmployeeHolder) convertView.getTag();
        }
        holder.name.setText(employeeArrayList.get(position).getName());
        holder.fullname.setText(employeeArrayList.get(position).getFullName());
        return convertView;

    }

}