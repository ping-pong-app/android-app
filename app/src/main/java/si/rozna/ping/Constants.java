package si.rozna.ping;

public class Constants {

    /* Database */
    public static final String DB_NAME = "si.rozna.ping.db";

    /* Rest API */
    public static final String BASE_URL = "https://api.pinger.group/v1/";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    /* Shared preferences */
    public static final String SHARED_PREFERENCES_KEY = "si.rozna.ping.shared_pref";
    public static final String SHARED_PREFERENCES_CACHE_KEY = "isCached";

    /* UI */
    public static final int MIN_GROUP_NAME_LENGTH = 3;
    public static final int SPLASH_SCREEN_DURATION_IN_MILLIS = 1000;
    public static final int DISMISS_SUCCESSFUL_INVITE_AFTER = 1000;
    public static final int TIME_BETWEEN_REFRESH_IN_MS = 10000;


    /* Messaging */
    public static final String MESSAGE_PING = "PING";
    public static final String MESSAGE_PONG = "PING.REPLY";
    public static final String ACTION_PONG = "notification_ping_action_pong";
    public static final String ACTION_REJECT = "notification_ping_action_reject";
    public static final String EXTRAS_PING_ID = "intent_extras_ping_id";
    public static final String EXTRAS_GROUP_ID = "intent_extras_group_id";
    public static final String EXTRAS_NOTIFICATION_ID = "intent_extras_notification_id";


}
