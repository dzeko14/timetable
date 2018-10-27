package my.dzeko.timetable.wrappers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import my.dzeko.timetable.R;

public class SharedPreferencesWrapper {
    private static final String SELECTED_GROUP = "selected_group";
    private static final String KEY_DATE = "key_date";
    private static final String SELECTED_BOTTOM_NAV_FRAGMENT = "selected_bottom_fragment";
    private static String CURRENT_WEEK_KEY;
    private static String FIRST_WEEK_VALUE;
    private static String SECOND_WEEK_VALUE;

    private static SharedPreferencesWrapper mInstance;

    private SharedPreferencesWrapper(){ }

    public static void initialize(Context context) {
        if (mInstance != null) return;
        mInstance = new SharedPreferencesWrapper();
        mInstance.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        CURRENT_WEEK_KEY = context.getString(R.string.weeks_prefs_key);
        FIRST_WEEK_VALUE = context.getString(R.string.first_week_prefs_value);
        SECOND_WEEK_VALUE = context.getString(R.string.second_week_prefs_value);
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

    public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener){
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void setCurrentWeek(boolean isFirstWeek){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (isFirstWeek) {
            editor.putString(CURRENT_WEEK_KEY, FIRST_WEEK_VALUE);
        } else {
            editor.putString(CURRENT_WEEK_KEY, SECOND_WEEK_VALUE);
        }
        editor.apply();
    }
}
