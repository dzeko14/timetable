package my.dzeko.timetable.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import my.dzeko.timetable.entities.Group;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM `Group`")
    List<Group> getAllGroups();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void saveGroup(Group group);

    @Delete
    void deleteGroup(Group group);
}
