package si.rozna.ping.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Used only for POST requests.
 * Will never be returned by server.
 * */
public class PingDTO {

    @SerializedName("groupId")
    @Expose
    private String groupId;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
