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
import my.dzeko.timetable.adapters.ScheduleRecyclerAdapter;
import my.dzeko.timetable.contracts.ScheduleContract;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.presenters.SchedulePresenter;

/**
 * Fragment is used as View in MVP pattern
 */
public class ScheduleFragment extends Fragment implements ScheduleContract.View {
    ScheduleContract.Presenter mPresenter;
    ScheduleRecyclerAdapter mAdapter;
    RecyclerView mRecyclerView;

    View mNoScheduleIV;
    View mNoScheduleTV;

    View mProgressBar;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootFragmentView = inflater.inflate(R.layout.fragment_schedule, container, false);
        findViews(rootFragmentView);
        initializeRecyclerView();
        loadSchedule();
        return rootFragmentView;
    }

    private void findViews(View rootFragmentView) {
        mNoScheduleIV = rootFragmentView.findViewById(R.id.no_schedule_image_view);
        mNoScheduleTV = rootFragmentView.findViewById(R.id.no_schedule_text_view);
        mRecyclerView = rootFragmentView.findViewById(R.id.schedule_recycler_view);
        mProgressBar = rootFragmentView.findViewById(R.id.load_progress_bar_schedule);
    }

    private void loadSchedule() {
        mPresenter.onScheduleRequest();
    }

    private void initializeRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ScheduleRecyclerAdapter(Schedule.getEmptySchedule());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        mAdapter.updateSchedule(schedule);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollToCurrentDay() {
        mRecyclerView.scrollToPosition(mAdapter.getCurrentDayViewHolderPosition());
    }

    @Override
    public void showEmptyScreen() {
        mRecyclerView.setVisibility(View.GONE);
        mNoScheduleIV.setVisibility(View.VISIBLE);
        mNoScheduleTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyScreen() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mNoScheduleIV.setVisibility(View.GONE);
        mNoScheduleTV.setVisibility(View.GONE);
    }
}
