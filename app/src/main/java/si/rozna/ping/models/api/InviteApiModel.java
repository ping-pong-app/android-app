package si.rozna.ping.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class InviteApiModel extends ApiModel {

    @SerializedName("userId")
    @Expose
    protected String userId;

    @SerializedName("groupId")
    @Expose
    protected String groupId;

    @SerializedName("email")
    @Expose
    protected String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    @Override
    public String toString() {
        return "Invite{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
