package my.dzeko.timetable.wrappers;

import android.arch.persistence.room.Room;
import android.content.Context;

import my.dzeko.timetable.db.AppDatabase;

public class DatabaseWrapper {
    private static AppDatabase database;

    private static final String DATABASE_NAME = "app.db";

    private DatabaseWrapper() {
    }

    public static void initialize(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public static AppDatabase getDatabase(){
        return database;
    }
}
