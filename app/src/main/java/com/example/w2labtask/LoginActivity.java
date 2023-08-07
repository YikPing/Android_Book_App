package com.example.w2labtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.loginUsername);
        password= findViewById(R.id.loginPassword);
    }

    public void login(View v){
        if (validateUsername() & validatePassword()){
            checkUser();
        }
    }

    public void moveSignUpAct(View v){
        Intent intent= new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }


    public Boolean validateUsername(){
        String strUsername = username.getText().toString();

        if (strUsername.isEmpty()){
            username.setError("Please enter a username!");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String strPassword = password.getText().toString();

        if (strPassword.isEmpty()){
            password.setError("Please enter a password!");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = mRef.orderByChild("username").equalTo(strUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    username.setError(null);
                    String passFromDB = snapshot.child(strUsername).child("password").getValue(String.class);
                    String nameFromDB = snapshot.child(strUsername).child("name").getValue(String.class);

                    if (Objects.equals(passFromDB, strPassword)){
                        username.setError(null);
                        Toast.makeText(LoginActivity.this, "Welcome "+ nameFromDB + "!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        password.setError("Invalid password");
                        password.requestFocus();
                    }
                } else {
                    username.setError("User does not exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}