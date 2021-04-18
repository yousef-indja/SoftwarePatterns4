package com.example.softwarepatterns4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public DatabaseReference dr;

    private RecyclerView rv;
    MyAdapter adapter;
    private ArrayList<Stock> stockList = new ArrayList<>();
    private ArrayList<Stock> matchingStock = new ArrayList<>();

    private Button searchOption, filterOption, search, filter, addToCart;
    private Spinner searchSpinner, filterSpinner, orderSpinner;
    private EditText enterSearch;
    private ImageButton clear;
    private TextInputLayout searchLayout;

    private String[] searchCategory = {"Select Option", "Category", "Manufacturer", "Title"};
    private String[] filterCategory = {"Select Option","Category", "Manufacturer", "Title", "Price"};
    private String[] orderCategory = {"Sort By","Ascending", "Descending"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        setUpRV();
        searchOption = (Button)findViewById(R.id.searchOption);
        searchOption.setTextColor(getResources().getColor(R.color.white));
        filterOption = (Button)findViewById(R.id.filterOption);
        search = (Button)findViewById(R.id.searchButton);
        filter = (Button)findViewById(R.id.filterButton);

        searchSpinner = (Spinner) findViewById(R.id.searchSpinner);
        List<String> searchList = new ArrayList<>(Arrays.asList(searchCategory));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,searchList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        searchSpinner.setAdapter(spinnerArrayAdapter);

        filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        List<String> filterList = new ArrayList<>(Arrays.asList(filterCategory));
        final ArrayAdapter<String> filterArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,filterList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        filterSpinner.setAdapter(filterArrayAdapter);


        orderSpinner = (Spinner) findViewById(R.id.orderSpinner);
        orderSpinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, orderCategory);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(aa);


        enterSearch = (EditText)findViewById(R.id.searchText);
        searchLayout = (TextInputLayout)findViewById(R.id.searchLayout);
        clear = (ImageButton)findViewById(R.id.clear);



        dr= FirebaseDatabase.getInstance().getReference("Stock");
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

    public void searchChosen(View v){
        searchOption.setTextColor(getResources().getColor(R.color.white));
        search.setVisibility(View.VISIBLE);
        searchSpinner.setVisibility(View.VISIBLE);
        clear.setVisibility(View.VISIBLE);
        enterSearch.setVisibility(View.VISIBLE);
        searchLayout.setVisibility(View.VISIBLE);

        filterOption.setTextColor(getResources().getColor(R.color.grey));
        filter.setVisibility(View.INVISIBLE);
        filterSpinner.setVisibility(View.INVISIBLE);
        orderSpinner.setVisibility(View.INVISIBLE);

    }

    public void filterChosen(View v){
        searchOption.setTextColor(getResources().getColor(R.color.grey));
        searchSpinner.setVisibility(View.INVISIBLE);
        clear.setVisibility(View.INVISIBLE);
        enterSearch.setVisibility(View.INVISIBLE);
        searchLayout.setVisibility(View.INVISIBLE);

        filterOption.setTextColor(getResources().getColor(R.color.white));
        filter.setVisibility(View.VISIBLE);
        filterSpinner.setVisibility(View.VISIBLE);
        orderSpinner.setVisibility(View.VISIBLE);
    }

    public void clearSearch(View v){
        enterSearch.setText(null);
        redoAdapter(stockList);
        matchingStock.clear();
    }

    public void searchStock(View v){
        matchingStock.clear();
        String search = enterSearch.getText().toString();
        String categoryChosen = searchSpinner.getSelectedItem().toString();
        if(search.equals("")){
            Toast.makeText(MainActivity.this, "Error: search query empty!", Toast.LENGTH_SHORT).show();
        }else if(categoryChosen.equals("Select Option")){
            Toast.makeText(MainActivity.this, "Error: select a search option from dropdown!", Toast.LENGTH_SHORT).show();
        }else{
            for (Stock s: stockList) {
                if(categoryChosen.equals("Category")){
                    if(s.getCategory().contains(search)){
                        matchingStock.add(s);
                        redoAdapter(matchingStock);
                    }
                }else if(categoryChosen.equals("Manufacturer")){
                    if(s.getManufacturer().equalsIgnoreCase(search)){
                        matchingStock.add(s);
                        redoAdapter(matchingStock);
                    }
                }else if(categoryChosen.equals("Title")){
                    if(s.getTitle().contains(search)){
                        matchingStock.add(s);
                        redoAdapter(matchingStock);
                    }
                }

            }

            if(matchingStock.size() == 0){
                Toast.makeText(MainActivity.this, "Error: No matching results", Toast.LENGTH_SHORT).show();
                redoAdapter(stockList);
            }

        }
    }

    private void redoAdapter(ArrayList<Stock> s){

        MyAdapter adapter = new MyAdapter(s);
        rv.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));
        rv.setAdapter(adapter);


    }

    public void filterStock(View v){
        ArrayList<Stock> filterStock = new ArrayList<>();
        if(matchingStock.size() ==0){
            filterStock = stockList;
        }else{
            filterStock=matchingStock;
        }

        String categoryChosen = filterSpinner.getSelectedItem().toString();
        String order = orderSpinner.getSelectedItem().toString();

        if(categoryChosen.equals("Choose Option")){
            Toast.makeText(MainActivity.this, "Error: Please select option from dropdown list!", Toast.LENGTH_SHORT).show();
        }else{
            if(categoryChosen.equals("Category")){
                if(order.equals("Ascending")){
                    Collections.sort(filterStock, new SortByCategory());
                    redoAdapter(filterStock);
                }else if(order.equals("Descending")){
                    Collections.sort(filterStock, Collections.reverseOrder(new SortByCategory()));
                    redoAdapter(filterStock);
                }
            }

            if(categoryChosen.equals("Title")){
                if(order.equals("Ascending")){
                    Collections.sort(filterStock, new SortByTitle());
                    redoAdapter(filterStock);
                }else if(order.equals("Descending")){
                    Collections.sort(filterStock, Collections.reverseOrder(new SortByTitle()));
                    redoAdapter(filterStock);
                }
            }

            if(categoryChosen.equals("Manufacturer")){
                if(order.equals("Ascending")){
                    Collections.sort(filterStock, new SortByManufacturer());
                    redoAdapter(filterStock);
                }else if(order.equals("Descending")){
                    Collections.sort(filterStock, Collections.reverseOrder(new SortByManufacturer()));
                    redoAdapter(filterStock);
                }
            }

            if(categoryChosen.equals("Price")){
                if(order.equals("Ascending")){
                    Collections.sort(filterStock, new SortByPrice());
                    redoAdapter(filterStock);
                }else if(order.equals("Descending")){
                    Collections.sort(filterStock, Collections.reverseOrder(new SortByPrice()));
                    redoAdapter(filterStock);
                }
            }
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        MenuItem adjustStock = menu.findItem(R.id.editStock);
        if(FirebaseAuth.getInstance().getCurrentUser() != null && mUser.getEmail().equals("admin@admin.com")){
            adjustStock.setVisible(true);
        }else{
            adjustStock.setVisible(false);
        }

        return true;
    }

    public void editStock(MenuItem menuItem){

        Intent i = new Intent(MainActivity.this, EditStock.class);
        startActivity(i);
    }
    public void signUp(MenuItem menuItem){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this, SignUp.class);
            startActivity(i);
        }else{
            if(mUser.getEmail().equals("admin@admin.com")){
                Intent i = new Intent(MainActivity.this, ViewCustomers.class);
                startActivity(i);
            }else{
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent i = new Intent(MainActivity.this, SignUp.class);
                    startActivity(i);
                } else {
                    Log.d("MainActivity", "usersignedin");
                    Intent in = new Intent(MainActivity.this, UserPage.class);
                    startActivity(in);
                }
            }
        }


    }



    public void showCart(MenuItem menuItem){
        Intent i = new Intent(MainActivity.this, Cart.class);
        startActivity(i);
    }




}