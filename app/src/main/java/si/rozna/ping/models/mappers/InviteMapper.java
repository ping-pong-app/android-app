package si.rozna.ping.models.mappers;

import si.rozna.ping.models.Invite;
import si.rozna.ping.models.api.InviteApiModel;

public class InviteMapper {

    public static Invite fromApiModel(InviteApiModel model) {
        Invite invite = new Invite();
        invite.setId(model.getId());
        invite.setEmail(model.getEmail());
        invite.setGroupId(model.getGroupId());
        invite.setUserId(model.getUserId());
        return invite;
    }

}
