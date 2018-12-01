package my.dzeko.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.CalendarDetailContract;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.presenters.CalendarDetailPresenter;

public class CalendarDetailActivity extends AppCompatActivity implements CalendarDetailContract.View {
    CalendarDetailContract.Presenter mPresenter = new CalendarDetailPresenter(this);

    ViewGroup mDayScrollView;
    ProgressBar mProgressBar;
    ViewGroup[] mSubjectsViewGroups = new ViewGroup[5];
    View mNoScheduleIV;
    View mNoScheduleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);

        initializeViews();

        mPresenter.findRespectSubjects(getDate());
    }

    private Date getDate() {
        Intent intent = getIntent();
        return (Date) intent.getExtras().get(CalendarDetailContract.CLICKED_DATE);
    }

    private void initializeViews() {
        mDayScrollView = findViewById(R.id.day_calendar_detail);
        mProgressBar = findViewById(R.id.progress_bar_calendar_detail);
        mSubjectsViewGroups[0] = findViewById(R.id.subject_1_calendar_detail_activity);
        mSubjectsViewGroups[1] = findViewById(R.id.subject_2_calendar_detail_activity);
        mSubjectsViewGroups[2] = findViewById(R.id.subject_3_calendar_detail_activity);
        mSubjectsViewGroups[3] = findViewById(R.id.subject_4_calendar_detail_activity);
        mSubjectsViewGroups[4] = findViewById(R.id.subject_5_calendar_detail_activity);
        mNoScheduleIV = findViewById(R.id.no_schedule_image_view);
        mNoScheduleTV = findViewById(R.id.no_schedule_text_view);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mDayScrollView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
        mDayScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setupSubjects(List<Subject> subjects) {
        int viewGroupIndex = 0;
        for (Subject subject : subjects) {
            ViewGroup v = mSubjectsViewGroups[viewGroupIndex++];

            TextView mNumber = v.findViewById(R.id.tv_number);
            mNumber.setText(String.valueOf(subject.getPosition()));

            TextView mName = v.findViewById(R.id.tv_subject);
            mName.setText(subject.getFullSubjectName());

            TextView mCabinet = v.findViewById(R.id.tv_cabinet);
            String cabinet = (subject.getCabinet() + " " + nullString(subject.getType())).trim();
            mCabinet.setText(cabinet);

            TextView mTeacher = v.findViewById(R.id.tv_teacher);
            mTeacher.setText(subject.getTeacher());
        }

        for (int i = viewGroupIndex; i < 5; i++) {
            mSubjectsViewGroups[i].setVisibility(View.GONE);
        }
    }

    private String nullString(String str){
        if (str == null) return "";
        return str;
    }

    @Override
    public void showEmptyScreen() {
        mDayScrollView.setVisibility(View.GONE);
        mNoScheduleTV.setVisibility(View.VISIBLE);
        mNoScheduleIV.setVisibility(View.VISIBLE);
        for (ViewGroup v :
                mSubjectsViewGroups) {
            v.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideEmptyScreen() {
        mDayScrollView.setVisibility(View.VISIBLE);
        mNoScheduleTV.setVisibility(View.GONE);
        mNoScheduleIV.setVisibility(View.GONE);
        for (ViewGroup v :
                mSubjectsViewGroups) {
            v.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setActionBarTitle(String title) {
        setTitle(title);
    }
}
