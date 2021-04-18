package com.example.softwarepatterns4;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener{
    private List<Stock> listStock;

    public static final String KEY1 = "ID";
    private Stock stock;

    @Override
    public void onClick(View v) {


    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public MyViewHolder(View itemView){
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.recyclerTextView);
        }
    }


    public MyAdapter( List<Stock> listStock){
        this.listStock = listStock;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        stock = listStock.get(position);
        String top = "<b><h3>" + stock.getTitle() + "&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&euro;" + stock.getPrice() +"</h3></b>";
        String main = stock.toStringForRV().replace("\n", "<br>");
        String total = top + main;
        holder.textView.setText(Html.fromHtml(total));
        holder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Stock currentStock = listStock.get(position);
                Intent i = new Intent(v.getContext(), DetailedItem.class);
                i.putExtra(KEY1, String.valueOf(currentStock.getId()));
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listStock ==  null){
            return 0;
        } else {
            return listStock.size();
        }

    }

    public void remove(int position){
        listStock.remove(position);
        notifyItemRemoved(position);
    }







}
