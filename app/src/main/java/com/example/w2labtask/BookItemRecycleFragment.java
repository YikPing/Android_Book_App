package com.example.w2labtask;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.w2labtask.provider.BookItemViewModel;
import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookItemRecycleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookItemRecycleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private BookItemViewModel mBookItemViewModel;
    MyRecyclerViewAdapter adapter;

    public BookItemRecycleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookItemRecycleFragment.
     */
    public static BookItemRecycleFragment newInstance(String param1, String param2) {
        BookItemRecycleFragment fragment = new BookItemRecycleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookItemViewModel = new ViewModelProvider(this).get(BookItemViewModel.class);
        mBookItemViewModel.getAllBookItems().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_book_item_recycle, container, false);
        // Recycler View
        recyclerView = layout.findViewById(R.id.mainRecyclerView);

        layoutManager = new LinearLayoutManager(layout.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return layout;
    }
}