package si.rozna.ping.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PongApiModel extends ApiModel {

    @SerializedName("pingId")
    @Expose
    private String pingId;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public String getPingId() {
        return pingId;
    }

    public void setPingId(String pingId) {
        this.pingId = pingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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
