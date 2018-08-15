package my.dzeko.timetable.presenters;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.MainContract;
import my.dzeko.timetable.fragments.CalendarFragment;
import my.dzeko.timetable.fragments.ScheduleFragment;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.MockModel;
import my.dzeko.timetable.observers.GroupObservable;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private IModel mModel;

    private int mPreviousBottomNavigationItemId = R.id.schedule_bottom_navigation_main;
    private int mPreviousGroupNameNavigationItemId = -1;

    private Map<String, Integer> mGroupIds = new HashMap<>();
    private int mGroupIdCounter = 0;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MainPresenter(MainContract.View view) {
        mView = view;
        mModel = MockModel.getInstance();
        GroupObservable.getInstance().registerObserver(this);
    }

    @Override
    public boolean onUserClick(int itemId) {
        switch (itemId) {
            case android.R.id.home:
                mView.openDrawer();
                return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
        GroupObservable.getInstance().unregisterObserver(this);
    }

    @Override
    public void onGroupAdded(String groupName) {
        int id = mGroupIds.get(groupName);
        mView.addGroupNameNavigationDrawer(groupName, id);
        mView.setCheckedGroupNameNavigationView(mPreviousBottomNavigationItemId, false);
        mView.setCheckedGroupNameNavigationView(id, true);
        mPreviousGroupNameNavigationItemId = id;
    }

    @Override
    public void onGroupRemoved(String deletedGroupName, String newSelectedGroupName) {
        int thisGroupNameId = mGroupIds.get(deletedGroupName);
        if(deletedGroupName.equals(mModel.getSelectedScheduleGroupName())) {
            int newSelectedGroupNameId = mGroupIds.get(newSelectedGroupName);
            mView.setCheckedGroupNameNavigationView(newSelectedGroupNameId, true);
            mPreviousGroupNameNavigationItemId = newSelectedGroupNameId;
        }
        mView.removeGroupNameNavigationDrawer(thisGroupNameId);
        mGroupIds.remove(deletedGroupName);
    }

    @Override
    public boolean onBottomNavigationItemSelected(int itemId) {
        if(itemId == mPreviousBottomNavigationItemId) return false;

        mPreviousBottomNavigationItemId = itemId;

        switch (itemId) {
            case R.id.schedule_bottom_navigation_main:
                updateFragment(ScheduleFragment.getInstance());
                return true;
            case R.id.calendar_bottom_navigation_main:
                updateFragment(CalendarFragment.getInstance());
                return true;
            case R.id.settings_bottom_navigation_main:

                return true;
        }
        return false;
    }

    private void updateFragment(Fragment fragment) {
        mView.updateFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(int itemId, String itemName) {
        if(mGroupIds.containsKey(itemName)) {
            mView.showLoading();

            mCompositeDisposable.add(
                    selectSchedule(itemName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action() {
                                @Override
                                public void run() throws Exception {
                                    mView.hideLoading();
                                }
                            })
            );

            if(mPreviousGroupNameNavigationItemId != -1) {
                mView.setCheckedGroupNameNavigationView(mPreviousGroupNameNavigationItemId, false);
            }
            mView.setCheckedGroupNameNavigationView(itemId, true);
            mPreviousGroupNameNavigationItemId = itemId;
            return true;
        } else {
            //TODO: Implement non-groups menuItem clicks
        }
        return false;
    }

    private Completable selectSchedule(final String itemname) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.selectSchedule(itemname);
            }
        });
    }

    @Override
    public void onGroupsFirstLoad() {
        List<String> groups = mModel.getGroupList();
        for (String group : groups) {
            mGroupIds.put(group, mGroupIdCounter);
            mView.addGroupNameNavigationDrawer(group, mGroupIdCounter++);
        }
        String selectedGroup = mModel.getSelectedScheduleGroupName();
        int id = mGroupIds.get(selectedGroup);
        mView.setCheckedGroupNameNavigationView(id, true);
        mPreviousGroupNameNavigationItemId = id;
    }
}
