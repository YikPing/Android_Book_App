package com.example.w2labtask.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookItemDao {

    @Query("select * from bookItems")
    LiveData<List<BookItem>> getAllBookItem();

    @Query("select * from bookItems where bookTItle=:title")
    List<BookItem> getBookItem(String title);

    @Insert
    void addBook(BookItem bookItem);

    @Query("delete from bookItems where bookTitle= :title")
    void deleteBook(String title);

    @Query("delete FROM bookItems")
    void deleteAllBook();

    @Query("select count(*) from bookItems")
    LiveData<Integer> getCount();

    @Query("DELETE FROM bookItems WHERE Bookid = (SELECT Bookid FROM bookItems ORDER BY Bookid DESC LIMIT 1)")
    void deleteLastBook();

    @Query("delete from bookItems where CAST(bookPrice AS REAL) > 50")
    void deletePriceFifty();

}
