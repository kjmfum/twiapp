package com.learnakantwi.twiguides;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
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

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.MyViewHolder> implements Filterable {


    Context context;
    LayoutInflater inflater;
    onClickRecycle onClickRecycle;
    private ArrayList<Lessons> originalArray, tempArray;
    private Filter RecycleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Lessons> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                //filteredList = originalArray;
                filteredList.addAll(tempArray);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                //ArrayList<Animals> results = new ArrayList<>();
                for (Lessons x : tempArray) {

                    if (x.getTransliteration().toLowerCase().contains(filterPattern) || x.getTwi().toLowerCase().contains(filterPattern)) {
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


     LessonsAdapter(Context context, ArrayList<Lessons> originalArray, onClickRecycle onClickRecycle) {
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
        View view = inflater.inflate(R.layout.lessons_recycler_view, parent, false);
        return new MyViewHolder(view, onClickRecycle);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      /*  holder.textView1.setText(childrenAnimalsArrayList.get(position).englishAnimals);
        holder.textView2.setText(childrenAnimalsArrayList.get(position).twiAnimals);
        holder.imageView.setImageResource(childrenAnimalsArrayList.get(position).drawableID);*/

        holder.textView1.setText(originalArray.get(position).getTwi());
        holder.textView2.setText(originalArray.get(position).getTransliteration());

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
        ImageView imageView;
        onClickRecycle onClickRecycle;

        public MyViewHolder(@NonNull View itemView, onClickRecycle onClickRecycle) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textViewRecycle1);
            textView2 = itemView.findViewById(R.id.textViewRecycle2);
           // imageView = itemView.findViewById(R.id.imageViewRecycle1);
            this.onClickRecycle = onClickRecycle;


            itemView.setOnClickListener(this);

           // itemView.setOnLongClickListener();

        }

        @Override
        public void onClick(View v) {
           // Animation shake = AnimationUtils.loadAnimation(context, R.anim.children_animation);
           /* textView1.setTextColor(Color.GREEN);
            textView2.setTextColor(Color.RED);*/
          //  imageView.startAnimation(shake);





            textView1.setTextColor(Color.BLUE);
            textView2.setTextColor(Color.RED);
           // toast.setText(tvTwi.getText().toString());
            //toast.show();

            new Handler().postDelayed(() -> {
                textView1.setTextColor(Color.DKGRAY);
                textView2.setTextColor(Color.DKGRAY);
                //tvEnglish.setTextColor(Color.RED);

            },4000);





           onClickRecycle.onMyItemClick(getAdapterPosition(), v);


        }
    }
}