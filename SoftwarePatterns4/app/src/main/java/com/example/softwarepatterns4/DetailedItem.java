package com.example.softwarepatterns4;

import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailedItem extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser fUser;
    DatabaseReference dr;

    TextView title, man, price ,stock, id , category;
    Spinner qauntSpinner;

    TextView leaveReview, reviewsHead;
    EditText reviewText;
    Button submitButton;
    RatingBar rb;
    private RecyclerView rv;
    ReviewAdapter adapter;
    private ArrayList<Review> reviews = new ArrayList<>();


    Stock s;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        setContentView(R.layout.detailitem_layout);

        Intent intent = getIntent();
        String stringID = intent.getStringExtra(MyAdapter.KEY1);

        setUpRV();

        title = (TextView) findViewById(R.id.title);
        man = (TextView) findViewById(R.id.manufacturer);
        price = (TextView) findViewById(R.id.price);
        stock = (TextView) findViewById(R.id.stock);
        id = (TextView) findViewById(R.id.id);
        category = (TextView) findViewById(R.id.category);

        leaveReview = (TextView)findViewById(R.id.leaveReviewHead);
        reviewText = (EditText)findViewById(R.id.reviewText);
        submitButton = (Button)findViewById(R.id.submitReview);
        rb = (RatingBar)findViewById(R.id.ratingBar);

        reviewsHead = (TextView)findViewById(R.id.reviewsHead);


        //final Stock[] stock = new Stock[1];


        dr= FirebaseDatabase.getInstance().getReference("Stock").child(stringID);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                s = dataSnapshot.getValue(Stock.class);
                title.setText(s.getTitle());
                man.setText(s.getManufacturer());
                price.setText(String.valueOf(s.getPrice()));
                stock.setText(String.valueOf(s.getStock()));
                id.setText(String.valueOf(s.getId()));
                category.setText(s.getCategory());
                populateSpinner();
                checkPurchase(s.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dr= FirebaseDatabase.getInstance().getReference("Reviews");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    for (DataSnapshot snap2 : snap.getChildren()){
                        Review r = snap2.getValue(Review.class);
                        if(r.getId().equals(String.valueOf(s.getId()))){
                            reviews.add(r);
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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem adjustStock = menu.findItem(R.id.editStock);
        if(FirebaseAuth.getInstance().getCurrentUser() != null && fUser.getEmail().equals("admin@admin.com")){
            adjustStock.setVisible(true);
        }else{
            adjustStock.setVisible(false);
        }
        return true;
    }

    private void checkPurchase(int pageId){
        dr= FirebaseDatabase.getInstance().getReference("Customer").child(fUser.getUid()).child("Purchases");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean purchaseReview = false;
                boolean purchased = false;
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Stock s = snap.getValue(Stock.class);
                    if(s.getId() == pageId){
                        purchased = true;
                        if(s.isReviewed()==true){
                            purchaseReview = true;
                        }
                    }
                }

                if(purchaseReview == false && purchased == true){
                    leaveReview.setVisibility(View.VISIBLE);
                    reviewText.setVisibility(View.VISIBLE);
                    rb.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.VISIBLE);

                    reviewsHead.setVisibility(View.INVISIBLE);
                    rv.setVisibility(View.INVISIBLE);

                }else{
                    leaveReview.setVisibility(View.INVISIBLE);
                    reviewText.setVisibility(View.INVISIBLE);
                    rb.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.INVISIBLE);

                    reviewsHead.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void submitReview(View v){
        if(reviewText.equals("")){
            Toast.makeText(DetailedItem.this, "Error: Please fill in review field!", Toast.LENGTH_SHORT).show();
        }else if(rb.getRating()<.5){
            Toast.makeText(DetailedItem.this, "Error: Please leave a star rating!", Toast.LENGTH_SHORT).show();
        }else{
            Review rev = new Review.Builder().setText(reviewText.getText().toString()).setId(id.getText().toString())
                    .setName(fUser.getEmail()).setStars(rb.getRating()).create();
            dr= FirebaseDatabase.getInstance().getReference("Reviews").child(fUser.getUid()).child(id.getText().toString());
            dr.setValue(rev);

            dr= FirebaseDatabase.getInstance().getReference("Customer").child(fUser.getUid()).child("Purchases").child(id.getText().toString()).child("reviewed");
            dr.setValue(true);


            leaveReview.setVisibility(View.INVISIBLE);
            reviewText.setVisibility(View.INVISIBLE);
            rb.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);

            reviewsHead.setVisibility(View.VISIBLE);
            rv.setVisibility(View.VISIBLE);

            DetailedItem.this.recreate();
        }
    }

    public void setUpRV() {
        rv = (RecyclerView) findViewById(R.id.myRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new ReviewAdapter(reviews);

        rv.addItemDecoration((new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)));

        rv.setAdapter(adapter);

    }
    private void populateSpinner(){
        ArrayList<String> quantList = new ArrayList<>();
        quantList.add("Choose Quantity");
        for(int i=1; i<=Integer.parseInt(stock.getText().toString());i++){
            quantList.add(String.valueOf(i));
        }
        qauntSpinner = (Spinner) findViewById(R.id.quantSpinner);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,quantList){
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
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        qauntSpinner.setAdapter(arrayAdapter);
    }

    public void addToCart(View v){
        if(qauntSpinner.getSelectedItem().toString().equals("Select Quantity")){

        }else{
            s.setQuantity(Integer.parseInt(qauntSpinner.getSelectedItem().toString()));
            dr= FirebaseDatabase.getInstance().getReference("Customer").child(fUser.getUid()).child("Cart").child(String.valueOf(s.getId()));
            dr.setValue(s);
            int remainingStock= (s.getStock()-s.getQuantity());
            dr= FirebaseDatabase.getInstance().getReference("Stock").child(String.valueOf(s.getId())).child("stock");
            dr.setValue(remainingStock);
            Toast.makeText(DetailedItem.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DetailedItem.this, MainActivity.class);
            startActivity(i);
        }

    }

    public void editStock(MenuItem menuItem){
        Intent i = new Intent(DetailedItem.this, EditStock.class);
        i.putExtra("stock" , s);
        startActivity(i);

    }

}
