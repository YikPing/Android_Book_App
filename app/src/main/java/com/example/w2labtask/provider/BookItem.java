package com.example.w2labtask.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookItems")
public class BookItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bookId")
    private int BookId;
    @ColumnInfo(name = "bookTitle")
    private String BookTitle;
    @ColumnInfo(name = "bookIsbn")
    private String BookIsbn;
    @ColumnInfo(name = "bookAuthor")
    private String BookAuthor;
    @ColumnInfo(name = "bookDesc")
    private String BookDesc;
    @ColumnInfo(name = "bookPrice")
    private String BookPrice;


    public BookItem(){}
    public BookItem(String bookTitle, String bookIsbn, String bookAuthor, String bookDesc, String bookPrice) {
        BookTitle = bookTitle;
        BookIsbn = bookIsbn;
        BookAuthor = bookAuthor;
        BookDesc = bookDesc;
        BookPrice = bookPrice;
    }


    public int getBookId() {
        return BookId;
    }

    public void setBookId(@NonNull int bookId) {
        BookId = bookId;
    }

    public String getBookTitle() {
        return BookTitle;
    }

    public void setBookTitle(String bookTitle) {
        BookTitle = bookTitle;
    }

    public String getBookIsbn() {
        return BookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        BookIsbn = bookIsbn;
    }

    public String getBookAuthor() {
        return BookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        BookAuthor = bookAuthor;
    }

    public String getBookDesc() {
        return BookDesc;
    }

    public void setBookDesc(String bookDesc) {
        BookDesc = bookDesc;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }
}
