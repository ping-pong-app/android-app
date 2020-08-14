package si.rozna.ping.models.mappers;

import si.rozna.ping.models.Group;
import si.rozna.ping.models.api.GroupApiModel;

public class GroupMapper {

    public static Group fromApiModel(GroupApiModel model) {
        Group group = new Group();
        group.setId(model.getId());
        group.setName(model.getName());
        group.setOwnerId(model.getOwnerId());
        return group;
    }

}
