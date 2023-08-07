package com.example.w2labtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w2labtask.provider.BookItem;
import com.example.w2labtask.provider.BookItemDatabase;
import com.example.w2labtask.provider.BookItemViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    EditText id;
    EditText title;
    EditText isbn;
    EditText author;
    EditText desc;
    EditText price;

    String titleText;
    String isbnText;

    DrawerLayout drawer_layout;

    // Database view model
    private BookItemViewModel mBookItemViewModel;
    MyRecyclerViewAdapter adapter;

    // Firebase
    DatabaseReference mRef;

    View mainTouch;
    int initial_x = 0;
    int initial_y = 0;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set drawer to toggle button
        drawer_layout = findViewById(R.id.dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        NavigationView navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        // ListView
//        ListView listView = findViewById(R.id.main_list_view);
//        myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myList);
//        listView.setAdapter(myAdapter);

        // FAB button
//        FloatingActionButton fab = findViewById(R.id.add_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bookToast(null);
//                String titleText = title.getText().toString();
//                String priceText = price.getText().toString();
//                myList.add(titleText + " | $" + priceText);
//                myAdapter.notifyDataSetChanged();
//            }
//        });

        // request permission for sms
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS}, 0);

        // register intent
        IntentFilter intentFilter = new IntentFilter("MySMS");
        registerReceiver(myReceiver, intentFilter);

        // initialize all the edit text
        id = (EditText) findViewById(R.id.editTextID);
        title = (EditText) findViewById(R.id.editTextTitle);
        isbn = (EditText) findViewById(R.id.editTextISBN);
        author = (EditText) findViewById(R.id.editTextAuthor);
        desc = (EditText) findViewById(R.id.editTextDescription);
        price = (EditText) findViewById(R.id.editTextPrice);

        // only load when launch but not on orientation change
        if(savedInstanceState == null) {
            loadBook(null);
        }

        // Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, new BookItemRecycleFragment()).commit();
        mBookItemViewModel = new ViewModelProvider(this).get(BookItemViewModel.class);
        adapter = new MyRecyclerViewAdapter();

        // Firebase
        mRef = FirebaseDatabase.getInstance().getReference("Book/Item");

        // Touch gesture
