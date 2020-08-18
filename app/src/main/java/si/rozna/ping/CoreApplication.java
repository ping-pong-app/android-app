package si.rozna.ping;

import android.app.Application;

import si.rozna.ping.database.CacheManager;
import si.rozna.ping.utils.SharedPreferencesUtil;
import timber.log.Timber;

public class CoreApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        initTimber();
        initApplicationClasses();
    }

    private void initTimber(){
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initApplicationClasses(){
        SharedPreferencesUtil.createInstance(this);
        CacheManager.createInstance(this);
    }
}
