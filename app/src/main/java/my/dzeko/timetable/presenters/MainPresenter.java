package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.R;
import my.dzeko.timetable.activities.AddScheduleActivity;
import my.dzeko.timetable.activities.RemoveScheduleActivity;
import my.dzeko.timetable.activities.SettingsActivity;
import my.dzeko.timetable.adapters.RemoveScheduleAdapter;
import my.dzeko.timetable.contracts.MainContract;
import my.dzeko.timetable.contracts.RemoveScheduleContract;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.observers.GroupObservable;
import my.dzeko.timetable.receivers.ScheduleNotificationReceiver;
import my.dzeko.timetable.utils.NotificationUtils;
import my.dzeko.timetable.wrappers.DatabaseWrapper;
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private IModel mModel;

    private int mPreviousBottomNavigationItemId = -1;
    private int mPreviousGroupNameNavigationItemId = -1;

    private Map<String, Integer> mGroupIds = new HashMap<>();
    private int mGroupIdCounter = 0;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private boolean[] mIsCreatedFragment = {false, false, false};
    private int mActiveBottomNavigationFragmentId = -1;

    private boolean mIsRestoredState = false;

    private static final String PREV_BOT_NAV_ITEM_ID = "mPreviousBottomNavigationItemId";
    private static final String CREATED_FRAGMENTS = "mIsCreatedFragment";
    private static final String ACTIVE_BOT_NAV_FRAGMENT_ID = "mActiveBottomNavigationFragmentId";

    public MainPresenter(MainContract.View view) {
        mView = view;
        mModel = Model.getInstance();
        GroupObservable.getInstance().registerObserver(this);

        //Initialize DB
        DatabaseWrapper.initialize(mView.getContext());

        //Initialize SharedPrefs
        SharedPreferencesWrapper.initialize(mView.getContext());
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;

        mIsRestoredState = true;

        mPreviousBottomNavigationItemId = savedInstanceState.getInt(PREV_BOT_NAV_ITEM_ID);
        mIsCreatedFragment = savedInstanceState.getBooleanArray(CREATED_FRAGMENTS);
        mActiveBottomNavigationFragmentId = savedInstanceState.getInt(ACTIVE_BOT_NAV_FRAGMENT_ID);
    }

    @Override
    public Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putInt(PREV_BOT_NAV_ITEM_ID, mPreviousBottomNavigationItemId);
        bundle.putBooleanArray(CREATED_FRAGMENTS, mIsCreatedFragment);
        bundle.putInt(ACTIVE_BOT_NAV_FRAGMENT_ID, mActiveBottomNavigationFragmentId);
        return bundle;
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
        mCompositeDisposable.dispose();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupAdded(final String groupName) {
        mCompositeDisposable.add(
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                    int id = mGroupIdCounter++;
                    mGroupIds.put(groupName, id);

                    mView.addGroupNameNavigationDrawer(groupName, id);
                    if(mPreviousGroupNameNavigationItemId != -1) {
                        mView.setCheckedGroupNameNavigationView(mPreviousGroupNameNavigationItemId, false);
                    }
                    mView.setCheckedGroupNameNavigationView(id, true);
                    mPreviousGroupNameNavigationItemId = id;

                    mView.setTitle(groupName);
                }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupRemoved(final String deletedGroupName, final String newSelectedGroupName) {

        mCompositeDisposable.add(
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mView.showLoading();
                int thisGroupNameId = mGroupIds.get(deletedGroupName);
                if (newSelectedGroupName != null) {
                    int newSelectedGroupNameId = mGroupIds.get(newSelectedGroupName);
                    mView.setCheckedGroupNameNavigationView(newSelectedGroupNameId, true);
                    mPreviousGroupNameNavigationItemId = newSelectedGroupNameId;
                    mView.setTitle(newSelectedGroupName);
                }
                mView.removeGroupNameNavigationDrawer(thisGroupNameId);
                mGroupIds.remove(deletedGroupName);
                mView.hideLoading();
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
    }

    @Override
    public boolean onBottomNavigationItemSelected(int itemId) {
        if(itemId == mPreviousBottomNavigationItemId) return false;

        mPreviousBottomNavigationItemId = itemId;

        switch (itemId) {
            case R.id.schedule_bottom_navigation_main:
                updateFragment(MainContract.SCHEDULE_FRAGMENT_ID);
                mActiveBottomNavigationFragmentId = MainContract.SCHEDULE_FRAGMENT_ID;
                saveSelectedFragmentId();
                return true;
            case R.id.calendar_bottom_navigation_main:
                updateFragment(MainContract.CALENDAR_FRAGMENT_ID);
                mActiveBottomNavigationFragmentId = MainContract.CALENDAR_FRAGMENT_ID;
                saveSelectedFragmentId();
                return true;
            case R.id.editing_bottom_navigation_main:
                updateFragment(MainContract.EDITING_FRAGMENT_ID);
                mActiveBottomNavigationFragmentId = MainContract.EDITING_FRAGMENT_ID;
                saveSelectedFragmentId();
                return true;
        }
        return false;
    }

    @SuppressLint("CheckResult")
    private void saveSelectedFragmentId() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.saveCurrentBottomNavigationFragment(mActiveBottomNavigationFragmentId);
            }
        })
        .subscribeOn(Schedulers.io())
        .subscribe();
    }

    private void updateFragment(int fragmentId) {
        if(mIsCreatedFragment[fragmentId]) {
            mView.updateFragment(fragmentId, mActiveBottomNavigationFragmentId);
        } else {
            mIsCreatedFragment[fragmentId] = true;
            if(mActiveBottomNavigationFragmentId == -1) {
                mView.createFragment(fragmentId);
            } else {
                mView.createFragment(fragmentId, mActiveBottomNavigationFragmentId);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemId, String itemName) {
        if(mGroupIds.containsKey(itemName)) {
            if (itemId == mPreviousGroupNameNavigationItemId) return false;

            mView.showLoading();

            mView.setTitle(itemName);

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
            mView.closeDrawer();
            return true;
        } else {
            switch (itemId) {
                case R.id.add_schedule_navigation:
                    mView.startActivity(AddScheduleActivity.class);
                    mView.closeDrawer();
                    return true;
                case R.id.remove_schedule_navigation:
                    mView.startActivity(RemoveScheduleActivity.class);
                    mView.closeDrawer();
                    return true;
                case R.id.settings_navigation:
                    mView.startActivity(SettingsActivity.class);
                    mView.closeDrawer();
                    return true;
                case R.id.noti_navigation:
                    mView.getContext().sendBroadcast(new Intent(mView.getContext(), ScheduleNotificationReceiver.class));
                    return true;
            }
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

    @SuppressLint("CheckResult")
    @Override
    public void onGroupsFirstLoad() {
        mView.showLoading();

        mCompositeDisposable.add(
                Observable.just(mModel).subscribeOn(Schedulers.io())
                .flatMap(new Function<IModel, Observable<Group>>() {
                    @Override
                    public Observable<Group> apply(IModel model) throws Exception {
                        List<Group> groups = mModel.getGroupList();
                        if(groups.size() == 0) throw new Exception();

                        return Observable.fromIterable(groups);
                    }
                })
                .map(new Function<Group, Group>() {
                    @Override
                    public Group apply(Group group) throws Exception {
                        mGroupIds.put(group.getName(), mGroupIdCounter++);
                        return group;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Group>() {
                    @Override
                    public void onNext(Group group) {
                        String groupName = group.getName();
                        mView.addGroupNameNavigationDrawer(groupName, mGroupIds.get(groupName));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.startActivity(AddScheduleActivity.class);
                    }

                    @Override
                    public void onComplete() {
                        setSelectedSchedule();
                    }
                })
        );
    }

    @SuppressLint("CheckResult")
    private void setSelectedSchedule() {
        mCompositeDisposable.add(
                Single.just(mModel).subscribeOn(Schedulers.io())
                .map(new Function<IModel, String>() {
                    @Override
                    public String apply(IModel model) throws Exception {
                        return mModel.getSelectedScheduleGroupName();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String selectedGroup) {
                        int id = mGroupIds.get(selectedGroup);
                        mView.setCheckedGroupNameNavigationView(id, true);
                        mPreviousGroupNameNavigationItemId = id;
                        mView.setTitle(selectedGroup);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void onFragmentInitialization() {
        if (mIsRestoredState) return;

        mCompositeDisposable.add(
            Single.just(mModel)
                .map(new Function<IModel, Integer>() {
                    @Override
                    public Integer apply(IModel model) throws Exception {
                        return model.getCurrentBottomNavigationFragment();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer fragmentId) {
                        if(fragmentId == -1) {
                            fragmentId = MainContract.SCHEDULE_FRAGMENT_ID;
                        }
                        mView.createFragment(fragmentId);
                        mPreviousBottomNavigationItemId = getBottomNavigationItemIdFromFragmentId(fragmentId);
                        mView.setBottomNavigationItem(mPreviousBottomNavigationItemId);
                        mIsCreatedFragment[fragmentId] = true;
                        mActiveBottomNavigationFragmentId = fragmentId;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                })
        );
    }

    private int getBottomNavigationItemIdFromFragmentId(Integer fragmentId) {
        switch (fragmentId) {
            case MainContract.SCHEDULE_FRAGMENT_ID:
                return R.id.schedule_bottom_navigation_main;
            case MainContract.CALENDAR_FRAGMENT_ID:
                return R.id.calendar_bottom_navigation_main;
            case MainContract.EDITING_FRAGMENT_ID:
                return R.id.editing_bottom_navigation_main;
        }
        return 0;
    }

    @Override
    public void onManuallyScheduleCreated() {
        //onBottomNavigationItemSelected(R.id.editing_bottom_navigation_main);
        mView.showManuallyCreationHint();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCurrentWeekChecking() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.checkCurrentWeek();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }
}
