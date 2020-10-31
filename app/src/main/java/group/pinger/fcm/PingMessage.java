package group.pinger.fcm;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class PingMessage {

    /**
     * ID of ping entity
     */
    private String id;

    /**
     * User data of ping initiator
     */
    private User user;

    /**
     * Group data of pinged group
     */
    private Group group;

    /**
     * Constructs new message instance from FCM message
     *
     * @param message message received from FCM broker
     */
    public PingMessage(RemoteMessage message) {
        Map<String, String> data = message.getData();
        this.user = new User(data.get("userId"), data.get("username"));
        this.group = new Group(data.get("groupId"), data.get("groupName"));
        this.id = data.get("pingId");
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }

    public String getId() {
        return id;
    }

    public static class User {

        /**
         * User id
         */
        private String id;

        /**
         * User username
         */
        private String username;

        private User(String id, String username) {
            this.id = id;
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class Group {

        /**
         * Group id
         */
        private String id;

        /**
         * Group name
         */
        private String name;

        private Group(String id, String groupName) {
            this.id = id;
            this.name = groupName;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}
