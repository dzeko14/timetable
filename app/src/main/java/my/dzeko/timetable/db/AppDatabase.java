package my.dzeko.timetable.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Note;
import my.dzeko.timetable.entities.Subject;

@Database(entities = {Group.class, Subject.class, Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GroupDao getGroupDao();
    public abstract SubjectDao getSubjectDao();
    public abstract NoteDao getNoteDao();
}
