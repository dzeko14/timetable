package my.dzeko.timetable.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.CalendarPagerAdapter;
import my.dzeko.timetable.contracts.CalendarContract;
import my.dzeko.timetable.presenters.CalendarPresenter;

public class CalendarFragment extends Fragment implements CalendarContract.View {
    CalendarContract.Presenter mPresenter;

    private ViewPager mViewPager;
    private ProgressBar mProgressBar;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment getInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPresenter = new CalendarPresenter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        initializeViews(rootView);
        createViewPager(rootView);
        return rootView;
    }

    private void createViewPager(View rootView) {
        Context context = this.getContext();
        if(context == null) return;

        new AsyncLayoutInflater(context).inflate(R.layout.calendar_view_pager,
                (ViewGroup) rootView,
                new AsyncLayoutInflater.OnInflateFinishedListener() {
                    @Override
                    public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                        assert parent != null;
                        parent.addView(view);
                        mViewPager = (ViewPager) view;
                        mViewPager.setAdapter(new CalendarPagerAdapter(getFragmentManager()));
                        mPresenter.onViewInitialized();
                    }
                });
    }

    private void initializeViews(View rootView) {
        mProgressBar = rootView.findViewById(R.id.calendar_progress_bar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setMonth(int month) {
        mViewPager.setCurrentItem(month);
    }
}
