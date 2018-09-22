package my.dzeko.timetable.models;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import my.dzeko.timetable.db.GroupDao;
import my.dzeko.timetable.db.SubjectDao;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.observers.GroupObservable;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.utils.ApiUtils;
import my.dzeko.timetable.utils.ScheduleUtils;
import my.dzeko.timetable.wrappers.DatabaseWrapper;
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper;

public class Model implements IModel{
//  --------------------------------------------------------
    private static Model mInstance;
    private Model() { }
    public static Model getInstance() {
        if(mInstance == null) {
            mInstance = new Model();
        }
        return mInstance;
    }
//  --------------------------------------------------------

    @Override
    public List<Group> getGroupList() {
       return DatabaseWrapper.getDatabase().getGroupDao().getAllGroups();
    }

    @Override
    public Schedule getSelectedSchedule() {
        String selectedSchedule = SharedPreferencesWrapper.getInstance().getSelectedGroup();

        //get selected schedule from DB
        try {
            return ScheduleUtils.fetchScheduleFromList(
                    DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByGroupName(selectedSchedule),
                    selectedSchedule
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void selectSchedule(String groupName) {
        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);
        ScheduleObservable observable = ScheduleObservable.getInstance();

        Schedule schedule = null;

            //Else get selected schedule from DB
            SubjectDao subjectDao = DatabaseWrapper.getDatabase().getSubjectDao();
            try {
                schedule = ScheduleUtils.fetchScheduleFromList(
                        subjectDao.getSubjectsByGroupName(groupName), groupName);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        observable.notifySelectedScheduleChanged(schedule);
    }

    @Override
    public String getSelectedScheduleGroupName() {
        return SharedPreferencesWrapper.getInstance().getSelectedGroup();
    }

    @Override
    public void parseSchedule(String groupName) {
        try {
            ApiUtils.parseCurrentWeek();
            ApiUtils.parseSchedule(groupName);
            saveGroupName(groupName);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO Implement on error handling
        } catch (ParseException e) {
            e.printStackTrace();
            //TODO Implement on error handling
        }
    }

    private void saveGroupName(String groupName) {
        GroupDao groupDao = DatabaseWrapper.getDatabase().getGroupDao();
        groupDao.saveGroup(new Group(groupName));
        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);
    }

    @Override
    public void saveCurrentBottomNavigationFragment(int fragmentId) {
        SharedPreferencesWrapper.getInstance().setSelectedFragment(fragmentId);
    }

    @Override
    public int getCurrentBottomNavigationFragment() {
        return SharedPreferencesWrapper.getInstance().getSelectedFragmentId();
    }

    @Override
    public List<Subject> getSchedulesByDayIdAndWeekId(int dayId, int weekId, String groupName) {
        return DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByDayIdAndWeekId(dayId,
                weekId, groupName);
    }

    @Override
    public void removeSubject(Subject subject) {
        DatabaseWrapper.getDatabase().getSubjectDao().removeSubject(subject);
        ScheduleObservable.getInstance().notifySelectedScheduleChanged(getSelectedSchedule());
    }

    @Override
    public void removeDay(Day day) {
        SubjectDao dao = DatabaseWrapper.getDatabase().getSubjectDao();
        for (Subject subject : day.getSubjects()) {
            dao.removeSubject(subject);
        }
        ScheduleObservable.getInstance().notifySelectedScheduleChanged(getSelectedSchedule());
    }

    @Override
    public Subject getSubject(long subjectId) {
        return DatabaseWrapper.getDatabase().getSubjectDao().getSubjectById(subjectId);
    }

    @Override
    public void saveSubject(Subject subject) {
        SubjectDao subjectDao = DatabaseWrapper.getDatabase().getSubjectDao();
        Subject subjectPositionToChange = null;
        int oldPosition = -1;
        List<Subject> subjects = subjectDao.getSubjectsByDayIdAndWeekId(subject.getDayId(),
                subject.getWeekId(), getSelectedScheduleGroupName());

        for (Subject s: subjects) {
            if (subject.getId() == s.getId()) {
                oldPosition = s.getPosition();
            }
            if (s.getPosition() == subject.getPosition()) {
                subjectPositionToChange = s;
            }
        }

        if (subjectPositionToChange != null) {
            subjectPositionToChange.setPosition(oldPosition);
            subjectDao.saveSubject(subjectPositionToChange);
        }

        subjectDao.saveSubject(subject);
        ScheduleObservable.getInstance().notifySelectedScheduleChanged(getSelectedSchedule());
    }

    @Override
    public void saveGroup(String groupName) {
        GroupDao groupDao = DatabaseWrapper.getDatabase().getGroupDao();
        if (!groupDao.getGroupByName(groupName).getName().equals(groupName)) {
            saveGroupName(groupName);
            GroupObservable.getInstance().notifyGroupAdded(groupName);
            ScheduleObservable.getInstance()
                    .notifySelectedScheduleChanged(ScheduleUtils.createEmptySchedule(groupName));
        }
    }
}
