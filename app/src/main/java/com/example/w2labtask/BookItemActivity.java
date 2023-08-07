package com.example.w2labtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class BookItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_item);

        Intent intent = getIntent();

        // fragment
        getSupportFragmentManager().beginTransaction().add(R.id.bookActivityFrame, new BookItemRecycleFragment()).commit();
    }
}