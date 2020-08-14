package si.rozna.ping.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityIdentifier implements Comparable<EntityIdentifier> {

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("ID: %s", this.id);
    }

    @Override
    public int compareTo(EntityIdentifier entityIdentifier) {
        return this.id.compareTo(entityIdentifier.id);
    }
}
