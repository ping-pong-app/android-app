package si.rozna.ping.models.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public abstract class EntityDbModel {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    private long entityId;

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
