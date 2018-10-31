package my.dzeko.timetable.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        foreignKeys = [ForeignKey(entity = Subject::class, parentColumns = ["mId"], childColumns = ["subjectId"], onDelete = CASCADE)],
        indices = [Index("subjectId")]
)
data class Note(
        @PrimaryKey(autoGenerate = true)
        val id :Int,
        val text :String,
        val date : String,
        val subjectId :Int,
        val group :String
)