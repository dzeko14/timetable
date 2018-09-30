package my.dzeko.timetable.contracts;

import android.os.Bundle;

import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;

public class CreateOrUpdateSubjectContract {
    public static final String SUBJECT_ID = "subject_id";
    public static final String DAY_ID = "day_id";
    public static final String WEEK_ID = "week_id";

    public interface View extends IView {
        void setSubject(Subject subject);
        void saveData();
    }

    public interface Presenter extends IPresenter {
        void onDataSaving(String name, String fullName, String cabinet, String teacher, int position);
        void onDataReceived(int dayId, int weekId, long subjectId);

        void onRestoreInstanceState(Bundle savedInstanceState);

        Bundle saveState();
    }
}
