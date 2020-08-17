package si.rozna.ping.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import si.rozna.ping.database.dao.GroupDao;
import si.rozna.ping.models.db.GroupDbModel;

public class GroupRepository {

    private MutableLiveData<List<GroupDbModel>> searchResults = new MutableLiveData<>();
    private LiveData<List<GroupDbModel>> groups;

    private GroupDao groupDao;

    public GroupRepository(Application application){
        Database db = Database.getDatabase(application);
        groupDao = db.groupDao();
        groups = groupDao.getGroups();
    }

    public void getGroupById(String id) {
        Database.databaseWriterExecutor.execute(() -> searchResults.postValue(groupDao.getGroupById(id)));
    }

    public void addGroup(final GroupDbModel groupDbModel){
        Database.databaseWriterExecutor.execute(() -> groupDao.addGroup(groupDbModel));
    }

    public void deleteGroup(final String id) {
        Database.databaseWriterExecutor.execute(() -> groupDao.deleteGroup(id));
    }

    public LiveData<List<GroupDbModel>> getGroups(){
        return groups;
    }

    public MutableLiveData<List<GroupDbModel>> getSearchResults(){
        return searchResults;
    }

}
