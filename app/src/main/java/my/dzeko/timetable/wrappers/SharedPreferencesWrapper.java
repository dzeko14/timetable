package my.dzeko.timetable.wrappers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {
    private static final String PREFS_FILE_NAME = "preferences";
    private static final String SELECTED_GROUP = "selected_group";
    private static final String KEY_DATE = "key_date";

    private static SharedPreferencesWrapper mInstance;

    private SharedPreferencesWrapper(){ }

    public static void initialize(Context context) {
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

    public void setKeyDate(String keyDate) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_DATE, keyDate);
        editor.apply();
    }

    public String getKeyDate() {
        return mSharedPreferences.getString(KEY_DATE, null);
    }
}
