package group.pinger.ui.drawer.groups;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import group.pinger.database.GroupRepository;
import group.pinger.models.db.GroupDbModel;

public class GroupsViewModel extends AndroidViewModel {

    private LiveData<List<GroupDbModel>> groups;
    private MutableLiveData<List<GroupDbModel>> searchResult;

    public GroupRepository groupRepository;

    public GroupsViewModel(@NonNull Application application) {
        super(application);

        groupRepository = new GroupRepository(application);
        groups = groupRepository.getGroups();
        searchResult = groupRepository.getSearchResults();
    }

    public LiveData<List<GroupDbModel>> getGroups(){
        return groups;
    }

    public MutableLiveData<List<GroupDbModel>> getSearchResult() {
        return searchResult;
    }

    public void getGroupById(String id){
        groupRepository.getGroupById(id);
    }

    public void addGroup(GroupDbModel groupDbModel){
        groupRepository.addGroup(groupDbModel);
    }

    public void deleteGroup(String id){
        groupRepository.deleteGroup(id);
    }

}
