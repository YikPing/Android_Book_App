package com.example.w2labtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    EditText username;

    EditText password;

    EditText name;

    FirebaseDatabase databse;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.signUpUsernameEdit);
        password = findViewById(R.id.signUpPassEdit);
        name = findViewById(R.id.signUpName);
    }

    public void signUp(View v){
        databse = FirebaseDatabase.getInstance();
        reference = databse.getReference("users");

        String newName = name.getText().toString();
        String newUsername = username.getText().toString();
        String newPassword = password.getText().toString();

        // save to firebase database
        User userData = new User(newName, newUsername, newPassword);
        reference.child(newUsername).setValue(userData);

        Toast.makeText(SignUpActivity.this, "Sign up Successfull!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}