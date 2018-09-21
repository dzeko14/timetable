package my.dzeko.timetable.contracts;

import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

import my.dzeko.timetable.adapters.EditWeekExpandableListAdapter;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.observers.interfaces.IScheduleObserver;

public class EditWeekContract {
    public interface View extends IView {
        void setAdapter(BaseExpandableListAdapter adapter);
        void updateAdapter();
        void startActivity(Bundle bundle, Class c);
        void updateAdapter(Week mWeek);
        void setTitle(String weekName);
    }

    public interface Presenter extends IPresenter,
            IScheduleObserver,
            EditWeekExpandableListAdapter.OnEditExpandableListViewChildItemListener,
            EditWeekExpandableListAdapter.OnRemoveExpandableListViewChildItemListener,
            EditWeekExpandableListAdapter.OnRemoveExpandableListViewGroupItemListener{
        void onViewInitialized();
        void setWeek(Week week);
    }
}
