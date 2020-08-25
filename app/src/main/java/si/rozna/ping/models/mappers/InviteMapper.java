package si.rozna.ping.models.mappers;

import si.rozna.ping.models.ExtendedInvite;
import si.rozna.ping.models.Group;
import si.rozna.ping.models.Invite;
import si.rozna.ping.models.User;
import si.rozna.ping.models.api.ExtendedInviteApiModel;
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

    public static ExtendedInvite fromExtendedApiModel(ExtendedInviteApiModel model) {
        ExtendedInvite invite = new ExtendedInvite();
        invite.setId(model.getId());
        invite.setEmail(model.getEmail());
        invite.setGroupId(model.getGroupId());
        invite.setUserId(model.getUserId());

        invite.setUser(new User());
        if (model.getUser() != null) {
            invite.getUser().setId(model.getUser().getId());
            invite.getUser().setEmail(model.getUser().getEmail());
            invite.getUser().setDisplayName(model.getUser().getDisplayName());
            invite.getUser().setPhotoUrl(model.getUser().getPhotoUrl());
        }
        invite.setGroup(new Group());
        if (model.getGroup() != null) {
            invite.getGroup().setId(model.getGroup().getId());
            invite.getGroup().setName(model.getGroup().getName());
            invite.getGroup().setOwnerId(model.getGroup().getOwnerId());
        }

        return invite;
    }

}
