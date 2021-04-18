package com.example.softwarepatterns4;

import android.content.Intent;
import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;



public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> implements View.OnClickListener{
    private List<Customer> listCustomer;

    private Customer customer;

    FirebaseAuth auth = FirebaseAuth.getInstance();;
    DatabaseReference dr;

    String customerUID;

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


    public CustomerAdapter( List<Customer> listCustomer){
        this.listCustomer = listCustomer;
    }

    @NonNull
    @Override
    public CustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.MyViewHolder holder, int position) {

        customer = listCustomer.get(position);
        holder.textView.setText(customer.toString());
        holder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dr= FirebaseDatabase.getInstance().getReference("Customer");
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()){
                            for(DataSnapshot secondSnap : snap.getChildren()){
                                if(secondSnap.getKey().equals("Details")){
                                    customerUID=snap.getKey();
                                }
                            }
                        }
                        int position = holder.getAdapterPosition();
                        Customer currentCustomer = listCustomer.get(position);
                        Intent i = new Intent(v.getContext(), UserPage.class);
                        i.putExtra("customer", currentCustomer);
                        i.putExtra("customerUID", customerUID);
                        v.getContext().startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        if(listCustomer ==  null){
            return 0;
        } else {
            return listCustomer.size();
        }

    }

    public void remove(int position){
        listCustomer.remove(position);
        notifyItemRemoved(position);
    }







}
