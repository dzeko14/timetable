package my.dzeko.timetable.models;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
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

    private boolean mIsCurrentSchedule = false;

    @Override
    public List<Group> getGroupList() {
       return DatabaseWrapper.getDatabase().getGroupDao().getAllGroups();
    }

    @Override
    public Schedule getSelectedSchedule() {
        String selectedSchedule = SharedPreferencesWrapper.getInstance().getSelectedGroup();

        //get selected schedule from DB
        try {
            Schedule schedule = ScheduleUtils.fetchScheduleFromList(
                    DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByGroupName(selectedSchedule),
                    selectedSchedule
            );
            mIsCurrentSchedule = schedule.isSingleWeek();
            return schedule;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void selectSchedule(String groupName) {
        SharedPreferencesWrapper.getInstance().setSelectedGroup(groupName);
        ScheduleObservable observable = ScheduleObservable.getInstance();

        Schedule schedule = new Schedule(groupName, new ArrayList<Day>());

        //Else get selected schedule from DB
        SubjectDao subjectDao = DatabaseWrapper.getDatabase().getSubjectDao();
        try {
            schedule = ScheduleUtils.fetchScheduleFromList(
                    subjectDao.getSubjectsByGroupName(groupName), groupName);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mIsCurrentSchedule = schedule.isSingleWeek();

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
            mIsCurrentSchedule = ApiUtils.parseSchedule(groupName).isSingleWeek();
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
    public List<Subject> getSubjectsByDayIdAndWeekId(int dayId, int weekId, String groupName) {
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
        Collections.sort(subjects);

        for (Subject s: subjects) {
            if (subject.getId() == s.getId()) {
                oldPosition = s.getPosition();
            }
            if (s.getPosition() == subject.getPosition()) {
                subjectPositionToChange = s;
            }
        }

        if (oldPosition == -1) {
            oldPosition = 1;
            for (Subject s: subjects) {
                if (s.getPosition() == oldPosition) oldPosition++;
                else break;
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
        Group sameGroup = groupDao.getGroupByName(groupName);
        if (sameGroup == null) {
            saveGroupName(groupName);
            GroupObservable.getInstance().notifyGroupAdded(groupName);
            ScheduleObservable.getInstance()
                    .notifySelectedScheduleChanged(ScheduleUtils.createEmptySchedule(groupName));
        }
    }

    @Override
    public List<Subject> getSubjectsByDayId(int dayId, String groupName) {
        return DatabaseWrapper.getDatabase().getSubjectDao().getSubjectsByDayId(dayId, groupName);
    }

    @Override
    public boolean getIsSelectedScheduleSingleWeek() {
        return mIsCurrentSchedule;
    }
}
