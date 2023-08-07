package com.example.w2labtask.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {BookItem.class}, version = 1)
public abstract class BookItemDatabase extends RoomDatabase {
    public static final String BookItem_DATABASE_NAME = "book_item_database";

    public abstract BookItemDao BookItemDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile BookItemDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static BookItemDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {

            synchronized (BookItemDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BookItemDatabase.class, BookItem_DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
