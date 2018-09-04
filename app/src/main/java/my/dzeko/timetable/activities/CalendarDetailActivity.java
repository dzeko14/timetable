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
    CalendarDetailContract.Presenter mPresenter;

    ViewGroup mDayScrollView;
    ProgressBar mProgressBar;
    ViewGroup[] mSubjectsViewGroups = new ViewGroup[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_detail);

        initializeViews();
        initializePresenter();

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
    }

    private void initializePresenter() {
        mPresenter = new CalendarDetailPresenter(this);
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
            mCabinet.setText(subject.getCabinet());

            TextView mTeacher = v.findViewById(R.id.tv_teacher);
            mTeacher.setText(subject.getTeacher());
        }

        for (int i = viewGroupIndex; i < 5; i++) {
            mSubjectsViewGroups[i].setVisibility(View.GONE);
        }
    }

    @Override
    public void setActionBarTitle(String title) {
        setTitle(title);
    }
}
