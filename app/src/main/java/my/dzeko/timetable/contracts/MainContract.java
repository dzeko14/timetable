package my.dzeko.timetable.contracts;

import android.os.Bundle;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.observers.interfaces.IGroupObserver;

public class MainContract {
    public static final int SCHEDULE_FRAGMENT_ID = 0;
    public static final int CALENDAR_FRAGMENT_ID = 1;
    public static final int EDITING_FRAGMENT_ID = 2;
    public static final int ADD_SCHEDULE_REQUEST_CODE = 124;

    public interface Presenter extends IPresenter, IGroupObserver{
        void onGroupsFirstLoad();
        boolean onBottomNavigationItemSelected(int itemId);
        boolean onNavigationItemSelected(int itemId, String itemName);
        void onFragmentInitialization();
        void onManuallyScheduleCreated();
        void onRestoreState(Bundle savedInstanceState);
        Bundle saveState();
        void onCurrentWeekChecking();
    }

    public interface View extends IView {
        void openDrawer();
        void closeDrawer();
        void lockDrawer();
        void unlockDrawer();
        void updateFragment(int fragmentId, int activeFragmentId);
        void addGroupNameNavigationDrawer(String groupName, int id);
        void removeGroupNameNavigationDrawer(int removeItemId);
        void setCheckedGroupNameNavigationView(int id, boolean isChecked);
        void createFragment(int fragmentId, int activeFragmentId);
        void createFragment(int fragmentId);
        void setBottomNavigationItem(int itemId);
        void startActivity(Class c);
        void showManuallyCreationHint();
        void setTitle(String title);
    }
}
