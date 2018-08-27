package my.dzeko.timetable.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
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

    public static boolean isCurrentWeekFirst(String keyDateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault());
        Date keyDate = sdf.parse(keyDateString);
        Date currentDate = new Date();

        long diffInMillies = Math.abs(currentDate.getTime() - keyDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 7;

        return ((diff % 2) == 0);
    }

    public static String getMonthName(int month) {
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
    public static String createKeyDate(boolean isFirstWeek) {
        Calendar calendar = new GregorianCalendar();

        if(isFirstWeek){
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            calendar.add(Calendar.WEEK_OF_MONTH, -1);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year  = calendar.get(Calendar.YEAR);

        return String.format("%d/%d/%d", day, month, year);
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
}