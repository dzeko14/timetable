package my.dzeko.timetable.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import my.dzeko.timetable.fragments.CalendarUnitFragment;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter {
    private static final int MONTH_COUNT = 12;

    public CalendarPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        CalendarUnitFragment fragment = new CalendarUnitFragment();
        Bundle args = new Bundle();
        args.putInt(CalendarUnitFragment.MONTH_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return MONTH_COUNT;
    }
}
