package group.pinger.models.api;

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
}
