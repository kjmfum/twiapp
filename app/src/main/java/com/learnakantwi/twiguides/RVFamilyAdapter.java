package com.learnakantwi.twiguides;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVFamilyAdapter extends RecyclerView.Adapter<RVFamilyAdapter.MyViewHolder> implements Filterable {


    Context context;
    LayoutInflater inflater;
    onClickRecycle onClickRecycle;
    private ArrayList<Family> originalArray, tempArray;
    private Filter RecycleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Family> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                //filteredList = originalArray;
                filteredList.addAll(tempArray);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                //ArrayList<Animals> results = new ArrayList<>();
                for (Family x : tempArray) {

                    if (x.getTwiFamily().toLowerCase().contains(filterPattern) || x.englishFamily.toLowerCase().contains(filterPattern)) {
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


     RVFamilyAdapter(Context context, ArrayList<Family> originalArray, onClickRecycle onClickRecycle) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.originalArray = originalArray;
        tempArray = new ArrayList<>(originalArray);
        //tempArray = new ArrayList<>(originalArray);
        this.onClickRecycle = onClickRecycle;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View view = inflater.inflate(R.layout.rvlayout_home, parent, false);
        View view = inflater.inflate(R.layout.custom_time, parent, false);
        return new MyViewHolder(view, onClickRecycle);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      /*  holder.textView1.setText(childrenAnimalsArrayList.get(position).englishAnimals);
        holder.textView2.setText(childrenAnimalsArrayList.get(position).twiAnimals);
        holder.imageView.setImageResource(childrenAnimalsArrayList.get(position).drawableID);*/

        holder.textView1.setText(originalArray.get(position).getEnglishFamily());
        holder.textView2.setText(originalArray.get(position).getTwiFamily());
        //holder.imageView.setImageResource(originalArray.get(position).getImageOfActivity());

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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView2;
        TextView textView1;
       // ImageView imageView;
        onClickRecycle onClickRecycle;

        public MyViewHolder(@NonNull View itemView, onClickRecycle onClickRecycle) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.speakEnglishTime);
           textView2 = itemView.findViewById(R.id.speakTwiTime);
            //imageView = itemView.findViewById(R.id.imageViewRecycle1);
            this.onClickRecycle = onClickRecycle;


            itemView.setOnClickListener(this);

           // itemView.setOnLongClickListener();

        }

        @Override
        public void onClick(View v) {
            Animation shake = AnimationUtils.loadAnimation(context, R.anim.children_animation);
            textView1.setTextColor(Color.GREEN);
           // textView2.setTextColor(Color.RED);
           // imageView.startAnimation(shake);
           onClickRecycle.onMyItemClick(getAdapterPosition(), v);


        }
    }
}