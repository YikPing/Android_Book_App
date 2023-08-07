package com.example.w2labtask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w2labtask.provider.BookItem;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

    List<BookItem> data = new ArrayList<>();
    public MyRecyclerViewAdapter(){
    }

    public void setData(List<BookItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.position.setText("No. "+position);
        holder.id.setText("ID: "+data.get(position).getBookId());
        holder.title.setText("Title: "+data.get(position).getBookTitle());
        holder.isbn.setText("ISBN: "+data.get(position).getBookIsbn());
        holder.author.setText("Author: "+data.get(position).getBookAuthor());
        holder.desc.setText("Desc: "+data.get(position).getBookDesc());
        holder.price.setText("Price: $"+data.get(position).getBookPrice());

        final int fPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() { //set back to itemView for students
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "The "+ data.get(position).getBookTitle() +" Book by " + data.get(position).getBookAuthor(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView position;
        public TextView id;
        public TextView title;
        public TextView isbn;
        public TextView author;
        public TextView desc;
        public TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.card_position);
            id = itemView.findViewById(R.id.card_id);
            title = itemView.findViewById(R.id.card_title);
            isbn = itemView.findViewById(R.id.card_isbn);
            author = itemView.findViewById(R.id.card_author);
            desc = itemView.findViewById(R.id.card_desc);
            price = itemView.findViewById(R.id.card_price);
        }
    }
}
