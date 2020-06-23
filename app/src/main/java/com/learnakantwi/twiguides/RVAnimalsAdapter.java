package com.learnakantwi.twiguides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVAnimalsAdapter extends RecyclerView.Adapter<RVAnimalsAdapter.MyViewHolder> implements Filterable{



    Context context;
    ArrayList<Animals> originalArray , tempArray;
    LayoutInflater inflater;
    onClickRecycle onClickRecycle;
    private Filter RecycleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Animals> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                //filteredList = originalArray;
                filteredList.addAll(tempArray);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                //ArrayList<Animals> results = new ArrayList<>();
                for (Animals x : tempArray) {

                    if (x.getTwiAnimals().toLowerCase().contains(filterPattern) || x.getEnglishAnimals().toLowerCase().contains(filterPattern)) {
                        filteredList.add(x);
                    }
                }
            }
            FilterResults results1 = new FilterResults();
            results1.values = filteredList;

            return results1;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results1) {
            originalArray.clear();
            //originalArray = new ArrayList<>();
            originalArray.addAll((List) results1.values);

      /*   tempArray.clear();
         tempArray.addAll((List) results1.values);*/
            notifyDataSetChanged();
        }
    };
    //TimeAdapter.CustomFilter cf;

    public RVAnimalsAdapter(Context context, ArrayList<Animals> originalArray , onClickRecycle onClickRecycle) {
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = new ArrayList<>(originalArray);
        this.inflater = LayoutInflater.from(context);
        this.onClickRecycle = onClickRecycle;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_time_recycler , parent, false);
        return new MyViewHolder(view, onClickRecycle);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tvEnglish.setText(originalArray.get(position).getEnglishAnimals());
        holder.tvTwi.setText(originalArray.get(position).getTwiAnimals());
    }

    @Override
    public int getItemCount() {
        return originalArray.size();
    }

    @Override
    public Filter getFilter() {
        return RecycleFilter;
    }

    public interface onClickRecycle {
        void onMyItemClick(int position, View view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvEnglish;
        TextView tvTwi;
        onClickRecycle onClickRecycle;

        public MyViewHolder(@NonNull View itemView , onClickRecycle onClickRecycle) {
            super(itemView);

            tvEnglish = itemView.findViewById(R.id.textViewEnglish);
            tvTwi = itemView.findViewById(R.id.textViewTwi);
            this.onClickRecycle = onClickRecycle;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickRecycle.onMyItemClick(getAdapterPosition(), view);
        }
    }

}
