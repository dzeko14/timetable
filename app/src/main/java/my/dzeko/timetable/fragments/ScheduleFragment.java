package my.dzeko.timetable.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.ScheduleAdapter;
import my.dzeko.timetable.contracts.ScheduleContract;
import my.dzeko.timetable.models.Schedule;
import my.dzeko.timetable.presenters.SchedulePresenter;

/**
 * Fragment is used as View in MVP pattern
 */
public class ScheduleFragment extends Fragment implements ScheduleContract.View {
    ScheduleContract.Presenter mPresenter;
    ScheduleAdapter mAdapter;
    RecyclerView mRecyclerView;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment getInstance() {
        return new ScheduleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializePresenter();
    }

    private void initializePresenter() {
        mPresenter = new SchedulePresenter(this);
        mPresenter.onScheduleRequest();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootFragmentView = inflater.inflate(R.layout.fragment_schedule, container, false);
        initializeRecyclerView(rootFragmentView);
        return rootFragmentView;
    }

    private void initializeRecyclerView(View rootFragmentView) {
        mRecyclerView = rootFragmentView.findViewById(R.id.schedule_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        if(mAdapter == null) {
            mAdapter = new ScheduleAdapter(schedule);
            return;
        }
        mAdapter.updateSchedule(schedule);
    }
}