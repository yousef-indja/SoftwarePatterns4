package com.example.softwarepatterns4;


import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;



public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> implements View.OnClickListener{
    private List<Stock> listStock;

    private Stock stock;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    public DatabaseReference dr;
    public DatabaseReference dr2;
    public DatabaseReference dr3;

    public static final String KEY1 = "TOTAL";

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


    public CartAdapter( List<Stock> listStock){
        this.listStock = listStock;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false);
        itemView.setOnClickListener(mOnClickListener);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        stock = listStock.get(position);
        String top = "<b><h3>" + stock.getTitle() + "&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&euro;" + stock.getPrice() +"</h3></b>";
        String main = stock.toStringForCartRV().replace("\n", "<br>");
        String total = top + main;
        holder.textView.setText(Html.fromHtml(total));
        holder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Stock currentStock = listStock.get(position);
                dr= FirebaseDatabase.getInstance().getReference("Stock").child(String.valueOf(currentStock.getId()));
                dr3 = FirebaseDatabase.getInstance().getReference("Stock").child(String.valueOf(currentStock.getId())).child("stock");
                final int[] newStock = new int[1];
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Stock s = dataSnapshot.getValue(Stock.class);
                        newStock[0] = s.getStock();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dr2= FirebaseDatabase.getInstance().getReference("Customer").child(mUser.getUid()).child("Cart").child(String.valueOf(currentStock.getId()));
                dr2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Stock s = dataSnapshot.getValue(Stock.class);
                        newStock[0] = newStock[0] + s.getQuantity();
                        dr3.setValue(newStock[0]);
                        dr2.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                remove(position);

                Toast.makeText(v.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();

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

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {




        }
    };

}

