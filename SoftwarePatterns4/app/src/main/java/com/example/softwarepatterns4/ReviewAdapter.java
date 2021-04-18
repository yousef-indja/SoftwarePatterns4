package com.example.softwarepatterns4;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private List<Review> reviewList;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public MyViewHolder(View itemView){
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.recyclerTextView);
        }
    }


    public ReviewAdapter( List<Review> reviewList){
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {
        final Review reviews = reviewList.get(position);
        holder.textView.setText(reviews.toString());


    }

    @Override
    public int getItemCount() {
        if(reviewList ==  null){
            return 0;
        } else {
            return reviewList.size();
        }

    }

    public void remove(int position){
        reviewList.remove(position);
        notifyItemRemoved(position);
    }

}


