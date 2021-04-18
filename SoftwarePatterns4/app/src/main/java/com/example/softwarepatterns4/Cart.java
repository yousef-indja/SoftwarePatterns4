package com.example.softwarepatterns4;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class Cart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public DatabaseReference dr;
    public DatabaseReference dr2;

    private RecyclerView rv;
    CartAdapter adapter;

    private ArrayList<Stock> cartList = new ArrayList<>();
    private TextView total;

    private Button b;
    double amount = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        b = (Button)findViewById(R.id.checkout);
        if(mUser.getEmail().equals("admin@admin.com")){
            b.setText("Simulate Checkout");
        }else{
            b.setText("Checkout");
        }
        Intent intent = getIntent();
        String key = intent.getStringExtra(MyAdapter.KEY1);
        try{
            amount = Double.parseDouble(key);
        }catch(NullPointerException e){

        }


        setUpRV();
        total = (TextView)findViewById(R.id.total);
        dr= FirebaseDatabase.getInstance().getReference("Customer").child(mUser.getUid()).child("Cart");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Stock stock = snap.getValue(Stock.class);
                    cartList.add(stock);
                }
                setTotal();
                setUpRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_exit, menu);
        return true;
    }


    public void setTotal(){
        double t = 0;
        for(Stock s: cartList){
            t+=(s.getQuantity()*s.getPrice());
        }
        if(amount == 0){
            total.setText(String.valueOf(t));
        }else{
            t = t - amount;
            total.setText(String.valueOf(t));
        }

    }

    public void setUpRV() {
        rv = (RecyclerView) findViewById(R.id.myRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        adapter = new CartAdapter(cartList);

        rv.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        rv.setAdapter(adapter);

    }

    public void exit(MenuItem menuItem){
        Intent i = new Intent(Cart.this, MainActivity.class);
        startActivity(i);

    }

    public void checkout(View V){
        for(Stock s: cartList){
            dr= FirebaseDatabase.getInstance().getReference("Customer").child(mUser.getUid()).child("Purchases").child(String.valueOf(s.getId()));
            s.setReviewed(false);
            dr.setValue(s);


            dr2= FirebaseDatabase.getInstance().getReference("Customer").child(mUser.getUid()).child("Cart").child(String.valueOf(s.getId()));
            dr2.removeValue();
        }

        Toast.makeText(Cart.this, "Checkout Success!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Cart.this, MainActivity.class);
        startActivity(i);
    }



}
