package si.rozna.ping.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import si.rozna.ping.Constants;

public class SharedPreferencesUtil {

    public static SharedPreferencesUtil instance;
    private SharedPreferences sharedPreferences;

    public static SharedPreferencesUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesUtil();
            instance.initialize(context);
        }
        return instance;

    }

    public static SharedPreferencesUtil getInstance() {
        return instance;
    }

    private void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Activity.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

}
