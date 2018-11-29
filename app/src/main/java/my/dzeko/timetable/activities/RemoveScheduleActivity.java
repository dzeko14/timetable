package my.dzeko.timetable.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private RemoveScheduleAdapter mAdapter;

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
        mAdapter = new RemoveScheduleAdapter(new ArrayList<Group>());
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mPresenter.onRemoveScheduleClick(mAdapter.getGroupName(viewHolder.getItemId()));
            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(mRecyclerView);
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
        mAdapter = new RemoveScheduleAdapter(groups);
        mRecyclerView.setAdapter(mAdapter);
    }
}
