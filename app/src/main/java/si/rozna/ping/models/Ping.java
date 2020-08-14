package si.rozna.ping.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ping implements Comparable<Ping> {

    @SerializedName("id")
    @Expose
    private String id;

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

    @Override
    public int compareTo(Ping ping) {
        return this.id.compareTo(ping.id);
    }
}
