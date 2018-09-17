package my.dzeko.timetable.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.fragments.EditWeekFragment;

public class EditingPagerAdapter extends FragmentPagerAdapter {
    private List<Week> mWeeks = new ArrayList<>();


    public EditingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        EditWeekFragment fragment = EditWeekFragment.getInstance();
        fragment.setWeek(mWeeks.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return mWeeks.size();
    }

    public void addWeek(Week week) {
        mWeeks.add(week);
        notifyDataSetChanged();
    }
}
