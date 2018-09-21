package my.dzeko.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.CreateOrUpdateSubjectContract;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.presenters.CreateOrUpdateSubjectPresenter;

public class CreateOrUpdateSubjectActivity extends AppCompatActivity
        implements CreateOrUpdateSubjectContract.View {
    private CreateOrUpdateSubjectContract.Presenter mPresenter = new CreateOrUpdateSubjectPresenter(this);

    private EditText mSubjectNameEditText;
    private EditText mSubjectFullNameEditText;
    private EditText mSubjectCabinetEditText;
    private EditText mSubjectTeacherEditText;
    private Spinner mPositionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_subject);
        findViews();
        getDataFromIntent();
    }

    private void findViews() {
        mSubjectCabinetEditText = findViewById(R.id.activity_create_or_update_subject_cabinet_edit_text);
        mSubjectNameEditText = findViewById(R.id.activity_create_or_update_subject_name_edit_text);
        mSubjectFullNameEditText = findViewById(R.id.activity_create_or_update_subject_full_name_edit_text);
        mSubjectTeacherEditText = findViewById(R.id.activity_create_or_update_subject_teacher_edit_text);
        mPositionSpinner = findViewById(R.id.activity_create_or_update_subject_position_spinner);
    }

    public void getDataFromIntent() {
        Intent intent = getIntent();
        long subjectId = intent.getLongExtra(CreateOrUpdateSubjectContract.SUBJECT_ID, -1);
        int dayId = intent.getIntExtra(CreateOrUpdateSubjectContract.DAY_ID, -1);
        int weekId = intent.getIntExtra(CreateOrUpdateSubjectContract.WEEK_ID, -1);
        mPresenter.onDataReceived(dayId, weekId, subjectId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_or_update_subject_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mPresenter.onUserClick(item.getItemId()) | super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
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
    public Context getContext() {
        return this;
    }

    @Override
    public void saveData() {
        String name = mSubjectNameEditText.getText().toString();
        String fullName = mSubjectFullNameEditText.getText().toString();
        String cabinet = mSubjectCabinetEditText.getText().toString();
        String teacher = mSubjectTeacherEditText.getText().toString();
        int position = mPositionSpinner.getSelectedItemPosition() + 1;
        mPresenter.onDataSaving(name, fullName, cabinet, teacher, position);
        finish();
    }

    @Override
    public void setSubject(Subject subject) {
        mSubjectNameEditText.setText(subject.getSubjectName());
        mSubjectFullNameEditText.setText(subject.getFullSubjectName());
        mSubjectTeacherEditText.setText(subject.getTeacher());
        mSubjectCabinetEditText.setText(subject.getCabinet());
        mPositionSpinner.setSelection(subject.getPosition() - 1);
    }
}
