package si.rozna.ping.fcm;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;

public class FcmService {

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

}
