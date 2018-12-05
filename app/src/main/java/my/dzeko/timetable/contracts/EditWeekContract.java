package my.dzeko.timetable.contracts;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import my.dzeko.timetable.adapters.EditWeekRecyclerAdapter;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.observers.interfaces.IScheduleObserver;

public class EditWeekContract {
    public interface View extends IView {
        void setAdapter(RecyclerView.Adapter adapter);
        void updateAdapter();
        void startActivity(Bundle bundle, Class c);
        void updateAdapter(Week mWeek);
    }

    public interface Presenter extends IPresenter, IScheduleObserver,
            EditWeekRecyclerAdapter.OnEditExpandableListViewChildItemListener,
            EditWeekRecyclerAdapter.OnRemoveExpandableListViewChildItemListener,
            EditWeekRecyclerAdapter.OnAddExpandableListViewGroupItemListener{
        void onViewInitialized();
        void setWeek(Week week);
    }
}
