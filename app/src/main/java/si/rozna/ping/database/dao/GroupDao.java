package si.rozna.ping.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import si.rozna.ping.models.db.GroupDbModel;

public interface GroupDao {

    @Query("SELECT * FROM GroupDbModel WHERE id = :id")
    List<GroupDbModel> getGroupById(String id);

    @Query("SELECT * FROM GroupDbModel")
    LiveData<List<GroupDbModel>> getGroups();

    @Insert
    void addGroup(GroupDbModel groupDbModel);

    @Delete
    void deleteGroup(String id);


}
