package si.rozna.ping.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import si.rozna.ping.models.db.GroupDbModel;

@Dao
public interface GroupDao {

    @Query("SELECT * FROM GroupDbModel WHERE id = :id")
    List<GroupDbModel> getGroupById(String id);

    @Query("SELECT * FROM GroupDbModel")
    LiveData<List<GroupDbModel>> getGroups();

    @Insert
    void addGroup(GroupDbModel groupDbModel);

    @Query("DELETE FROM GROUPDBMODEL WHERE id = :id")
    void deleteGroup(String id);


}
