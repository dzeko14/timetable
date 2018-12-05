package my.dzeko.timetable.utils;

import android.annotation.SuppressLint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class DateUtils {
    public static final int FIRST_WEEK = 1;
    public static final int SECOND_WEEK = 2;

    public static int getCurrentDay() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentWeek() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public static int getCurrentMonth() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.MONTH);
    }

    public static boolean isCurrentWeekFirst(long keyDateLong) {
        Date keyDate = new Date(keyDateLong);
        Date currentDate = new Date();

        long diffInMillies = Math.abs(currentDate.getTime() - keyDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 7;

        return ((diff % 2) == 0);
    }

    public static List<String> getFirstWeekDates(boolean isCurrentWeekFirst) {
        List<String> dates = new ArrayList<>(6);
        Calendar calendar = new GregorianCalendar();

        if(isCurrentWeekFirst) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
        }

        for (int i = 0; i < 6; i++) {
            @SuppressLint("DefaultLocale")
            String date = String.format("%02d.%02d",
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1);
            dates.add(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    public static List<String> getSecondWeekDates(boolean isCurrentWeekFirst) {
        List<String> dates = new ArrayList<>(6);
        Calendar calendar = new GregorianCalendar();

        if(isCurrentWeekFirst) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }

        for (int i = 0; i < 6; i++) {
            @SuppressLint("DefaultLocale")
            String date = String.format("%02d.%02d",
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1);
            dates.add(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    public static String getMonthNameById(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "Січень";
            case Calendar.FEBRUARY:
                return "Лютий";
            case Calendar.MARCH:
                return "Березень";
            case Calendar.APRIL:
                return "Квітень";
            case Calendar.MAY:
                return "Травень";
            case Calendar.JUNE:
                return "Червень";
            case Calendar.JULY:
                return "Липень";
            case Calendar.AUGUST:
                return "Серпень";
            case Calendar.SEPTEMBER:
                return "Вересень";
            case Calendar.OCTOBER:
                return "Жовтень";
            case Calendar.NOVEMBER:
                return "Листопад";
            case Calendar.DECEMBER:
                return "Грудень";
            default:
                return null;
        }
    }

    @SuppressLint("DefaultLocale")
    public static long createKeyDate(boolean isFirstWeek) {
        Calendar calendar = new GregorianCalendar();

        if(isFirstWeek){
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }

        return calendar.getTimeInMillis();
    }

    public static int getDaysAmountInMonth(int month) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getFirstDayInWeekNumber(int month) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return dayInWeekNumber(calendar.get(Calendar.DAY_OF_WEEK));
    }

    private static int dayInWeekNumber(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return -1;
        }
    }

    public static int findDayPositionByDayNumber(int dayNumber, int month) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, dayNumber);
        calendar.set(Calendar.MONTH, month);
        return dayInWeekNumber(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static Date getDateFromDayAndMonth(int day, int month) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     *
     * @param date Date, over which determines week number.
     * @param keyDateLong Key date, using to determine week number.
     * @return Week number. (First week = 1; Second Week = 2)
     */
    public static int getWeekNumberFromDate(Date date, long keyDateLong){
        Date keyDate = new Date(keyDateLong);

        long diffInMillies = Math.abs(date.getTime() - keyDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 7;

        return ((diff % 2) == 0) ? 1 : 2;
    }

    public static int getDayOfWeekFromDate(Date d) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        return dayInWeekNumber(calendar.get(Calendar.DAY_OF_WEEK)) + 1;
    }

    @SuppressLint("DefaultLocale")
    public static String getdateInString(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return String.format("%02d/%02d/%d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
    }

    public static int getDayIdByName(String dayName) {
        final String[] DAY_OF_WEEKS_NAME = {
                "",
                "Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота"
        };
        List<String> dayList = Arrays.asList(DAY_OF_WEEKS_NAME);
        return dayList.indexOf(dayName);
    }



    static List<String> getCurrentWeekDates() {
        List<String> dates = new ArrayList<>(6);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 6; i++) {
            @SuppressLint("DefaultLocale")
            String date = String.format("%02d.%02d",
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1);
            dates.add(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }

    @SuppressLint("DefaultLocale")
    public static String getTomorrowMonthAndDay(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return  String.format("%02d.%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1);
    }


    public static long getScheduleTimeInLong(int hour){
        Calendar calendar = Calendar.getInstance();

        Calendar setCalendar = Calendar.getInstance();

        setCalendar.set(Calendar.HOUR_OF_DAY, hour);

        setCalendar.set(Calendar.MINUTE, 0);

        setCalendar.set(Calendar.SECOND, 0);

        if(setCalendar.before(calendar)) setCalendar.add(Calendar.DATE,1);

        return setCalendar.getTimeInMillis();
    }
}
