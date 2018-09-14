package my.dzeko.timetable.contracts;

import android.widget.BaseExpandableListAdapter;

import java.util.List;

import my.dzeko.timetable.adapters.EditWeekExpandableListAdapter;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;

public class EditWeekContract {
    public interface View extends IView {
        void setAdapter(BaseExpandableListAdapter adapter);
        void updateAdapter();
    }

    public interface Presenter extends IPresenter,
            EditWeekExpandableListAdapter.OnEditExpandableListViewChildItemListener,
            EditWeekExpandableListAdapter.OnRemoveExpandableListViewChildItemListener,
            EditWeekExpandableListAdapter.OnRemoveExpandableListViewGroupItemListener{
        void onViewInitialized();
        void setWeek(List<Day> week);
    }
}
