package si.rozna.ping.models.mappers;

import si.rozna.ping.models.Group;
import si.rozna.ping.models.api.GroupApiModel;
import si.rozna.ping.models.db.GroupDbModel;

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

}
