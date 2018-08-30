package my.dzeko.timetable.contracts;

import android.content.Context;
import android.support.v4.app.Fragment;

import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;
import my.dzeko.timetable.observers.interfaces.IGroupObserver;

public class MainContract {

    public interface Presenter extends IPresenter, IGroupObserver{
        void onGroupsFirstLoad();
        boolean onBottomNavigationItemSelected(int itemId);
        boolean onNavigationItemSelected(int itemId, String itemName);
    }

    public interface View extends IView {
        int SCHEDULE_FRAGMENT_ID = 0;
        int CALENDAR_FRAGMENT_ID = 1;
        int EDITING_FRAGMENT_ID = 2;

        void openDrawer();
        void closeDrawer();
        void lockDrawer();
        void unlockDrawer();
        void updateFragment(int fragmentId);
        void addGroupNameNavigationDrawer(String groupName, int id);
        void removeGroupNameNavigationDrawer(int removeItemId);
        void setCheckedGroupNameNavigationView(int id, boolean isChecked);

        void startActivity(Class c);
    }
}
