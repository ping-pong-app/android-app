package group.pinger.fcm;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;

public class FcmService {

    private static final String PING_TOPIC = "PING.%s";
    private static final String PING_REPLY_TOPIC = "PING.REPLY.%s";

    /**
     * Subscribes to multiple topics on FCM broker
     * @param topics list of topics to subscribe to
     */
    public static void subscribe(String... topics) {
        Arrays.stream(topics).forEach(topic
                -> FirebaseMessaging.getInstance().subscribeToTopic(topic));
    }

    /**
     * Unsubscribe from multiple topics on FCM broker
     * @param topics list of topics to unsubscribe from
     */
    public static void unsubscribe(String... topics){
        Arrays.stream(topics).forEach(topic
                -> FirebaseMessaging.getInstance().unsubscribeFromTopic(topic));
    }

    public static void subscribeToPing(String groupId){
        String topic = String.format(PING_TOPIC, groupId);
        subscribe(topic);
    }

    public static void unsubscribeFromPing(String groupId){
        String topic = String.format(PING_TOPIC, groupId);
        unsubscribe(topic);
    }

    public static void subscribeToPong(String groupId){
        String topic = String.format(PING_REPLY_TOPIC, groupId);
        subscribe(topic);
    }

    public static void unsubscribeFromPong(String groupId){
        String topic = String.format(PING_REPLY_TOPIC, groupId);
        unsubscribe(topic);
    }

}
