package group.pinger.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PingApiModel extends ApiModel {

    @SerializedName("groupId")
    @Expose
    private String groupId;

    @SerializedName("pingerId")
    @Expose
    private String pingerId;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPingerId() {
        return pingerId;
    }

    public void setPingerId(String pingerId) {
        this.pingerId = pingerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
