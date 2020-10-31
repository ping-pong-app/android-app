package group.pinger.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import group.pinger.database.dao.GroupDao;
import group.pinger.fcm.FcmService;
import group.pinger.models.db.GroupDbModel;

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
        FcmService.subscribeToPing(groupDbModel.getId());
    }

    public void deleteGroup(final String id) {
        FcmService.unsubscribeFromPing(id);
        FcmService.unsubscribeFromPong(id);
        Database.databaseWriterExecutor.execute(() -> groupDao.deleteGroup(id));
    }

    public void dropTable(){
        if(groups != null && groups.getValue() != null) {
            for (GroupDbModel groupDbModel : groups.getValue()) {
                FcmService.unsubscribeFromPing(groupDbModel.getId());
                FcmService.unsubscribeFromPong(groupDbModel.getId());
            }
        }
        Database.databaseWriterExecutor.execute(() -> groupDao.dropTable());
    }

    public LiveData<List<GroupDbModel>> getGroups(){
        return groups;
    }

    public MutableLiveData<List<GroupDbModel>> getSearchResults(){
        return searchResults;
    }

}
