package my.dzeko.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Spinner mTypeSpinner;

    private static final String SUBJECT_NAME = "subject name";
    private static final String SUBJECT_FULL_NAME = "subject full name";
    private static final String SUBJECT_CABINET = "subject cabinet";
    private static final String SUBJECT_TEACHER = "subject teacher";
    private static final String SUBJECT_POSITION = "subject position";
    private static final String SUBJECT_TYPE = "subject type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_subject);
        setTitle(R.string.create_or_update_subject_activity);
        findViews();
        mPresenter.onRestoreInstanceState(savedInstanceState);
        getDataFromIntent();

    }

    private void findViews() {
        mSubjectCabinetEditText = findViewById(R.id.activity_create_or_update_subject_cabinet_edit_text);
        mSubjectNameEditText = findViewById(R.id.activity_create_or_update_subject_name_edit_text);
        mSubjectFullNameEditText = findViewById(R.id.activity_create_or_update_subject_full_name_edit_text);
        mSubjectTeacherEditText = findViewById(R.id.activity_create_or_update_subject_teacher_edit_text);
        mPositionSpinner = findViewById(R.id.activity_create_or_update_subject_position_spinner);
        mTypeSpinner = findViewById(R.id.activity_create_or_update_subject_type_spinner);
        (findViewById(R.id.submit_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onUserClick(v.getId());
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        long subjectId = intent.getLongExtra(CreateOrUpdateSubjectContract.SUBJECT_ID, -1);
        int dayId = intent.getIntExtra(CreateOrUpdateSubjectContract.DAY_ID, -1);
        int weekId = intent.getIntExtra(CreateOrUpdateSubjectContract.WEEK_ID, -1);
        mPresenter.onDataReceived(dayId, weekId, subjectId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSubjectNameEditText.setText(savedInstanceState.getString(SUBJECT_NAME));
        mSubjectFullNameEditText.setText(savedInstanceState.getString(SUBJECT_FULL_NAME));
        mSubjectCabinetEditText.setText(savedInstanceState.getString(SUBJECT_CABINET));
        mSubjectTeacherEditText.setText(savedInstanceState.getString(SUBJECT_TEACHER));
        mPositionSpinner.setSelection(savedInstanceState.getInt(SUBJECT_POSITION));
        mTypeSpinner.setSelection(savedInstanceState.getInt(SUBJECT_TYPE));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SUBJECT_NAME, mSubjectNameEditText.getText().toString());
        outState.putString(SUBJECT_FULL_NAME, mSubjectFullNameEditText.getText().toString());
        outState.putString(SUBJECT_CABINET, mSubjectCabinetEditText.getText().toString());
        outState.putString(SUBJECT_TEACHER, mSubjectTeacherEditText.getText().toString());
        outState.putInt(SUBJECT_POSITION, mPositionSpinner.getSelectedItemPosition());
        outState.putInt(SUBJECT_TYPE, mTypeSpinner.getSelectedItemPosition());
        outState.putAll(mPresenter.saveState());
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
        String type = getTypeString(mTypeSpinner.getSelectedItemPosition());
        mPresenter.onDataSaving(name, fullName, cabinet, teacher, position, type);
        finish();
    }

    private String getTypeString(int position) {
        String[] types = getResources().getStringArray(R.array.subject_types);
        if (position == 0) return null;
        return types[position];
    }

    @Override
    public void setSubject(Subject subject) {
        mSubjectNameEditText.setText(subject.getSubjectName());
        mSubjectFullNameEditText.setText(subject.getFullSubjectName());
        mSubjectTeacherEditText.setText(subject.getTeacher());
        mSubjectCabinetEditText.setText(subject.getCabinet());
        mPositionSpinner.setSelection(subject.getPosition() - 1);
        mTypeSpinner.setSelection(getTypeInt(subject.getType()));
    }

    private int getTypeInt(String type){
        if (type == null) return 0;
        String[] types = getResources().getStringArray(R.array.subject_types);
        for (int i = 0; i < types.length; i++) {
            if (type.equals(types[i])) return i;
        }

        return 0;
    }

}
