package my.dzeko.timetable.presenters

import android.annotation.SuppressLint
import android.os.Bundle
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import my.dzeko.timetable.R
import my.dzeko.timetable.activities.AddScheduleActivity
import my.dzeko.timetable.activities.RemoveScheduleActivity
import my.dzeko.timetable.activities.SettingsActivity
import my.dzeko.timetable.contracts.MainContract
import my.dzeko.timetable.entities.Group
import my.dzeko.timetable.interfaces.IModel
import my.dzeko.timetable.models.Model
import my.dzeko.timetable.observers.GroupObservable
import my.dzeko.timetable.wrappers.DatabaseWrapper
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper

class MainPresenter(private var mView: MainContract.View?) : MainContract.Presenter {
    private val mModel: IModel = Model.getInstance()

    private var mPreviousBottomNavigationItemId = -1
    private var mPreviousGroupNameNavigationItemId = -1

    private val mGroupIds: MutableMap<String, Int> = HashMap()
    private var mGroupIdCounter = 0

    private val mCompositeDisposable = CompositeDisposable()

    private var mIsCreatedFragment: BooleanArray? = booleanArrayOf(false, false, false)
    private var mActiveBottomNavigationFragmentId = -1

    private var mIsRestoredState = false

    init {
        GroupObservable.getInstance().registerObserver(this)

        //Initialize DB
        DatabaseWrapper.initialize(mView!!.context)

        //Initialize SharedPrefs
        SharedPreferencesWrapper.initialize(mView!!.context)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return

        mIsRestoredState = true

        mPreviousBottomNavigationItemId = savedInstanceState.getInt(PREV_BOT_NAV_ITEM_ID)
        mIsCreatedFragment = savedInstanceState.getBooleanArray(CREATED_FRAGMENTS)
        mActiveBottomNavigationFragmentId = savedInstanceState.getInt(ACTIVE_BOT_NAV_FRAGMENT_ID)
    }

    override fun saveState(): Bundle {
        val bundle = Bundle()
        bundle.putInt(PREV_BOT_NAV_ITEM_ID, mPreviousBottomNavigationItemId)
        bundle.putBooleanArray(CREATED_FRAGMENTS, mIsCreatedFragment)
        bundle.putInt(ACTIVE_BOT_NAV_FRAGMENT_ID, mActiveBottomNavigationFragmentId)
        return bundle
    }

    override fun onUserClick(itemId: Int): Boolean {
        when (itemId) {
            android.R.id.home -> {
                mView!!.openDrawer()
                return true
            }
        }
        return false
    }

    override fun destroy() {
        mView = null
        GroupObservable.getInstance().unregisterObserver(this)
        mCompositeDisposable.dispose()
    }

