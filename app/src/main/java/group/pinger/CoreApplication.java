package group.pinger;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;

import group.pinger.database.CacheManager;
import group.pinger.utils.SharedPreferencesUtil;
import group.pinger.BuildConfig;
import group.pinger.R;
import timber.log.Timber;

public class CoreApplication extends Application {

    public static final String CHANNEL_ID = "group.pinger.ANDROID";

    @Override
    public void onCreate(){
        super.onCreate();
        initTimber();
        initApplicationClasses();
        createNotificationChannel();
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

    private void createNotificationChannel() {
        String name = getString(R.string.ping_channel);
        String desc = getString(R.string.ping_channel_desc);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(desc);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
