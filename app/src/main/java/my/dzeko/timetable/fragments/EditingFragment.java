package my.dzeko.timetable.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.EditingPagerAdapter;
import my.dzeko.timetable.contracts.EditingContract;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.presenters.EditingPresenter;

public class EditingFragment extends Fragment implements EditingContract.View {
    EditingContract.Presenter mPresenter;

    private ProgressBar mProgressBar;
    private ViewPager mViewPager;

    private EditingPagerAdapter mPagerAdapter;


    public EditingFragment() {
        // Required empty public constructor
    }

    public static  EditingFragment getInstance() {
        return new EditingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializePresenter();
        initializeAdapter();
    }

    private void initializeAdapter() {
        mPagerAdapter = new EditingPagerAdapter(getFragmentManager());
    }

    private void initializePresenter() {
        mPresenter = new EditingPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editing, container, false);
        initializeView(rootView);
        mPresenter.onViewCreated();
        return rootView;
    }

    private void initializeView(View rootView) {
        mProgressBar = rootView.findViewById(R.id.editing_progress_bar);
        mViewPager = rootView.findViewById(R.id.editing_view_pager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void addWeekToAdapter(List<Day> week) {
        mPagerAdapter.addWeek(week);
    }

    @Override
    public void setupAdapter() {
        mViewPager.setAdapter(mPagerAdapter);
    }
}
