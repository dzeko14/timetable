package my.dzeko.timetable.presenters;

import java.util.List;

import my.dzeko.timetable.adapters.EditWeekExpandableListAdapter;
import my.dzeko.timetable.contracts.EditWeekContract;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Subject;

public class EditWeekPresenter implements EditWeekContract.Presenter {
    private EditWeekContract.View mView;
    private List<Day> mWeek;

    public EditWeekPresenter(EditWeekContract.View view) {
        mView = view;
    }

    @Override
    public boolean onUserClick(int itemId) {
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void onEditClick(Subject subject) {

    }

    @Override
    public void onRemoveChildItemClick(Subject subject) {

    }

    @Override
    public void onRemoveGroupItemClick(Day day) {

    }

    @Override
    public void setWeek(List<Day> week) {
        mWeek = week;

    }

    private EditWeekExpandableListAdapter getAdapterFromSchedule(List<Day> schedule) {
        return new EditWeekExpandableListAdapter(mView.getContext(), schedule);
    }

    @Override
    public void onViewInitialized() {
        EditWeekExpandableListAdapter adapter = getAdapterFromSchedule(mWeek);
        adapter.setEditChildItemListener(this);
        adapter.setRemoveGroupItemListener(this);
        adapter.setRemoveChildItemListener(this);
        mView.setupExpandableListAdapter(adapter);
    }
}
