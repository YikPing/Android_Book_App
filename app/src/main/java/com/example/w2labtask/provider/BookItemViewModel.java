package com.example.w2labtask.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookItemViewModel extends AndroidViewModel {
    private BookItemRepository mRepository;
    private LiveData<List<BookItem>> mAllBookItems;

    public BookItemViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookItemRepository(application);
        mAllBookItems = mRepository.getAllBookItem();
    }

    public LiveData<List<BookItem>> getAllBookItems() {
        return mAllBookItems;
    }

    public void insert(BookItem bookitem) {
        mRepository.insert(bookitem);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }

    public LiveData<Integer> getCount() {
        return mRepository.getCount();
    }

    public void deleteLast(){mRepository.deleteLast();}

    public void deletePriceFifty(){
        mRepository.deletePriceFifty();
    }
}
