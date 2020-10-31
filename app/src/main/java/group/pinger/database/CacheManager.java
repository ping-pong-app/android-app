package group.pinger.database;

import android.app.Application;

import group.pinger.Constants;
import group.pinger.utils.SharedPreferencesUtil;

public class CacheManager {

    private static CacheManager INSTANCE;
    private Application application;

    private CacheManager(Application application){
        this.application = application;
    }

    public static void createInstance(Application application) {
        if(application != null) {
            INSTANCE = new CacheManager(application);
        }
    }

    public static CacheManager getInstance(){
        return INSTANCE;
    }

    public static boolean isInstanceCreated(){
        return INSTANCE != null;
    }

    public static void clearCache(){

        // Clear shared preferences for cache
        SharedPreferencesUtil.getInstance().deleteString(Constants.SHARED_PREFERENCES_CACHE_KEY);

        // Delete all cached data
        new GroupRepository(INSTANCE.application).dropTable();

    }
}
