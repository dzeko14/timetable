package my.dzeko.timetable;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import my.dzeko.timetable.fragments.CalendarFragment;
import my.dzeko.timetable.fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.main_drawer_layout);

        initializeBottomNavigationView();
        initializeToolBar();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragments_frame_layout_main_activity, ScheduleFragment.getInstance());
        transaction.commit();
    }

    private void initializeToolBar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_main_toolbar_home);
        }
    }

    private void initializeBottomNavigationView() {
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            int mPreviousId = R.id.schedule_bottom_navigation_main;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == mPreviousId) return false;

                mPreviousId = id;
                item.setChecked(true);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (id) {
                    case R.id.schedule_bottom_navigation_main:
                        transaction.replace(R.id.fragments_frame_layout_main_activity, ScheduleFragment.getInstance());
                        transaction.commit();
                        return true;
                    case R.id.calendar_bottom_navigation_main:
                        transaction.replace(R.id.fragments_frame_layout_main_activity, CalendarFragment.getInstance());
                        transaction.commit();
                        return true;
                    case R.id.settings_bottom_navigation_main:

                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
