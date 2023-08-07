package com.example.w2labtask.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookItemRepository {

    private BookItemDao mBookItemDao;
    private LiveData<List<BookItem>> mAllBookItems;

    BookItemRepository(Application application) {
        BookItemDatabase db = BookItemDatabase.getDatabase(application);
        mBookItemDao = db.BookItemDao();
        mAllBookItems = mBookItemDao.getAllBookItem();
    }
    LiveData<List<BookItem>> getAllBookItem() {
        return mAllBookItems;
    }
    void insert(BookItem bookItem) {
        BookItemDatabase.databaseWriteExecutor.execute(() -> mBookItemDao.addBook(bookItem));
    }

    void deleteAll(){
        BookItemDatabase.databaseWriteExecutor.execute(()->{
            mBookItemDao.deleteAllBook();
        });
    }
    LiveData<Integer> getCount() {
        return mBookItemDao.getCount();
    }

    void deleteLast(){
        BookItemDatabase.databaseWriteExecutor.execute(()->{
            mBookItemDao.deleteLastBook();
        });
    }

    void deletePriceFifty(){
        BookItemDatabase.databaseWriteExecutor.execute(()->{
            mBookItemDao.deletePriceFifty();
        });
    }
}
