package si.rozna.ping.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import si.rozna.ping.Constants;

public class SharedPreferencesUtil {

    public static SharedPreferencesUtil instance;
    private SharedPreferences sharedPreferences;

    private SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
    }

    public static void createInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesUtil(context);
        }
    }

    public static SharedPreferencesUtil getInstance() {
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void deleteString(String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

}