//        mainTouch = findViewById(R.id.mainTouchFrame);
//        mainTouch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = motionEvent.getActionMasked();
//                switch(action){
//                    case(MotionEvent.ACTION_DOWN):
//                        initial_x = (int) motionEvent.getX();
//                        initial_y = (int)motionEvent.getY();
//                        if(initial_y < 100 && initial_x < 100){
//                            // if touch top left corner, capitalize author
//                            String authorText = author.getText().toString();
//                            authorText = authorText.toUpperCase();
//                            author.setText(authorText);
//                        }
//                        return true;
//                    case(MotionEvent.ACTION_UP):
//                        // if swiping bottom to top at least 40, clear field
//                        if(initial_y - motionEvent.getY() > 40){
//                            clearField(null);
//                        }else if(initial_y-motionEvent.getY() < -40) {
//                            // swipe down to close app
//                            finish();
//                        }
//                        else if(Math.abs(initial_y-motionEvent.getY()) < 40) {
//                            // if swiped from right add book
//                            if (motionEvent.getX() < initial_x) {
//                                addBookRecycle(null);
//                            }
//                        }
//                        return true;
//                    case(MotionEvent.ACTION_MOVE):
//                        // if swiping from left to right, add 1 dollar to price
//                        if(motionEvent.getX() > initial_x){
//                            double price_double = Double.parseDouble(price.getText().toString());
//                            price_double = price_double + 1;
//                            price.setText(String.format("%.2f",price_double));
//                        }
//                    default:
//                        return true;
//                }
//            }
//        });

        // Gesture Detector
        MyGestureListener myGestureListener = new MyGestureListener();
        mDetector = new GestureDetectorCompat(this, myGestureListener);
        mDetector.setOnDoubleTapListener(myGestureListener);

        View myLayout = findViewById(R.id.mainTouchFrame);
        myLayout.setOnTouchListener((View.OnTouchListener) this);
    }

    //
    public void addBookRecycle(View v){
        bookToast(null);
        String idText = id.getText().toString();
        String titleText = title.getText().toString();
        String isbnText = isbn.getText().toString();
        String authorText = author.getText().toString();
        String descText = desc.getText().toString();
        String priceText = price.getText().toString();

        BookItem bookItem = new BookItem(titleText, isbnText, authorText, descText, priceText);

        // recycleList.add(bookItem);
        mBookItemViewModel.insert(bookItem);
        adapter.notifyDataSetChanged();
        // Firebase push
        mRef.push().setValue(bookItem);
    }

    // inflate option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.clear_fields_option_menu){
            clearField(null);
        }
        else if (id == R.id.load_data_option_menu){
            loadBook(null);
        }
        else if (id == R.id.total_books_option_menu){
            mBookItemViewModel.getCount().observe(this, count -> {
                Toast.makeText(this, "There are total of " + count +" books", Toast.LENGTH_SHORT).show();
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outstate){
        super.onSaveInstanceState(outstate);

        titleText = title.getText().toString();
        outstate.putString("title",titleText);
        isbnText = isbn.getText().toString();
        outstate.putString("isbn",isbnText);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        //super.onRestoreInstanceState(savedInstanceState);

        titleText = savedInstanceState.getString("title");
        isbnText = savedInstanceState.getString("isbn");
        title.setText(titleText);
        isbn.setText(isbnText);
    }

    public void saveBook(){
        String idText = id.getText().toString();
        String titleText = title.getText().toString();
        String isbnText = isbn.getText().toString();
        String authorText = author.getText().toString();
        String descText = desc.getText().toString();
        String priceText = price.getText().toString();

        SharedPreferences myData = getSharedPreferences("f1", 0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("id", idText);
        myEditor.putString("title", titleText);
        myEditor.putString("isbn", isbnText);
        myEditor.putString("author", authorText);
        myEditor.putString("desc", descText);
        myEditor.putString("price", priceText);
        myEditor.commit();
    }

    public void bookToast(View v){
        saveBook();
        String titleText = title.getText().toString();
        String priceText = price.getText().toString();
        Toast bookAdded = Toast.makeText(this, titleText  + " at $" + priceText + " has been added", Toast.LENGTH_SHORT);
        bookAdded.show();
    }

    public void clearField(View v){
        id.getText().clear();
        title.getText().clear();
        isbn.getText().clear();
        author.getText().clear();
        desc.getText().clear();
        price.getText().clear();
    }

    public void doublePrice(View v){
        double price_double = Double.parseDouble(price.getText().toString());
        price_double = price_double * 2;
        price.setText(String.format("%.2f",price_double));
    }

    public void loadBook(View v){
        SharedPreferences myData = getSharedPreferences("f1", 0);
        String idText = myData.getString("id", "");
        String titleText = myData.getString("title", "");
        String isbnText = myData.getString("isbn", "");
        String authorText = myData.getString("author", "");
        String descText = myData.getString("desc", "");
        String priceText = myData.getString("price", "");

        id.setText(idText);
        title.setText(titleText);
        isbn.setText(isbnText);
        author.setText(authorText);
        desc.setText(descText);
        price.setText(priceText);
    }

    public void setISBN(View v){

        SharedPreferences myData = getSharedPreferences("f1", 0);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("isbn", "00112233");
        myEditor.commit();

    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String bookDetail = intent.getStringExtra("bookDetail");
            StringTokenizer bookDetailParts = new StringTokenizer(bookDetail,"|");

            id.setText(bookDetailParts.nextToken());
            title.setText(bookDetailParts.nextToken());
            isbn.setText(bookDetailParts.nextToken());
            author.setText(bookDetailParts.nextToken());
            desc.setText(bookDetailParts.nextToken());
            String newPrice = bookDetailParts.nextToken();
            price.setText(newPrice);

            if(bookDetailParts.hasMoreTokens()){
                Boolean incPrice = Boolean.parseBoolean(bookDetailParts.nextToken());
                if(incPrice){
                    double price_double = Double.parseDouble(newPrice);
                    price_double = price_double + 100;
                    price.setText(String.format("%.2f",price_double));
                }
                else if(!incPrice){
                    double price_double = Double.parseDouble(newPrice);
                    price_double = price_double + 5;
                    price.setText(String.format("%.2f",price_double));
                }
            }
        }
    };

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.nvAddBook) {
                addBookRecycle(null);
            }
            else if (id == R.id.nvRemoveLast) {
                mBookItemViewModel.deleteLast();
                adapter.notifyDataSetChanged();
            }
            else if (id == R.id.nvRemoveAll) {
                mBookItemViewModel.deleteAll();
                adapter.notifyDataSetChanged();
                // Firebase delete all
                mRef.removeValue();
            }
            else if (id == R.id.nvListAll) {
                Intent myIntent = new Intent(MainActivity.this, BookItemActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
            else if (id == R.id.removeFifty) {
                mBookItemViewModel.deletePriceFifty();
                adapter.notifyDataSetChanged();
            }
            else if (id == R.id.nvClose){
                finish();
            }

            // close the drawer
            drawer_layout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);

        return true;
    }

    // Gesture Detector convenience class
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            // set random isbn on single tap
            String randomIsbn = RandomString.generateNewRandomString(7);
            isbn.setText(randomIsbn);

            return true;
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            // clear field on double tap
            clearField(null);

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // increment/decrement the price by the amount of scroll
            if(!price.getText().toString().isEmpty()) {
                double price_double = Double.parseDouble(price.getText().toString());
                price_double = price_double - distanceX;
                price.setText(String.format("%.2f", price_double));
            }
            // if vertical scroll, set title to "Untitled"
            if (Math.abs(distanceY) > 20 & Math.abs(distanceX) < 40) {
                title.setText("Untitled");
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // move activity to background on fling
            if(Math.abs(velocityX) > 3000 | Math.abs(velocityY) > 3000) {
                moveTaskToBack(true);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // load default/saved attributes on long press
            loadBook(null);

        }

    }
}
