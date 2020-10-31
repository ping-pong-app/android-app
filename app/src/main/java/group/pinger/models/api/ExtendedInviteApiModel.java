package group.pinger.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendedInviteApiModel extends InviteApiModel {

    @SerializedName("group")
    @Expose
    private GroupApiModel group;

    @SerializedName("user")
    @Expose
    private UserApiModel user;

    public GroupApiModel getGroup() {
        return group;
    }

    public void setGroup(GroupApiModel group) {
        this.group = group;
    }

    public UserApiModel getUser() {
        return user;
    }

    public void setUser(UserApiModel user) {
        this.user = user;
    }
}
