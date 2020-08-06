package si.rozna.ping.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDTO implements Comparable<GroupDTO> {

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
    public int compareTo(GroupDTO groupDTO) {
        return this.id.compareTo(groupDTO.getId());
    }

    @Override
    public String toString(){
        return "[id: " + this.id
                + ", name: " + this.name
                + ", ownerId: " + this.ownerId + "]";
    }
}
