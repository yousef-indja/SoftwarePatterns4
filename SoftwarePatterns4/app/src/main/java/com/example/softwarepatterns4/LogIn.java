package com.example.softwarepatterns4;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mAuth = FirebaseAuth.getInstance();

    }


    public void logIn (View V){
        TextView e = (TextView)findViewById(R.id.email);
        String email = e.getText().toString();

        TextView p = (TextView)findViewById(R.id.logInPass);
        String password = p.getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(LogIn.this, "Please Fill In All Fields!", Toast.LENGTH_SHORT).show();
        } else if(password.length() < 6) {
            Toast.makeText(LogIn.this, "Password Must Be At Least 6 Characters!", Toast.LENGTH_SHORT).show();
        } else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                        public void onComplete(Task<AuthResult> task){
                            if (task.isSuccessful()){
                                Toast.makeText(LogIn.this, "User Signed In.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LogIn.this, MainActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(LogIn.this, "Error Signing In. Check Credentials And Try Again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

