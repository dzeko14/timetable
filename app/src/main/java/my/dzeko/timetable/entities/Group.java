package my.dzeko.timetable.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Group {
    @PrimaryKey
    @NonNull
    private String mName;

    @Ignore
    public Group() {
    }

    public Group(@NonNull String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            Group otherGroup = (Group) obj;
            return mName.equals(otherGroup.mName);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }
}
