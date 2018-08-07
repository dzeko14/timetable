package my.dzeko.timetable.observers;

import java.util.List;

import my.dzeko.timetable.models.Schedule;
import my.dzeko.timetable.observers.interfaces.IScheduleObserver;
import my.dzeko.timetable.observers.interfaces.Observable;

public class ScheduleObservable extends Observable<IScheduleObserver> {
    private static ScheduleObservable mInstance;
    private ScheduleObservable() {}
    public static ScheduleObservable getInstance() {
        if(mInstance == null){
            mInstance = new ScheduleObservable();
        }
        return mInstance;
    }

    public void notifySelectedScheduleChanged(Schedule schedule) {
        List<IScheduleObserver> observers = getObservers();
        for (IScheduleObserver observer : observers) {
            observer.onSelectedScheduleChanged(schedule);
        }
    }
}
