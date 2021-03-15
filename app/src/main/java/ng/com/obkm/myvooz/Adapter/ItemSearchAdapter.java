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

import ng.com.obkm.myvooz.model.ItemSearch;
import ng.com.obkm.myvooz.R;


public class ItemSearchAdapter extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<ItemSearch> employeeArrayList;
    public ArrayList<ItemSearch> orig;

    public ItemSearchAdapter(Context context, ArrayList<ItemSearch> employeeArrayList) {
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
                final ArrayList<ItemSearch> results = new ArrayList<ItemSearch>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ItemSearch g : orig) {
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
                employeeArrayList = (ArrayList<ItemSearch>) results.values;
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
            holder.age=(TextView) convertView.findViewById(R.id.txtAge);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EmployeeHolder) convertView.getTag();
        }

        holder.name.setText(employeeArrayList.get(position).getName());
        if(employeeArrayList.get(position).getType() == 0){
            holder.age.setText("Группа");
        }
        else if(employeeArrayList.get(position).getType() == 1){
            holder.age.setText("Преподаватель");
        }
        else if(employeeArrayList.get(position).getType() == 2){
            holder.age.setText("Учебное заведение");
        }
        else if(employeeArrayList.get(position).getType() == 4){
            holder.age.setText("Неделя");
        }
        else if(employeeArrayList.get(position).getType() == 5){
            holder.age.setText("Корпус");
        }
        else if(employeeArrayList.get(position).getType() == 6){
            holder.age.setText("Предмет");
        }
        //holder.age.setText(String.valueOf(employeeArrayList.get(position).getId()));

        return convertView;

    }

}