package com.example.softwarepatterns4;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public DatabaseReference dr;

    private Spinner spinner;

    private String[] payment = new String[]{"Choose Payment Method", "PayPal", "Visa", "Mastercard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        mAuth = FirebaseAuth.getInstance();

        spinner = (Spinner)findViewById(R.id.spinner);
        List<String> paymentList = new ArrayList<>(Arrays.asList(payment));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,paymentList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
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
        spinner.setAdapter(spinnerArrayAdapter);


    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    public void logIn(MenuItem menuItem){
        Intent i = new Intent(SignUp.this, LogIn.class);
        startActivity(i);
    }


    public void signUp(View v){
        TextView e = (TextView)findViewById(R.id.email);
        final String email = e.getText().toString();

        TextView n = (TextView)findViewById(R.id.name);
        final String name = n.getText().toString();

        TextView p = (TextView)findViewById(R.id.signUpPass);
        final String password = p.getText().toString();

        TextView a = (TextView)findViewById(R.id.address);
        final String address = a.getText().toString();

        TextView ph = (TextView)findViewById(R.id.phone);
        final String phone = ph.getText().toString();

        String chosenPayment = spinner.getSelectedItem().toString();

        if(email.equals("") || name.equals("") || password.equals("")|| phone.equals("")|| address.equals("") || chosenPayment.equals("Choose Payment Method")){
            Toast.makeText(SignUp.this, "Please Fill In All Fields!", Toast.LENGTH_SHORT).show();
        } else if(password.length() < 6){
            Toast.makeText(SignUp.this, "Password Must Be At Least 6 Characters!", Toast.LENGTH_SHORT).show();
        }  else {
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>(){
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    boolean check = !task.getResult().getSignInMethods().isEmpty();

                    if(check){
                        Toast.makeText(SignUp.this, "Error: Email Already In Use!", Toast.LENGTH_SHORT).show();
                    } else{
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("signUp", "createUserWithEmail:success");
                                            mUser = mAuth.getCurrentUser();
                                            dr = FirebaseDatabase.getInstance().getReference();
                                            Customer customer = new Customer.Builder().setName(name).setShippingAddress(address).setPaymentMethod(chosenPayment).setPhoneNumber(phone).create();

                                            dr.child("Customer").child(mUser.getUid()).child("Details").setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(SignUp.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(SignUp.this, MainActivity.class);
                                                    startActivity(i);
                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SignUp.this, "Error: Sign Up Failure!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Log.w("signUp", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(SignUp.this, "ERROR: Sign Up Failure!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                });
                    }
                }
            });
        }



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

