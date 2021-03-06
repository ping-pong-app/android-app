package group.pinger.fcm;

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

import java.util.Optional;

import group.pinger.Constants;
import group.pinger.R;
import group.pinger.auth.AuthService;
import group.pinger.auth.LoginActivity;
import timber.log.Timber;

import static group.pinger.Constants.ACTION_PONG;
import static group.pinger.Constants.ACTION_REJECT;
import static group.pinger.Constants.EXTRAS_GROUP_ID;
import static group.pinger.Constants.EXTRAS_NOTIFICATION_ID;
import static group.pinger.Constants.EXTRAS_PING_ID;
import static group.pinger.CoreApplication.CHANNEL_ID;

public class PingMessageService extends FirebaseMessagingService {

//    public static final int NOTIFICATION_ID = 420420420;

    public static int NOTIFICATION_ID = 0;

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
            PongMessage message = new PongMessage(remoteMessage);
            Optional<String> userId = AuthService.getCurrentUserId();
            // If message is from sender, don't show notification
            if(userId.isPresent()) {
                if (!userId.get().equals(message.getUser().getId()))
                    showPongNotification(message);
            }

        } else if(from.contains(Constants.MESSAGE_PING)) {
            PingMessage message = new PingMessage(remoteMessage);
            Optional<String> userId = AuthService.getCurrentUserId();
            // If message is from sender, don't show notification
            if(userId.isPresent()) {
                if (!userId.get().equals(message.getUser().getId()))
                    showPingNotification(message);
            }

        } else {
            Timber.i("Unknown message has arrived: %s", from);
        }
    }

    private void showPingNotification(PingMessage pingMessage) {

        // Pending intent that will lunch application
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Pending intent that will send PONG (ping response)
        Intent pongActionIntent = new Intent(this, NotificationActionReceiver.class);
        pongActionIntent.setAction(ACTION_PONG);
        pongActionIntent.putExtra(EXTRAS_PING_ID, pingMessage.getId());
        pongActionIntent.putExtra(EXTRAS_GROUP_ID, pingMessage.getGroup().getId());
        pongActionIntent.putExtra(EXTRAS_NOTIFICATION_ID, NOTIFICATION_ID);
        PendingIntent pongPendingIntent = PendingIntent.getBroadcast(this, 1, pongActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Pending intent that will reject PING
        Intent rejectActionIntent = new Intent(this, NotificationActionReceiver.class);
        rejectActionIntent.setAction(ACTION_REJECT);
        rejectActionIntent.putExtra(EXTRAS_PING_ID, pingMessage.getId());
        rejectActionIntent.putExtra(EXTRAS_GROUP_ID, pingMessage.getGroup().getId());
        rejectActionIntent.putExtra(EXTRAS_NOTIFICATION_ID, NOTIFICATION_ID);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 0, rejectActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Default sound for notifications
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Creates notification content text
        String notificationMessage = String.format("User %s pinged group %s!", pingMessage.getUser().getUsername(), pingMessage.getGroup().getName());

        // Creates base notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
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

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NotificationManager.class);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
            NOTIFICATION_ID += 1;
        }

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


    private void showPongNotification(PongMessage pongMessage) {

        // Pending intent that will lunch application
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Default sound for notifications
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Creates notification content text
        // TODO: Make enum!
        String notificationMessage = "You shouldn't see this!";
        if(pongMessage.getResponse().equals("REJECT")) {
            notificationMessage = String.format("User %s from group %s has REJECTED invite!", pongMessage.getUser().getUsername(), pongMessage.getGroup().getName());
        } else if (pongMessage.getResponse().equals("PONG")) {
            notificationMessage = String.format("User %s from group %s has PONG-ed!", pongMessage.getUser().getUsername(), pongMessage.getGroup().getName());
        }

        // Creates base notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.ping))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_baseline_person_pin_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NotificationManager.class);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
            NOTIFICATION_ID += 1;
        }

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
