package my.dzeko.timetable.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.CalendarUnitContract;
import my.dzeko.timetable.presenters.CalendarUnitPresenter;

public class CalendarUnitFragment extends Fragment implements CalendarUnitContract.View {
    public static String MONTH_NUMBER;

    public CalendarUnitContract.Presenter mPresenter;

    public Bundle mArgsBundle;

    public ViewGroup mTableViewGroup;
    public TextView mMonthNameTextView;

    public CalendarUnitFragment() {
        // Required empty public constructor
    }

    public void setArgs(Bundle bundle) {
        mArgsBundle = bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializePresenter();
    }

    private void initializePresenter() {
        int month = mArgsBundle.getInt(MONTH_NUMBER);
        mPresenter = new CalendarUnitPresenter(this, month);
        mArgsBundle = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar_unit, container, false);
        initializeViews(rootView);
        initializeHandlersForDays();
        mPresenter.onCalendarInitialization();
        return rootView;
    }

    private void initializeHandlersForDays() {
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                Button day = (Button) ((ViewGroup)mTableViewGroup.getChildAt(i)).getChildAt(j);
                day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int day = Integer.valueOf(((Button)v).getText().toString());
                        mPresenter.onCalendarUserClick(day);
                    }
                });
            }
        }
    }

    private void initializeViews(View rootView) {
        mMonthNameTextView = rootView.findViewById(R.id.month_fragment_calendar_unit);
        mTableViewGroup = rootView.findViewById(R.id.calendar_fragment_calendar_unit);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void setDayNumber(int dayPositionInWeek, int week, int number) {
        Button day = (Button) ((ViewGroup)mTableViewGroup.getChildAt(week))
                .getChildAt(dayPositionInWeek);
        day.setText(String.valueOf(number));
        day.setEnabled(true);
    }

    @Override
    public void setMonthName(String monthName) {
        mMonthNameTextView.setText(monthName);
    }

    @Override
    public void hideWeekRow(int week) {
        mTableViewGroup.getChildAt(week).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
