package my.dzeko.timetable.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.EditWeekExpandableListAdapter;
import my.dzeko.timetable.contracts.EditWeekContract;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.presenters.EditWeekPresenter;

public class EditWeekFragment extends Fragment implements EditWeekContract.View {
    private EditWeekContract.Presenter mPresenter = new EditWeekPresenter(this);
    private ExpandableListView mExpandableListView;

    public EditWeekFragment() {
        // Required empty public constructor
    }

    public static EditWeekFragment getInstance() {
        return new EditWeekFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_week, container, false);
        initializeViews(rootView);
        mPresenter.onViewInitialized();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    private void initializeViews(View rootView) {
        mExpandableListView = rootView.findViewById(R.id.edit_week_fragment_expandable_list_view);
    }

    public void setWeek(List<Day> week) {
        mPresenter.setWeek(week);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void updateAdapter() {
        ((EditWeekExpandableListAdapter)mExpandableListView.getExpandableListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void setAdapter(BaseExpandableListAdapter adapter) {
        mExpandableListView.setAdapter(adapter);
    }
}
