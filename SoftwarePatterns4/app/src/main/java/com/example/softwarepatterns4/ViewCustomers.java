package com.example.softwarepatterns4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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

public class ViewCustomers extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public DatabaseReference dr;

    private RecyclerView rv;
    CustomerAdapter adapter;
    private ArrayList<Customer> customerList = new ArrayList<>();

    private String customerUID;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcustomers_layout);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        setUpRV();


        dr= FirebaseDatabase.getInstance().getReference("Customer");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    for(DataSnapshot secondSnap : snap.getChildren()){
                        if(secondSnap.getKey().equals("Details")){
                            Customer c = secondSnap.getValue(Customer.class);
                            customerList.add(c);
                            customerUID=snap.getKey();
                        }
                    }
                }
                setUpRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUpRV() {
        rv = (RecyclerView) findViewById(R.id.myRecyclerView);
        rv.isClickable();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new CustomerAdapter(customerList);

        rv.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        rv.setAdapter(adapter);

        if(customerList.isEmpty()){

        }else{

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void logOut(View v){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent i = new Intent(ViewCustomers.this, MainActivity.class);
        startActivity(i);
    }
}
