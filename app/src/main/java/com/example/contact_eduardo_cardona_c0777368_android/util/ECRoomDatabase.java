package com.example.contact_eduardo_cardona_c0777368_android.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contact_eduardo_cardona_c0777368_android.data.ContactDao;
import com.example.contact_eduardo_cardona_c0777368_android.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ECRoomDatabase extends  RoomDatabase {
    public abstract ContactDao contactDao();
    // instance of the room database
    private static volatile ECRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    // executor service helps to do tasks in background thread
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ECRoomDatabase getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (ECRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ECRoomDatabase.class, "room_ec_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecutor.execute(() -> {
                        ContactDao contactDao = INSTANCE.contactDao();
                        contactDao.deleteAll();

//                        Employee employee = new Employee("Mo", "cs", "", 1111);
//                        employeeDao.insert(employee);
                    });
                }
            };
}
