package my.dzeko.timetable.observers.interfaces;

import my.dzeko.timetable.entities.Schedule;

public interface IScheduleObserver extends IObserver {
    void onSelectedScheduleChanged(Schedule schedule);
}
