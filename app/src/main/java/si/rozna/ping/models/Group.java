package si.rozna.ping.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group implements Comparable<Group> {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ownerId")
    @Expose
    private String ownerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    @Override
    public int compareTo(Group group) {
        return this.id.compareTo(group.getId());
    }

    @NonNull
    @Override
    public String toString() {
        return "[id: " + this.id
                + ", name: " + this.name
                + ", ownerId: " + this.ownerId + "]";
    }
}
