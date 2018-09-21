package my.dzeko.timetable.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.EditingPagerAdapter;
import my.dzeko.timetable.contracts.EditingContract;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.presenters.EditingPresenter;

public class EditingFragment extends Fragment implements EditingContract.View {
    EditingContract.Presenter mPresenter;

    private ProgressBar mProgressBar;
    private ViewPager mViewPager;
    private LinearLayout mContentLinearLayout;
    private TabLayout mTabLayout;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editing, container, false);
        findViews(rootView);
        initializeTabs();
        mPresenter.onViewCreated();
        return rootView;
    }

    private void initializeTabs() {
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    private void findViews(View rootView) {
        mProgressBar = rootView.findViewById(R.id.editing_progress_bar);
        mViewPager = rootView.findViewById(R.id.editing_view_pager);
        mContentLinearLayout = rootView.findViewById(R.id.editing_fragment_content_layout);
        mTabLayout = rootView.findViewById(R.id.editing_fragment_tab_layout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mContentLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
        mContentLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addWeekToAdapter(Week week, String title) {
        mPagerAdapter.addWeek(week, title);
    }

    @Override
    public void setupAdapter() {
        mViewPager.setAdapter(mPagerAdapter);
    }
}
