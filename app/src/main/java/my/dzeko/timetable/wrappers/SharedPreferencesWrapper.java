package my.dzeko.timetable.wrappers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {
    private static final String PREFS_FILE_NAME = "preferences";
    private static final String SELECTED_GROUP = "selected_group";
    private static final String KEY_DATE = "key_date";
    private static final String SELECTED_BOTTOM_NAV_FRAGMENT = "selected_bottom_fragment";

    private static SharedPreferencesWrapper mInstance;

    private SharedPreferencesWrapper(){ }

    public static void initialize(Context context) {
        if (mInstance != null) return;
        mInstance = new SharedPreferencesWrapper();
        mInstance.mSharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesWrapper getInstance(){
        return mInstance;
    }


    private SharedPreferences mSharedPreferences;

    public void setSelectedGroup(String group) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SELECTED_GROUP, group);
        editor.apply();
    }

    public String getSelectedGroup() {
        return mSharedPreferences.getString(SELECTED_GROUP, null);
    }

    public void removeSelectedGroup() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(SELECTED_GROUP);
        editor.apply();
    }

    public void setKeyDate(long keyDateLong) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(KEY_DATE, keyDateLong);
        editor.apply();
    }

    public long getKeyDate() {
        return mSharedPreferences.getLong(KEY_DATE, -1);
    }

    public void setSelectedFragment(int fragmentId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(SELECTED_BOTTOM_NAV_FRAGMENT, fragmentId);
        editor.apply();
    }

    public int getSelectedFragmentId() {
        return mSharedPreferences.getInt(SELECTED_BOTTOM_NAV_FRAGMENT, -1);
    }
}
