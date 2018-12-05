package my.dzeko.timetable.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.dzeko.timetable.R;
import my.dzeko.timetable.adapters.EditWeekRecyclerAdapter;
import my.dzeko.timetable.contracts.CreateOrUpdateSubjectContract;
import my.dzeko.timetable.contracts.EditWeekContract;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.presenters.EditWeekPresenter;

public class EditWeekFragment extends Fragment implements EditWeekContract.View {
    private EditWeekContract.Presenter mPresenter = new EditWeekPresenter(this);
    private RecyclerView mRecyclerView;
    public static final String WEEK = "week";

    public EditWeekFragment() {
        // Required empty public constructor
    }

    public static EditWeekFragment getInstance() {
        return new EditWeekFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setPresenterArguments();
    }

    private void setPresenterArguments() {
        Bundle bundle = getArguments();
        mPresenter.setWeek((Week) bundle.getParcelable(WEEK));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_week, container, false);
        findViews(rootView);
        setupRecyclerView(rootView.getContext());
        mPresenter.onViewInitialized();
        return rootView;
    }

    private void setupRecyclerView(Context context) {
        LinearLayoutManager llmanager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(llmanager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    private void findViews(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void updateAdapter() {
        ((EditWeekRecyclerAdapter) mRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void updateAdapter(Week week) {
        ((EditWeekRecyclerAdapter) mRecyclerView.getAdapter()).updateData(week);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void startActivity(Bundle bundle, Class c) {
        Intent intent = new Intent(this.getContext(), c);
        intent.putExtra(CreateOrUpdateSubjectContract.SUBJECT_ID,
                bundle.getLong(CreateOrUpdateSubjectContract.SUBJECT_ID));
        intent.putExtra(CreateOrUpdateSubjectContract.DAY_ID,
                bundle.getInt(CreateOrUpdateSubjectContract.DAY_ID));
        intent.putExtra(CreateOrUpdateSubjectContract.WEEK_ID,
                bundle.getInt(CreateOrUpdateSubjectContract.WEEK_ID));
        startActivity(intent);
    }
}
