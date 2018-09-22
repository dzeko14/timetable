package my.dzeko.timetable.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import my.dzeko.timetable.R;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Week;

public abstract class WeekUtils {
    public static Week createWeek(Context context, int weekId) {
        Resources resources = context.getResources();
        String[] dayOfWeekNames = resources.getStringArray(R.array.day_of_weeks_names);
        String weekName;

        if (weekId == 1) {
            weekName = resources.getString(R.string.first_week);
        } else {
            weekName = resources.getString(R.string.second_week);
        }

        List<Day> days = new ArrayList<>(6);
        for (int i = 1; i < 7; i++) {
            days.add(new Day(
                    i,
                    weekId ,
                    dayOfWeekNames[i - 1],
                    "",
                    weekName,
                    false
            ));
        }
        return new Week(days, weekId);
    }

    public static void updateWeek(Week weekToUpdate, Week newWeek) {
        List<Integer> list = new LinkedList<>(Arrays.asList(0,1,2,3,4,5));

        for (Day day : newWeek.getDaysList()) {
            Integer index = day.getId() - 1;
            weekToUpdate.getDaysList().set(index, day);
            list.remove(index);
        }

        for (Integer dayId : list) {
            weekToUpdate.getDaysList().get(dayId).getSubjects().clear();
        }
    }
}
