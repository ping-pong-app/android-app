package group.pinger.models.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity
public class GroupDbModel extends EntityDbModel{


    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "ownerId")
    private String ownerId;


    @Ignore
    public GroupDbModel(){}

    public GroupDbModel(String id,
                        String name,
                        String ownerId){
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;

    }



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

    public String toString() {
        return "GroupDbModel {" +
                ", id: '" + id + '\'' +
                ", name: '" + name + '\'' +
                ", ownerId: '" + ownerId + '\'' +
                "}";
    }


}
