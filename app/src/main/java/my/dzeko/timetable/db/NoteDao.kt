package my.dzeko.timetable.db

import android.arch.persistence.room.*
import my.dzeko.timetable.entities.Note

@Dao
interface NoteDao {
    @Query("Select * From Note Where `group` = :group")
    fun getNotesByGroup(group :String) :List<Note>

    @Query("Select * From Note Where `group` = :group And date = :date")
    fun getNotesByGroupAndDate(group :String, date :String) :List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note :Note)

    @Delete
    fun deleteNote(note :Note)
}