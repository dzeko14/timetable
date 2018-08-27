package my.dzeko.timetable.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.CalendarAdapter;
import my.dzeko.timetable.contracts.CalendarContract;
import my.dzeko.timetable.presenters.CalendarPresenter;

public class CalendarFragment extends Fragment implements CalendarContract.View {
    CalendarContract.Presenter mPresenter;

    private ViewPager mViewPager;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment getInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CalendarPresenter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        initializeViews(rootView);
        initializeViewPager();
        mPresenter.onViewInitialized();
        return rootView;
    }

    private void initializeViewPager() {
        mViewPager.setAdapter(new CalendarAdapter(getFragmentManager()));
    }

    private void initializeViews(View rootView) {
        mViewPager = rootView.findViewById(R.id.calendar_view_pager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setMonth(int month) {
        mViewPager.setCurrentItem(month);
    }
}