    @SuppressLint("CheckResult")
    override fun onGroupAdded(groupName: String) {
        mCompositeDisposable.add(
            Completable.fromAction {
                val id = mGroupIdCounter++
                mGroupIds[groupName] = id

                mView!!.addGroupNameNavigationDrawer(groupName, id)
                if (mPreviousGroupNameNavigationItemId != -1) {
                    mView!!.setCheckedGroupNameNavigationView(
                        mPreviousGroupNameNavigationItemId,
                        false
                    )
                }
                mView!!.setCheckedGroupNameNavigationView(id, true)
                mPreviousGroupNameNavigationItemId = id
                mView!!.setTitle(groupName)
            }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    @SuppressLint("CheckResult")
    override fun onGroupRemoved(deletedGroupName: String, newSelectedGroupName: String) {
        mCompositeDisposable.add(
            Completable.fromAction {
                mView!!.showLoading()
                val thisGroupNameId = mGroupIds[deletedGroupName]!!
                if (newSelectedGroupName != null) {
                    val newSelectedGroupNameId = mGroupIds[newSelectedGroupName]!!
                    mView!!.setCheckedGroupNameNavigationView(newSelectedGroupNameId, true)
                    mPreviousGroupNameNavigationItemId = newSelectedGroupNameId
                    mView!!.setTitle(newSelectedGroupName)
                }
                mView!!.removeGroupNameNavigationDrawer(thisGroupNameId)
                mGroupIds.remove(deletedGroupName)
                mView!!.hideLoading()
            }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun onBottomNavigationItemSelected(itemId: Int): Boolean {
        if (itemId == mPreviousBottomNavigationItemId) return false

        mPreviousBottomNavigationItemId = itemId

        when (itemId) {
            R.id.schedule_bottom_navigation_main -> {
                updateFragment(MainContract.SCHEDULE_FRAGMENT_ID)
                mActiveBottomNavigationFragmentId = MainContract.SCHEDULE_FRAGMENT_ID
                saveSelectedFragmentId()
                return true
            }

            R.id.calendar_bottom_navigation_main -> {
                updateFragment(MainContract.CALENDAR_FRAGMENT_ID)
                mActiveBottomNavigationFragmentId = MainContract.CALENDAR_FRAGMENT_ID
                saveSelectedFragmentId()
                return true
            }

            R.id.editing_bottom_navigation_main -> {
                updateFragment(MainContract.EDITING_FRAGMENT_ID)
                mActiveBottomNavigationFragmentId = MainContract.EDITING_FRAGMENT_ID
                saveSelectedFragmentId()
                return true
            }
        }
        return false
    }

    @SuppressLint("CheckResult")
    private fun saveSelectedFragmentId() {
        Completable.fromAction {
            mModel.saveCurrentBottomNavigationFragment(
                mActiveBottomNavigationFragmentId
            )
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun updateFragment(fragmentId: Int) {
        if (mIsCreatedFragment!![fragmentId]) {
            mView!!.updateFragment(fragmentId, mActiveBottomNavigationFragmentId)
        } else {
            mIsCreatedFragment!![fragmentId] = true
            if (mActiveBottomNavigationFragmentId == -1) {
                mView!!.createFragment(fragmentId)
            } else {
                mView!!.createFragment(fragmentId, mActiveBottomNavigationFragmentId)
            }
        }
    }

    override fun onNavigationItemSelected(itemId: Int, itemName: String): Boolean {
        if (mGroupIds.containsKey(itemName)) {
            if (itemId == mPreviousGroupNameNavigationItemId) return false

            mView!!.showLoading()

            mView!!.setTitle(itemName)

            mCompositeDisposable.add(
                selectSchedule(itemName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { mView!!.hideLoading() }
            )

            if (mPreviousGroupNameNavigationItemId != -1) {
                mView!!.setCheckedGroupNameNavigationView(mPreviousGroupNameNavigationItemId, false)
            }
            mView!!.setCheckedGroupNameNavigationView(itemId, true)
            mPreviousGroupNameNavigationItemId = itemId
            mView!!.closeDrawer()
            return true
        } else {
            when (itemId) {
                R.id.add_schedule_navigation -> {
                    mView!!.startActivity(AddScheduleActivity::class.java)
                    mView!!.closeDrawer()
                    return true
                }

                R.id.remove_schedule_navigation -> {
                    mView!!.startActivity(RemoveScheduleActivity::class.java)
                    mView!!.closeDrawer()
                    return true
                }

                R.id.settings_navigation -> {
                    mView!!.startActivity(SettingsActivity::class.java)
                    mView!!.closeDrawer()
                    return true
                }
            }
        }
        return false
    }

    private fun selectSchedule(itemname: String): Completable {
        return Completable.fromAction { mModel.selectSchedule(itemname) }
    }

    @SuppressLint("CheckResult")
    override fun onGroupsFirstLoad() {
        mView!!.showLoading()

        mCompositeDisposable.add(
            Observable.just(mModel).subscribeOn(Schedulers.io())
                .flatMap {
                    val groups = mModel.groupList
                    if (groups.size == 0) throw Exception()

                    Observable.fromIterable(groups)
                }
                .map { group ->
                    mGroupIds[group.name] = mGroupIdCounter++
                    group
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Group?>() {
                    override fun onNext(group: Group) {
                        val groupName = group.name
                        mView!!.addGroupNameNavigationDrawer(groupName, mGroupIds[groupName]!!)
                    }

                    override fun onError(e: Throwable) {
                        mView!!.hideLoading()
                        mView!!.startActivity(AddScheduleActivity::class.java)
                    }

                    override fun onComplete() {
                        setSelectedSchedule()
                    }
                })
        )
    }

    @SuppressLint("CheckResult")
    private fun setSelectedSchedule() {
        mCompositeDisposable.add(
            Single.just(mModel).subscribeOn(Schedulers.io())
                .map { mModel.selectedScheduleGroupName }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<String?>() {
                    override fun onSuccess(selectedGroup: String) {
                        val id = mGroupIds[selectedGroup]!!
                        mView!!.setCheckedGroupNameNavigationView(id, true)
                        mPreviousGroupNameNavigationItemId = id
                        mView!!.setTitle(selectedGroup)
                        mView!!.hideLoading()
                    }

                    override fun onError(e: Throwable) {
                    }
                })
        )
    }

    @SuppressLint("CheckResult")
    override fun onFragmentInitialization() {
        if (mIsRestoredState) return

        mCompositeDisposable.add(
            Single.just(mModel)
                .map { model -> model.currentBottomNavigationFragment }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Int?>() {
                    override fun onSuccess(fragmentId: Int) {
                        var fragmentId = fragmentId
                        if (fragmentId == -1) {
                            fragmentId = MainContract.SCHEDULE_FRAGMENT_ID
                        }
                        mView!!.createFragment(fragmentId)
                        mPreviousBottomNavigationItemId =
                            getBottomNavigationItemIdFromFragmentId(fragmentId)
                        mView!!.setBottomNavigationItem(mPreviousBottomNavigationItemId)
                        mIsCreatedFragment!![fragmentId] = true
                        mActiveBottomNavigationFragmentId = fragmentId
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun getBottomNavigationItemIdFromFragmentId(fragmentId: Int): Int {
        when (fragmentId) {
            MainContract.SCHEDULE_FRAGMENT_ID -> return R.id.schedule_bottom_navigation_main
            MainContract.CALENDAR_FRAGMENT_ID -> return R.id.calendar_bottom_navigation_main
            MainContract.EDITING_FRAGMENT_ID -> return R.id.editing_bottom_navigation_main
        }
        return 0
    }

    override fun onManuallyScheduleCreated() {
        //onBottomNavigationItemSelected(R.id.editing_bottom_navigation_main);
        mView!!.showManuallyCreationHint()
    }

    @SuppressLint("CheckResult")
    override fun onCurrentWeekChecking() {
        Completable.fromAction { mModel.checkCurrentWeek() }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    companion object {
        private const val PREV_BOT_NAV_ITEM_ID = "mPreviousBottomNavigationItemId"
        private const val CREATED_FRAGMENTS = "mIsCreatedFragment"
        private const val ACTIVE_BOT_NAV_FRAGMENT_ID = "mActiveBottomNavigationFragmentId"
    }
}
