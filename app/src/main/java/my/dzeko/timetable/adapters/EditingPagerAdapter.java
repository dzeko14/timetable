package my.dzeko.timetable.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
    private List<String> mTitles = new ArrayList<>();


    public EditingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        EditWeekFragment fragment = EditWeekFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EditWeekFragment.WEEK, mWeeks.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mWeeks.size();
    }

    public void addWeek(Week week, String title) {
        mWeeks.add(week);
        mTitles.add(title);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
