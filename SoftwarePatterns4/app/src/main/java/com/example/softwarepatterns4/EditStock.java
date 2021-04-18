package com.example.softwarepatterns4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditStock extends AppCompatActivity {

    EditText title, price, manufacturer, category, id, stock;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public DatabaseReference dr;

    boolean replace = false;

    Stock stck, newStock;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editstock_layout);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        title = (EditText)findViewById(R.id.title);
        price = (EditText)findViewById(R.id.price);
        manufacturer = (EditText)findViewById(R.id.manufacturer);
        category = (EditText)findViewById(R.id.category);
        id = (EditText)findViewById(R.id.id);
        stock = (EditText)findViewById(R.id.stock);


        Intent i = getIntent();
        stck = (Stock)getIntent().getSerializableExtra("stock");
        if(stck == null){

        }else{
            replace = true;
            title.setText(stck.getTitle());
            price.setText(String.valueOf(stck.getPrice()));
            manufacturer.setText(stck.getManufacturer());
            category.setText(stck.getCategory());
            id.setText(String.valueOf(stck.getId()));
            stock.setText(String.valueOf(stck.getStock()));

        }
    }

    public void editStock(View v){

        if(title.getText().toString().equals("") || price.getText().toString().equals("")|| manufacturer.getText().toString().equals("")|| category.getText().toString().equals("")||
                id.getText().toString().equals("")|| stock.getText().toString().equals("")){
            Toast.makeText(EditStock.this, "Error: Please fill in all fields!", Toast.LENGTH_SHORT).show();

        }else{
            newStock = new Stock(Integer.parseInt(id.getText().toString()), title.getText().toString(), manufacturer.getText().toString(), category.getText().toString(),
                    Double.parseDouble(price.getText().toString()), Integer.parseInt(stock.getText().toString()));
            if(replace == true){
                dr= FirebaseDatabase.getInstance().getReference("Stock").child(String.valueOf(stck.getId()));
                dr.removeValue();
            }
            dr= FirebaseDatabase.getInstance().getReference("Stock");
            dr.child(id.getText().toString()).setValue(newStock);

            Toast.makeText(EditStock.this, "Stock Updated!", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(EditStock.this, MainActivity.class);
            startActivity(in);
        }


    }
}
