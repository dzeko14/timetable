package my.dzeko.timetable.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import my.dzeko.timetable.entities.Subject;

@Dao
public abstract class SubjectDao {
    @Query("SELECT * FROM Subject WHERE mGroup = :groupName")
    public abstract List<Subject> getSubjectsByGroupName(String groupName);

    @Insert
    public abstract void saveSubjects(List<Subject> subjects);

    @Query("DELETE FROM Subject WHERE mGroup = :groupName")
    public abstract void deleteSubjectsByGroupName(String groupName);

    @Transaction
    public void saveOrUpdateSchedule(String groupName, List<Subject> subjects) {
        deleteSubjectsByGroupName(groupName);
        saveSubjects(subjects);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long saveSubject(Subject subject);

    @Query("SELECT * FROM Subject WHERE mDayId = :dayId AND mWeekId = :weekId AND mGroup = :groupName")
    public abstract List<Subject> getSubjectsByDayIdAndWeekId(int dayId, int weekId, String groupName);

    @Delete
    public abstract void removeSubject(Subject subject);

    @Query("SELECT * FROM Subject WHERE mId = :subjectId")
    public abstract Subject getSubjectById(long subjectId);
}
