package si.rozna.ping;

import android.app.Application;

import timber.log.Timber;

public class CoreApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        initTimber();
    }

    private void initTimber(){
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
}
