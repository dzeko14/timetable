package my.dzeko.timetable.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.AddScheduleContract;
import my.dzeko.timetable.presenters.AddSchedulePresenter;

public class AddScheduleActivity extends AppCompatActivity implements AddScheduleContract.View {
    private AddScheduleContract.Presenter mPresenter;

    private EditText mGroupNameEditText;
    private View mConfirmGroupNameButton;
    private View mCreateGroupScheduleButton;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        initializePresenter();
        initializeViewComponent();
    }

    private void initializeViewComponent() {
        mGroupNameEditText = findViewById(R.id.groupName_editText_addScheduleActivity);
        mConfirmGroupNameButton = findViewById(R.id.confirmGroupName_button_addGroupActivity);
        mCreateGroupScheduleButton = findViewById(R.id.createGroup_button_addGroupActivity);
        mProgressBar = findViewById(R.id.loading_progressBar_addScheduleActivity);
    }

    private void initializePresenter() {
        mPresenter = new AddSchedulePresenter(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    public void onClick(View v) {
        mPresenter.onUserClick(v.getId());
    }

    @Override
    public void showLoading() {
        mGroupNameEditText.setVisibility(View.INVISIBLE);
        mConfirmGroupNameButton.setVisibility(View.INVISIBLE);
        mCreateGroupScheduleButton.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mGroupNameEditText.setVisibility(View.VISIBLE);
        mConfirmGroupNameButton.setVisibility(View.VISIBLE);
        mCreateGroupScheduleButton.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getGroupName() {
        return mGroupNameEditText.getText().toString();
    }

    @Override
    public void showEmptyGroupName() {
        Toast.makeText(this, "Введіть назву групи", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close() {
        onBackPressed();
    }
}
