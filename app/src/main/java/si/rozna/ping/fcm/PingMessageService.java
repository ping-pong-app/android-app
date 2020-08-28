package si.rozna.ping.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import si.rozna.ping.Constants;
import si.rozna.ping.R;
import si.rozna.ping.auth.LoginActivity;
import timber.log.Timber;

import static si.rozna.ping.Constants.ACTION_PONG;
import static si.rozna.ping.Constants.ACTION_REJECT;
import static si.rozna.ping.Constants.EXTRAS_PING_ID;
import static si.rozna.ping.Constants.EXTRAS_PONG_TOPIC;
import static si.rozna.ping.CoreApplication.CHANNEL_ID;

public class PingMessageService extends FirebaseMessagingService {

    public static final int NOTIFICATION_ID = 420420420;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Timber.d("MESSAGE RECEIVED");

        Timber.d("Message id: %s", remoteMessage.getMessageId());
        Timber.d("From: %s", remoteMessage.getFrom());
        Timber.d("Data: %s", remoteMessage.getData());
        Timber.d("MessageType: %s", remoteMessage.getMessageType());

        if(remoteMessage.getFrom() == null)
            return;

        String from = remoteMessage.getFrom();

        if(from.contains(Constants.MESSAGE_PONG)){
            Timber.i("PONG MESSAGE");

        } else if(from.contains(Constants.MESSAGE_PING)) {
            Timber.i("PING MESSAGE");
            PingMessage message = new PingMessage(remoteMessage);
            showNotification(message);
        } else {
            Timber.i("Unknown message has arrived: " + from);
        }
    }

    private void showNotification(PingMessage message) {

        Notification notification = createNotification(message);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NotificationManager.class);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }

    private Notification createNotification(PingMessage message) {

        // Pending intent that will lunch application
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Pending intent that will send PONG (ping response)
        Intent pongActionIntent = new Intent(this, NotificationActionReceiver.class);
        pongActionIntent.setAction(ACTION_PONG);
        pongActionIntent.putExtra(EXTRAS_PING_ID, message.getId());
        pongActionIntent.putExtra(EXTRAS_PONG_TOPIC, String.format(getString(R.string.subscribe_pong_topic), message.getGroup().getId()));
        PendingIntent pongPendingIntent = PendingIntent.getBroadcast(this, 1, pongActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Pending intent that will reject PING
        Intent rejectActionIntent = new Intent(this, PingMessageService.class);
        rejectActionIntent.setAction(ACTION_REJECT);
        PendingIntent rejectPendingIntent = PendingIntent.getService(this, 0, rejectActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Default sound for notifications
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Creates notification content text
        String notificationMessage = String.format("User %s pinged group %s!", message.getUser().getUsername(), message.getGroup().getName());

        // Creates base notification
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.ping))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_baseline_person_pin_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_baseline_add_24, getString(R.string.reject), rejectPendingIntent)
                .addAction(R.drawable.ic_baseline_add_24, getString(R.string.pong), pongPendingIntent)
                .build();

//        RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification_ping_preview);
//
//        // Creates base notification
//        return new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_baseline_person_pin_24)
//                .setCustomContentView(view)
//                .setCustomBigContentView(view)
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                .build();

    }



}
