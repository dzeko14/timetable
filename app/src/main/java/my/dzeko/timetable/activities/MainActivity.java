package my.dzeko.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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
import my.dzeko.timetable.fragments.ScheduleFragment;
import my.dzeko.timetable.presenters.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private SubMenu mGroupNamesSubMenu;

    private View mProgressBar;
    private View mFragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.main_drawer_layout);

        findViews();
        initializePresenter();
        initializeBottomNavigationView();
        initializeToolBar();
        initializeFragmentLayout();
        initializeNavigationView();
        loadGroupNames();
    }

    private void findViews() {
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return mPresenter.onBottomNavigationItemSelected(item.getItemId());
            }
        });
    }

    private void initializeFragmentLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragments_frame_layout_main_activity, ScheduleFragment.getInstance());
        transaction.commit();
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
    public void updateFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragments_frame_layout_main_activity, fragment);
        transaction.commit();
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
    }

    @Override
    public void hideLoading() {
        mFragmentLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
