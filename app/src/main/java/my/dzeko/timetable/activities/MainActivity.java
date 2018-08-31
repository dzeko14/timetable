package my.dzeko.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.MainContract;
import my.dzeko.timetable.fragments.CalendarFragment;
import my.dzeko.timetable.fragments.EditingFragment;
import my.dzeko.timetable.fragments.ScheduleFragment;
import my.dzeko.timetable.presenters.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private SubMenu mGroupNamesSubMenu;

    private View mProgressBar;
    private View mFragmentLayout;
    private BottomNavigationView mBottomNavigationView;

    private Fragment[] mFragments = new Fragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initializePresenter();
        initializeBottomNavigationView();
        initializeToolBar();
        initializeFragments();
        initializeNavigationView();
        loadGroupNames();
    }

    private void findViews() {
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        mProgressBar = findViewById(R.id.progress_bar_main);
        mFragmentLayout = findViewById(R.id.fragments_frame_layout_main_activity);
    }

    private void initializePresenter() {
        mPresenter = new MainPresenter(this);
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
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return mPresenter.onBottomNavigationItemSelected(item.getItemId());
            }
        });
    }

    private void initializeFragments() {
        mPresenter.onFragmentInitialization();
    }

    private void initializeNavigationView() {
        NavigationView nv = findViewById(R.id.navigation_view);
        mGroupNamesSubMenu = nv.getMenu().getItem(0).getSubMenu();
        mGroupNamesSubMenu.setGroupCheckable(R.id.schedule_list_navigation_group, true, true);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return mPresenter.onNavigationItemSelected(item.getItemId(), item.getTitle().toString());
            }
        });
    }

    private void loadGroupNames() {
        mPresenter.onGroupsFirstLoad();
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onUserClick(id) || super.onOptionsItemSelected(item);
    }

    @Override
    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void updateFragment(int fragmentId, int activeFragmentId) {
        Fragment activeFragment = mFragments[activeFragmentId];
        Fragment newActiveFragment = mFragments[fragmentId];
        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .show(newActiveFragment)
                .commit();
    }

    @Override
    public void addGroupNameNavigationDrawer(String groupName, int id) {
         mGroupNamesSubMenu.add(R.id.schedule_list_navigation_group, id, id, groupName).setCheckable(true);
    }

    @Override
    public void removeGroupNameNavigationDrawer(int id) {
        mGroupNamesSubMenu.removeItem(id);
    }

    @Override
    public void setCheckedGroupNameNavigationView(int id, boolean isChecked) {
        mGroupNamesSubMenu.findItem(id).setChecked(isChecked);
    }

    @Override
    public void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        mFragmentLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mFragmentLayout.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void createFragment(int fragmentId, int activeFragmentId) {
        Fragment activeFragment = mFragments[activeFragmentId];
        mFragments[fragmentId] = getFragmentById(fragmentId);
        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .add(R.id.fragments_frame_layout_main_activity,  mFragments[fragmentId])
                .commit();
    }

    @Override
    public void createFragment(int fragmentId) {
        mFragments[fragmentId] = getFragmentById(fragmentId);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragments_frame_layout_main_activity,  mFragments[fragmentId])
                .commit();
    }

    private Fragment getFragmentById(int fragmentId) {
        switch (fragmentId) {
            case MainContract.SCHEDULE_FRAGMENT_ID:
                return ScheduleFragment.getInstance();
            case MainContract.CALENDAR_FRAGMENT_ID:
                return CalendarFragment.getInstance();
            case MainContract.EDITING_FRAGMENT_ID:
                return EditingFragment.getInstance();
            default:
                return null;
        }
    }

    @Override
    public void setBottomNavigationItem(int itemId) {
        mBottomNavigationView.getMenu().findItem(itemId).setChecked(true);
    }
}
