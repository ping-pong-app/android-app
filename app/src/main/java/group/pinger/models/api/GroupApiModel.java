package group.pinger.models.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupApiModel extends ApiModel {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ownerId")
    @Expose
    private String ownerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @NonNull
    @Override
    public String toString() {
        return "[id: " + this.id
                + ", name: " + this.name
                + ", ownerId: " + this.ownerId + "]";
    }
}
