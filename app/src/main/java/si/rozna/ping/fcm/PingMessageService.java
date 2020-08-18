package si.rozna.ping.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import si.rozna.ping.R;
import si.rozna.ping.auth.LoginActivity;
import timber.log.Timber;

public class PingMessageService extends FirebaseMessagingService {

    public static final String CHANNEL_ID = "si.rozna.ping.ANDROID";
    public static final int NOTIFICATION_ID = 420420420;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Timber.i("MESSAGE RECEIVED");

        Timber.i("Message id: %s", remoteMessage.getMessageId());
        Timber.i("From: %s", remoteMessage.getFrom());
        Timber.i("Data: %s", remoteMessage.getData());
        Timber.i("MessageType: %s", remoteMessage.getMessageType());


        PingMessage message = new PingMessage(remoteMessage);
        String notificationMessage = String.format("User %s pinged group %s!", message.getUser().getUsername(), message.getGroup().getName());

        sendNotification(notificationMessage);
    }

    private void sendNotification(String message){

        Notification notification = createNotification(message);
        NotificationChannel channel = createNotificationChannel();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }

    private Notification createNotification(String message) {

        // Pending intent that will lunch application
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Default sound for notifications
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Returns built notification
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.ping))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent).build();
    }

    private NotificationChannel createNotificationChannel() {
        String name = getString(R.string.ping_channel);
        String desc = getString(R.string.ping_channel_desc);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(desc);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        return channel;
    }
}
