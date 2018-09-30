package my.dzeko.timetable.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.RemoveScheduleAdapter;
import my.dzeko.timetable.contracts.RemoveScheduleContract;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.presenters.RemoveSchedulePresenter;

public class RemoveScheduleActivity extends AppCompatActivity implements RemoveScheduleContract.View {
    private RemoveScheduleContract.Presenter mPresenter = new RemoveSchedulePresenter(this);

    private RecyclerView mRecyclerView;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_schedule);
        findViews();
        setupRecyclerView();
        mPresenter.onGroupListRequest();
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new RemoveScheduleAdapter(new ArrayList<Group>(), mPresenter));
    }

    private void findViews() {
        mRecyclerView = findViewById(R.id.remove_schedule_recycler_view);
        mProgressBar = findViewById(R.id.remove_schedule_progress_bar);
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
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setGroupList(List<Group> groups) {
        mRecyclerView.setAdapter(new RemoveScheduleAdapter(groups, mPresenter));
    }
}
