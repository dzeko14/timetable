package my.dzeko.timetable.contracts;

import android.widget.BaseExpandableListAdapter;

import java.util.List;

import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.observers.interfaces.IScheduleObserver;

public class EditingContract {
    public interface View extends IView {
        void addWeekToAdapter(List<Day> week);
        void setupAdapter();
    }

    public interface Presenter extends IPresenter, IScheduleObserver {
        void onViewCreated();
    }
}
