package com.learnakantwi.twiguides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class DaysOfWeekAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<DaysOfWeek> originalArray , tempArray;
    //CustomFilter cf;

    public DaysOfWeekAdapter(Context context, ArrayList<DaysOfWeek> originalArray) {
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }


    public void update(ArrayList<DaysOfWeek> results){
        originalArray = new ArrayList<>();
        originalArray.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return originalArray.size();
    }

    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.custom_daysofweek, null);

        TextView textView = (TextView) convertView.findViewById(R.id.tvDaysOfWkEnglish);
        TextView textView1 = convertView.findViewById(R.id.tvDaysOfWkTwi);

        textView.setText(originalArray.get(position).getNameEnglish());
        textView1.setText(originalArray.get(position).getNameTwi());
        //if image would have been --- setImageResource.

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return null;
    }

   /* @Override
    public Filter getFilter() {
        if (cf==null){
            cf = new CustomFilter();
        }

        return cf;
    }

    class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length()>0  ) {
                constraint = constraint.toString().toUpperCase();

                ArrayList<DaysOfWeek> filters = new ArrayList<>();

                for (int i = 0; i < tempArray.size(); i++) {


                    if (tempArray.get(i).getNameEnglish().toUpperCase().contains(constraint)) {
                        DaysOfWeek daysOfWeekFilter = new DaysOfWeek(tempArray.get(i).getNameEnglish(), tempArray.get(i).getNameTwi());
                        filters.add(daysOfWeekFilter);
                    }
                }
                filterResults.count= filters.size();
                filterResults.values = filters;
            }
            else {
                filterResults.count= tempArray.size();
                filterResults.values=tempArray;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            originalArray = (ArrayList<DaysOfWeek> )results.values;
            notifyDataSetChanged();
        }
    }*/
}
