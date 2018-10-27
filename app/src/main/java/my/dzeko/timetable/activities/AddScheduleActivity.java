package my.dzeko.timetable.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.AddScheduleContract;
import my.dzeko.timetable.presenters.AddSchedulePresenter;
import my.dzeko.timetable.services.ParseScheduleService;

public class AddScheduleActivity extends AppCompatActivity implements AddScheduleContract.View {
    private AddScheduleContract.Presenter mPresenter = new AddSchedulePresenter(this);

    private EditText mGroupNameEditText;
    private View mConfirmGroupNameButton;
    private View mCreateGroupScheduleButton;
    private View mProgressBar;

    private static final String GROUP_NAME = "group name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        initializeViewComponent();
    }

    private void initializeViewComponent() {
        mGroupNameEditText = findViewById(R.id.groupName_editText_addScheduleActivity);
        mConfirmGroupNameButton = findViewById(R.id.confirmGroupName_button_addGroupActivity);
        mCreateGroupScheduleButton = findViewById(R.id.createGroup_button_addGroupActivity);
        mProgressBar = findViewById(R.id.loading_progressBar_addScheduleActivity);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GROUP_NAME, mGroupNameEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGroupNameEditText.setText(savedInstanceState.getString(GROUP_NAME));
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
    public Context getContext() {
        return getApplicationContext();
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
        finish();
    }

    @Override
    public void startService(String groupName, Class c) {
        Intent intent = new Intent(this, c);
        intent.putExtra(ParseScheduleService.PARSING_GROUP_NAME, groupName);

        startService(intent);
    }

    @Override
    public void notifyScheduleCreated() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showChoseWeekNumberDialog() {
        String[] items = getContext().getResources().getStringArray(R.array.weeks);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pick_current_week_title)
                .setSingleChoiceItems(items, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.setCurrentWeek(which);
                            }
                        })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.createGroupSchedule();
            }
        })
                .setNegativeButton(android.R.string.cancel, new EmptyClickListener())
                .create()
                .show();
    }

    private class EmptyClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //Empty
        }
    }
}
