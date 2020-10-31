package group.pinger.models.mappers;

import group.pinger.models.Group;
import group.pinger.models.api.GroupApiModel;
import group.pinger.models.db.GroupDbModel;

public class GroupMapper {

    public static Group fromApiModel(GroupApiModel model) {
        Group group = new Group();
        group.setId(model.getId());
        group.setName(model.getName());
        group.setOwnerId(model.getOwnerId());
        return group;
    }

    public static Group fromDbModel(GroupDbModel model){
        Group group = new Group();
        group.setId(model.getId());
        group.setName(model.getName());
        group.setOwnerId(model.getOwnerId());
        return group;
    }

    public static GroupDbModel toDbModel(Group group) {
        GroupDbModel groupDbModel = new GroupDbModel();
        groupDbModel.setId(group.getId());
        groupDbModel.setName(group.getName());
        groupDbModel.setOwnerId(group.getOwnerId());
        return groupDbModel;
    }

    public static GroupDbModel toDbModelFromApiModel(GroupApiModel group) {
        GroupDbModel groupDbModel = new GroupDbModel();
        groupDbModel.setId(group.getId());
        groupDbModel.setName(group.getName());
        groupDbModel.setOwnerId(group.getOwnerId());
        return groupDbModel;
    }



}
