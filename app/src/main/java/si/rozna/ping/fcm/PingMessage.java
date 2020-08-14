package si.rozna.ping.fcm;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class PingMessage {

    private String id;

    private User user;

    private Group group;

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
        private String id;
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
        private String id;
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
