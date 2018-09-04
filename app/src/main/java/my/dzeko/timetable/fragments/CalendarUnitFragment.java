package my.dzeko.timetable.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.CalendarDetailContract;
import my.dzeko.timetable.contracts.CalendarUnitContract;
import my.dzeko.timetable.presenters.CalendarUnitPresenter;
import my.dzeko.timetable.ui.views.SquareTextView;

public class CalendarUnitFragment extends Fragment implements CalendarUnitContract.View {
    public static String MONTH_NUMBER;

    private CalendarUnitContract.Presenter mPresenter;

    private Bundle mArgsBundle;

    private TextView mMonthNameTextView;
    private ViewGroup[] mDaysNumberRows = new ViewGroup[6];

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
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                SquareTextView day = (SquareTextView) mDaysNumberRows[i].getChildAt(j);
                day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int day = Integer.valueOf(((SquareTextView)v).getText().toString());
                        mPresenter.onCalendarUserClick(day);
                    }
                });
            }
        }
    }

    private void initializeViews(View rootView) {
        mMonthNameTextView = rootView.findViewById(R.id.month_fragment_calendar_unit);

        mDaysNumberRows[0] = rootView.findViewById(R.id.calendar_day_numbers_1);
        mDaysNumberRows[1] = rootView.findViewById(R.id.calendar_day_numbers_2);
        mDaysNumberRows[2] = rootView.findViewById(R.id.calendar_day_numbers_3);
        mDaysNumberRows[3] = rootView.findViewById(R.id.calendar_day_numbers_4);
        mDaysNumberRows[4] = rootView.findViewById(R.id.calendar_day_numbers_5);
        mDaysNumberRows[5] = rootView.findViewById(R.id.calendar_day_numbers_6);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void setDayNumber(int dayPositionInWeek, int week, int number) {
        SquareTextView day = (SquareTextView) mDaysNumberRows[week].getChildAt(dayPositionInWeek);
        day.setText(String.valueOf(number));
        day.setEnabled(true);
    }

    @Override
    public void setMonthName(String monthName) {
        mMonthNameTextView.setText(monthName);
    }

    @Override
    public void hideWeekRow(int week) {
        mDaysNumberRows[week].setVisibility(View.INVISIBLE);
    }

    @Override
    public void setSelectedMonth(int dayPositionInWeek, int week) {
        mDaysNumberRows[week].getChildAt(dayPositionInWeek).setSelected(true);
    }

    @Override
    public void startActivity(Class c, Date date) {
        Intent intent = new Intent(this.getContext(), c);
        intent.putExtra(CalendarDetailContract.CLICKED_DATE, date);
        startActivity(intent);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
