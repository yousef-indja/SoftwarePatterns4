package com.example.softwarepatterns4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser fUser;
    DatabaseReference dr;

    private RecyclerView rv;
    MyAdapter adapter;
    private ArrayList<Stock> stockList = new ArrayList<>();

    String customerUID;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.userpage_layout);

        Intent i = getIntent();
        Customer customer = (Customer)getIntent().getSerializableExtra("customer");
        customerUID = i.getStringExtra("customerUID");
        final TextView email =(TextView)findViewById(R.id.email);
        final TextView emailHead = (TextView)findViewById(R.id.emailHead);
        final TextView address =(TextView)findViewById(R.id.address);
        final TextView phone =(TextView)findViewById(R.id.number);
        final TextView payment =(TextView)findViewById(R.id.payment);


        setUpRV();

        fUser = auth.getCurrentUser();

        if(fUser.getEmail().equals("admin@admin.com")){
            email.setText(customer.getName());
            emailHead.setText("Name");
            address.setText(customer.getShippingAddress());
            phone.setText(customer.getPhoneNumber());
            payment.setText(customer.getPaymentMethod());

            dr= FirebaseDatabase.getInstance().getReference("Customer").child(customerUID).child("Purchases");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Stock stock = snap.getValue(Stock.class);
                        stockList.add(stock);
                    }
                    setUpRV();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }else{
            dr= FirebaseDatabase.getInstance().getReference("Customer").child(fUser.getUid()).child("Details");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Customer customer = dataSnapshot.getValue(Customer.class);
                        email.setText(fUser.getEmail());
                        address.setText(customer.getShippingAddress());
                        phone.setText(customer.getPhoneNumber());
                        payment.setText(customer.getPaymentMethod());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            dr= FirebaseDatabase.getInstance().getReference("Customer").child(fUser.getUid()).child("Purchases");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()){
                        Stock stock = snap.getValue(Stock.class);
                        stockList.add(stock);
                    }
                    setUpRV();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    public void setUpRV() {
        rv = (RecyclerView) findViewById(R.id.myRecyclerView);
        rv.isClickable();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new MyAdapter(stockList);

        rv.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        rv.setAdapter(adapter);

        if(stockList.isEmpty()){

        }else{

        }
    }

    public void logOut(View v){
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent i = new Intent(UserPage.this, MainActivity.class);
        startActivity(i);
    }

}
