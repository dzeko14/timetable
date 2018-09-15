package my.dzeko.timetable.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import my.dzeko.timetable.api.ApiBuilder;
import my.dzeko.timetable.db.SubjectDao;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.entities.ScheduleApiRespond;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.entities.WeekApiRespond;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.observers.GroupObservable;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.wrappers.DatabaseWrapper;
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper;
import retrofit2.Call;

public abstract class ApiUtils {
    public static void parseSchedule(String groupName) throws IOException, ParseException {
        Call<ScheduleApiRespond> scheduleCall = ApiBuilder.buildGetGroupScheduleServiceObservable(groupName);
        SubjectDao subjectDao = DatabaseWrapper.getDatabase().getSubjectDao();

        //Parse schedule
        List<Subject> subjectList = scheduleCall.execute().body().getData();

        //Deleting old schedule of this group from db
        subjectDao.deleteSubjectsByGroupName(groupName);

        List<Group> groupList = Model.getInstance().getGroupList();
        boolean isGroupAdded = false;

        for (Group group : groupList) {
            if (group.getName().equals(groupName)) {
                isGroupAdded = true;
                break;
            }
        }

        if (!isGroupAdded) {
            GroupObservable.getInstance().notifyGroupAdded(groupName);
        }

        for (Subject subject: subjectList) {
            subject.setGroup(groupName);
            long id = subjectDao.saveSubject(subject);
            subject.setId(id);
        }

        Schedule schedule = ScheduleUtils.fetchScheduleFromList(subjectList, groupName);
        ScheduleObservable.getInstance().notifySelectedScheduleChanged(schedule);
    }

    public static void parseCurrentWeek() throws IOException {
        Call<WeekApiRespond> weekCall = ApiBuilder.buildGetCurrentWeekServiceObservable();
        int currentWeekNumber = weekCall.execute().body().getData();
        SharedPreferencesWrapper.getInstance().setKeyDate(
                DateUtils.createKeyDate(currentWeekNumber == 1)
        );
    }
}
