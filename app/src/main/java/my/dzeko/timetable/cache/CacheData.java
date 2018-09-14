package my.dzeko.timetable.cache;

import java.util.HashMap;
import java.util.Map;

import my.dzeko.timetable.entities.Schedule;

public class CacheData {
    private Map<String, Schedule> mCachedSchedule = new HashMap<>();

    public Schedule getCachedSchedule(String groupName) {
        return mCachedSchedule.get(groupName);
    }

    public void addCachedSchedule(String groupName, Schedule cachedSchedule) {
        this.mCachedSchedule.put(groupName, cachedSchedule);
    }

    public boolean isScheduleCached(String groupName) {
        return mCachedSchedule.containsKey(groupName);
    }
}
